package com.kaishengit.contorller.business;

import com.google.common.collect.Maps;
import com.kaishengit.dto.AjaxResult;
import com.kaishengit.dto.OutSourcingDto;
import com.kaishengit.exception.NotFoundException;
import com.kaishengit.pojo.Craft;
import com.kaishengit.pojo.CraftOutSourcing;
import com.kaishengit.pojo.CraftOutSourcingDocs;
import com.kaishengit.service.OutSourcingService;
import com.kaishengit.utiils.Page;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * Created by 刘忠伟 on 2017/2/18.
 */
@Controller
@RequestMapping("/business/outSourcing")
public class OutSourcingContorller {


    //contorller被扫描会纳入bean管理，所以可以自动注入
    @Autowired
    private OutSourcingService outSourcingService;


    /**
     * 使用datatables插件完成表格搜索，发送的是ajax请求获取值，所以这里不需要传值
     * @return
     */
    @GetMapping
    public String outSourcingList() {

        return "business/outSourcing/list";
    }

    /**
     * 搜索，datatables发送ajax请求
     * @return
     */
    @GetMapping("/data.json")
    @ResponseBody
    public Map<String,Object> searching(Integer draw,Integer start,Integer length,
                            String serialNumber,String companyName,String status,
                            String startDate,String stopDate) throws UnsupportedEncodingException {
        //转换编码
        if(StringUtils.isNotEmpty(companyName)){
            companyName = new String(companyName.getBytes("iso-8859-1"), "utf-8");
        }

        if(StringUtils.isNotEmpty(status)){
            status = new String(status.getBytes("iso-8859-1"), "utf-8");
        }

        List<CraftOutSourcing> sourcingList = outSourcingService.findBySearch(start,length,serialNumber,companyName,status,startDate,stopDate);
        //需要知道一共有多少条数据
        Integer total = outSourcingService.findBySearchCount(serialNumber,companyName,status,startDate,stopDate);
        Map<String,Object> map = Maps.newHashMap();
        map.put("draw",draw);
        map.put("recordsTotal",total);
        map.put("recordsFiltered",total);
        map.put("data",sourcingList);

        return map;
    }

    /**
     * 添加新劳务合同
     * @param model
     * @return
     */
    @GetMapping("/new")
    public String outSourcingNew(Model model){
        //需要把所有工种返回
        List<Craft> craftList = outSourcingService.findCraftAll();

        model.addAttribute("craftList",craftList);
        return "business/outSourcing/new";
    }

    /**
     * 添加新的劳务合同的post提交，传过来的是大json字符串，需要定义dto类，使用springmvc特性，自动封装，属性要和json属性一致
     * @return 返回json，需要把新增的流水号返回，用于客户端显示哪个合同
     */
    @PostMapping("/new")
    @ResponseBody
    public AjaxResult outSourcingNew(@RequestBody OutSourcingDto outSourcingDto){//根据springmvc自动将json值封装到dto
        try {
            String serialNumber = outSourcingService.saveOutSourcingContract(outSourcingDto);

            return new AjaxResult(AjaxResult.SUCCESS,serialNumber);
        } catch (RuntimeException ex){
            return new AjaxResult(ex.getMessage());
        }
    }


    /**
     * 根据select框的选择发送json即carft的id，查数据
     * @param carftId
     * @return
     */
    @GetMapping("/carft.json")
    @ResponseBody
    public AjaxResult getCarft(Integer carftId){
        Craft craft = outSourcingService.findCraftById(carftId);
        //因为是ajax相当于?传值，可以改变，所以判断一下
        if(craft != null){
            return new AjaxResult(craft);
        } else {
            return new AjaxResult("您选择的工种并不存在！");
        }

    }


    /**
     * 根据流水号查合同，三个表。跳转到文件详情页面
     * @param serialNumber
     * @param model
     * @return
     */
    @GetMapping("/{serialNumber:\\d+}")//必须是数字不是数字404
    public String showContract(@PathVariable String serialNumber,Model model){//不能强转403
        //根据serialNumber查一遍数据库,三张表
        Map<String,Object> map = outSourcingService.findOutSourcing(serialNumber);
        model.addAttribute("map",map);
        return "business/outSourcing/show";
    }

    /**
     * 劳务外包文件下载。普通文件下载没有return，直接把文件放到响应输出流
     * @param id 根据文件的id
     */
    @GetMapping("/download/{id:\\d+}")
    public void download(@PathVariable Integer id, HttpServletResponse response) throws IOException {

        CraftOutSourcingDocs docs = outSourcingService.findDocsById(id);

        //获取文件的输入流
        FileInputStream inputStream = outSourcingService.getIntPutStream(id);
        //设置文件的响应头类型为客户端不能识别的二进制
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        //设置下载的文件名字为旧名字
        String fileName = docs.getSourceName();
        fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
        response.setHeader("Content-Disposition","attachment;filename=\""+fileName+"\"");

        OutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream,outputStream);

        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }


    /**
     * 劳务外包文件的打包下载打包成zip
     * @param id
     * @param response
     */
    @GetMapping("/docs/zipDownload/{id:\\d+}")
    public void zipDownload(@PathVariable Integer id ,HttpServletResponse response) throws IOException {

        CraftOutSourcing sourcing = outSourcingService.findOutSourcingById(id);
        if(sourcing == null){
            throw new NotFoundException();
        } else {
            //设置文件响应头，类型为不能识别的
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            //设置默认的名字为公司名字

            String fileName = sourcing.getCompanyName()+".zip";
            fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
            response.setHeader("Content-Disposition","attachment;filename=\""+fileName+"\"");

            OutputStream outputStream = response.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            outSourcingService.zipDownload(id, zipOutputStream);
        }
    }

    /**
     * 劳务外包的    状态修改
     * @return
     */
    @PostMapping("/status/finish")
    @ResponseBody
    public AjaxResult statusFinish(Integer id){
        outSourcingService.updateStatus(id);

        return null;
    }


}
