package com.kaishengit.contorller;

import com.google.common.collect.Maps;
import com.kaishengit.pojo.Device;
import com.kaishengit.pojo.User;
import com.kaishengit.service.DeviceService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by 刘忠伟 on 2017/1/17.
 *  设备管理控制器
 */
@Controller
@RequestMapping("/device")
public class DeviceContorller {

    Logger logger = LoggerFactory.getLogger(DeviceContorller.class);

    @Autowired
    private DeviceService deviceService;//接口指向实现类，面向接口编程。降低耦合性，便于管理。可扩展


    @RequestMapping
    public String list(){
        //返回所有的设备，使用datatable插件之后，表格发出的是ajaxget请求。不需要跳转时就查询全部数据，先跳转然后表发异步。在接受json即可。虽然不写，但是直接跳转到这里也是可以的。只不过数据，不是跳转传的，是ajax传的
        /*List<Device> deviceList = deviceService.findAll();
        moder.addAttribute("deviceList",deviceList);*/
        return "device/list";
    }

    @GetMapping("/load")
    @ResponseBody
    public Map load(Integer draw, Integer start, Integer length,
                    @RequestParam("order[0][dir]") String orderType,
                    @RequestParam("order[0][column]") Integer orderIndex,
                    HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
        //orderName是有多个的，需要确定哪一个列的name。但是order[0][column]根据第几列排序是就一个确定的。两个个连起来，获取到按照哪一列排序的列名
        String orderColumn = httpServletRequest.getParameter("columns[" + orderIndex + "][name]");

        //添加搜索，jsp传过来的搜索内容。需要获取deviceName
        String deviceName = httpServletRequest.getParameter("deviceName");
        //get请求需要转码
        deviceName = new String(deviceName.getBytes("ISO8859-1"),"UTF-8");


        //发送的是ajax异步get请求，所以返回的不再是视图层，而是内容json。
        //并且传过来的值是“columns[4][data]”这种形式的，所以不能直接当作变量@RequestParam("columns[4][data]") Integer id
        //每次传过来的draw是请求的次数，需要把这个值返回过去。

        //需要添加排序功能，只根据totalNum或者price排序。    获取排序的列名和是升序还是降序
        //order[0][column]是按照第几列排序。并且我们设置了，第几列的name值对应其表的列名。所以获取name值columns[5][name]

        Map<String,Object> map = Maps.newHashMap();
        map.put("start",start);
        map.put("length",length);
        map.put("orderType",orderType);
        map.put("orderColumn",orderColumn);//根据那一列排序的列名
        map.put("deviceName",deviceName);
        List<Device> deviceList = deviceService.findPageDataTables(map);

        //datatable需要的返回值类型是一个json对象。map集合可以被json转换成一个对象
        Map<String,Object> resultMap = Maps.newHashMap();
        //1需要把请求的次数原值返回draw
        resultMap.put("draw",draw);
        //2需要返回一共多少条数据recordsTotal，所以在查一次
        Long count = deviceService.findCount(deviceName);
        resultMap.put("recordsTotal",count);
        //3返回recordsFiltered
        resultMap.put("recordsFiltered",count);
        //4返回data数据
        resultMap.put("data",deviceList);

        //springmvc之前添加的转换json依赖，就自动转换了，只需要返回集合或者对象
        return resultMap;

    }



    @GetMapping("/new")
    public String newDevice(){
        return "device/new";
    }
    @PostMapping("/new")
    public String newDevice(Device device, RedirectAttributes redirectAttributes){//name值要和对象和属性值一致，就会自动获取post提交的值封装到device

        deviceService.save(device);
        //使用shiro权限之后，添加设备要记录日志谁添加的。所以就需要当前登录对象，使用shiro。原理就是session获取。，可在jsp和servlet获取。jsp是使用shiro标签。在servlet使用subject
        //所有在服务端使用shiro的东西，需要创建用户对象subject。此subject对象在哪里都可以创建。可孩子i系那个logout和login。或者获取对象
        Subject subject = SecurityUtils.getSubject();
        //创建subject用户之后，获取当前登录对象
        User user = (User) subject.getPrincipal();

        logger.debug("{}添加了设备-------------------------{}",user.getUserName(),device.getName());

        /*redirectAttributes类用于，重定向到get请求转发到jsp，这个过程自动完成。直接将值传到jsp，用于通知，添加完成。一般不能直接重定向传值到get。springmvc使用过滤器完成的*/
        redirectAttributes.addFlashAttribute("message","添加新设备成功！");//flash闪的
        return "redirect:/device";
    }

    @GetMapping("/{id:\\d+}/edit")
    public String edit(@PathVariable Integer id,Model model){
        Device device = deviceService.findById(id);
        model.addAttribute("device",device);
        return "device/edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String edit(Device device,RedirectAttributes redirectAttributes){//直接使用隐藏域，把id传值过来了，不用使用地址栏获取了。隐藏域的安全性也更高一些

        deviceService.update(device);//这里只有一个sql，成功就全成功，失败全失败，不用开启声明式事务

        redirectAttributes.addFlashAttribute("message","设备修改成功！");
        return "redirect:/device";
    }

    @GetMapping("/{id:\\d+}/del")
    //是异步请求，需要返回内容，字符串。动态url传值，不符合正则，404，不能强转400
    @ResponseBody
    public String del(@PathVariable Integer id){
        deviceService.delete(id);
        //不管存不存在删除即可
        return "success";
    }


}
