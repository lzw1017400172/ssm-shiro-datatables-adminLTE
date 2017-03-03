package com.kaishengit.contorller.business;

import com.kaishengit.dto.AjaxResult;
import com.kaishengit.dto.DataTableResult;
import com.kaishengit.dto.DeviceRentDto;
import com.kaishengit.exception.NotFoundException;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.pojo.Device;
import com.kaishengit.pojo.DeviceRent;
import com.kaishengit.pojo.DeviceRentDocs;
import com.kaishengit.service.DeviceService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.condition.MediaTypeExpression;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * Created by 刘忠伟 on 2017/2/16.
 * 设备租凭控制器
 * 已经被springmvc扫描，成为控制器
 */
@Controller
@RequestMapping("/business/device/rent")
public class DeviceRentContorller {

    //自动注入，接口指向实现类
    @Autowired
    private DeviceService deviceService;


    /**
     * 跳转到设备租凭页面
     * @return
     */
    @GetMapping
    public String deviceRentList(){

        return "business/rent/list";
    }

    /**
     * 根据合同序列号查找合同
     * @param serialNumber 合同序列号
     * @return
     * 动态url传值，必须使用注解。不符合404，不能强转403
     */
    @GetMapping("/{serialNumber:\\d+}")
    public String deviceRentSerialNumber(@PathVariable String serialNumber,Model model){
        Map<String,Object> map =  deviceService.findBySerialNumber(serialNumber);
        model.addAttribute("map",map);
        return "business/rent/show";
    }

    /**
     * 设备租赁的文件下载，根据文件的id之下载指定单个文件
     * @param id
     * @return
     * 输出流不需要return返回值
     */
    @GetMapping("/docs/{id:\\d+}")
    public void download(@PathVariable Integer id, HttpServletResponse response) throws IOException {

        FileInputStream inputStream = deviceService.download(id);

        if(inputStream!= null){

            DeviceRentDocs docs = deviceService.findDocsById(id);

            //设置响应头,类型为客户端不能识别的，就会出现下载，能识别就显示
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            //更改文件下载框默认的文件名字为原文件名
            String fileName = docs.getSourceName();
            fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
            //格式是："Content-Disposition","attachment;filename='fileName'"
            response.setHeader("Content-Disposition","attachment;filename=\""+ fileName +"\"");

            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            inputStream.close();
            outputStream.close();

        } else{
            throw  new NotFoundException();
        }
    }

    /**
     * 打成zip包下载，打包下载，根据租赁合同rent_id
     * 文件下载需要响应输出流，不需要return返回
     */
    @GetMapping("/docs/zip/{id:\\d+}")
    public void ZipDownload(@PathVariable Integer id,HttpServletResponse response) throws IOException {

        //判断此合同是否存在
        DeviceRent rent = deviceService.findRentById(id);
        if(rent!=null){
            //设置响应头为客户端不能识别
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            //设置响应文件下载的默认名字  为公司名字zip包的名字
            String fileName = rent.getCompanyName()+".zip";
            fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
            response.setHeader("Content-Disposition","attachment;filename=\""+fileName+"\"");

            //创建zip包的文件输出流
            OutputStream outputStream = response.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            deviceService.zipDownload(rent,zipOutputStream);

        } else {
            throw new NotFoundException();
        }

    }


    /**
     * 跳转到新增设备租凭合同页面，并且附带设备的信息
     * @return
     */
    @GetMapping("/new")
    public String deviceRentAdd(Model model){
        //传过去值所有设备信息
        List<Device> deviceList = deviceService.findAll();
        model.addAttribute("deviceList",deviceList);
        return "business/rent/new";
    }

    /**
     * 添加新的租赁，数据太多，并且需要保证事务，所以客户端传过来的是，转换好的json字符串，在这里需要转换成对象
     * 所以封装一个新的类dto，用来转化成相应对象，才会根据springmvc特性自动装到此对象.在服务端接受json使用@RequestBody注解
     * @return
     */
    @PostMapping("/new")
    @ResponseBody
    public AjaxResult deviceRentAdd(@RequestBody DeviceRentDto deviceRentDto){
        try{
            String serialNumber = deviceService.saveRent(deviceRentDto);
            AjaxResult ajaxResult = new AjaxResult();
            ajaxResult.setStatus(AjaxResult.SUCCESS);
            ajaxResult.setData(serialNumber);
            return ajaxResult;
        } catch (RuntimeException ex) {
            return new AjaxResult(ex.getMessage());
        }

    }

    /**
     * 获取服务器时间
     * @return
     */
    @GetMapping("/time")
    @ResponseBody
    public Date deviceRentTime(){

        return new Date();
    }

    /**
     * 查询指定id的设备的信息，返回json
     * @return
     */
    @GetMapping("/device.json")
    @ResponseBody
    public AjaxResult getDevice(Integer deviceId){
        Device device = deviceService.findById(deviceId);
        if(device == null){
            return new AjaxResult(AjaxResult.ERROR,"设备不存在！");
        } else {
            return new AjaxResult(device);
       }

    }


    /**
     * deviceRent展示界面，使用dtatatables插件，接受值，返回json。默认根据id排序
     * @return
     */
    @GetMapping("/datatables.json")
    @ResponseBody
    public DataTableResult DeviceRentList(Integer draw,Integer start, Integer length){

        List<DeviceRent> rentList = deviceService.findDeviceRent(start,length);
        //查询总共多少条数据
        Integer total = deviceService.findDeviceRentCount();
        DataTableResult result = new DataTableResult(draw,total,total,rentList);
        return result;
    }

    /**
     * 客户端点击完成，设备租赁合同状态 变成 完成
     * @return
     */
    @PostMapping("/finish")
    @ResponseBody
    public AjaxResult finish(Integer id){
        try {
            deviceService.finish(id);
            return new AjaxResult(AjaxResult.SUCCESS,"修改成功！");
        }catch (ServiceException ex){
            return new AjaxResult(ex.getMessage());
        }
    }

}
