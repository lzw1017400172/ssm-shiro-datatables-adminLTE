package com.kaishengit.contorller;

import com.kaishengit.dto.AjaxResult;
import com.kaishengit.dto.OutSourcingDto;
import com.kaishengit.exception.NotFoundException;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.pojo.Disk;
import com.kaishengit.service.DiskService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.metal.MetalFileChooserUI;
import java.io.*;
import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/21.
 * 网盘系统的控制器
 */
@Controller
@RequestMapping("/disk")
public class DiskContorller {


    @Autowired
    private DiskService diskService;


    /**
     * 因为文件夹是分级显示的，第一次进来是根下的。根下的fid=0，所以
     * @return
     */
    @GetMapping("/list")
    public String diskList(@RequestParam(required = false,defaultValue = "0")Integer fid, Model model){
        //根据fid查找，属于此文件夹下的所有资源。fid=0表示根下
        List<Disk> diskList = diskService.findByFid(fid);
        model.addAttribute("diskList",diskList);
        //当页面显示的是fid的文件下一级，需要把fid传回去，新建文件夹和上传文件需要知道自己的位置，虽然都是保存在一个文件夹里了
        model.addAttribute("id",fid);
        return "disk/list";
    }

    /**
     * 文件下载单个文件下载，点击文件时，
     * 传值资源id
     */
    @GetMapping("/download/{id:\\d+}")
    public void download(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        Disk disk = diskService.findById(id);
        if (disk == null || Disk.FOLDER.equals(disk.getType())) {
            throw new NotFoundException();
        } else {
            //单个的文件下载
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            String fileName = disk.getSourceName();
            /*字符串编码倒转*/
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            FileInputStream inputStream = diskService.getInputStream(disk);
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);

            outputStream.flush();
            inputStream.close();
            outputStream.close();
        }


    }


    /**
     * 文件上传，并且写入网盘数据库，此文件所在位置。即fid=id
     * @param file
     * @param fid    文件的fid
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult upload(Integer fid,MultipartFile file){//WebUpload传值fomDATA可以直接获取


        try {
            diskService.saveNewFile(file,fid);//创建新数据，并且上传到磁盘
            return new AjaxResult(fid);
        } catch (ServiceException e) {
            e.printStackTrace();
            return  new AjaxResult(e.getMessage());
        }

    }


    /**
     * 点击创新文件夹，在当前文件夹创建，当前文件夹为fid
     * @param fid
     * @param newFolderName
     * @return
     */
    @GetMapping("/new/folder")
    @ResponseBody
    public AjaxResult newFolder(Integer fid,String newFolderName) throws UnsupportedEncodingException {//获取?的传参

        diskService.saveNewFolder(fid,new String(newFolderName.getBytes("ISO8859-1"),"UTF-8"));
        return  new AjaxResult(AjaxResult.SUCCESS,"");
    }


    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(Integer id){

        diskService.remove(id);

        return new AjaxResult(AjaxResult.SUCCESS,"");
    }


}
