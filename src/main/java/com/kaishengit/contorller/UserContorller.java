package com.kaishengit.contorller;

import com.kaishengit.exception.NotFoundException;
import com.kaishengit.pojo.Role;
import com.kaishengit.pojo.User;
import com.kaishengit.service.UserService;
import com.kaishengit.utiils.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by 刘忠伟 on 2017/1/12.
 */
/*使用扫描注解，确定是contorller类*/
@Controller
@RequestMapping("/user")
public class UserContorller {

    Logger logger = LoggerFactory.getLogger(UserContorller.class);
    /*这里一定需要service对象，已经使用注解被bean管理。只需要自动注入。面向接口编程，注入实现类对象*/
    @Autowired
    private UserService userService;

    /*加上分页，需要jsp传过来p页数*/
    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "p",required = false,defaultValue = "1") Integer pageNum,
                       @RequestParam(required = false,defaultValue = "",name = "q_name") String userName,
                       @RequestParam(required = false,defaultValue = "",name = "q_role") Integer roleId,
                       Model model) throws UnsupportedEncodingException {  //@RequestParam是获取url或者表单传参数。只要是请求
        //传过来空的话就是1，不是数字就是400，参数和类型不符合，不能强转就报404.requrired不是必须的。默认1
        logger.debug("p--------------------"+pageNum);
        //get传过来的值，变成utf-8编码
        if(StringUtils.isNotEmpty(userName)) {

            userName = new String(userName.getBytes("ISO8859-1"), "UTF-8");
            logger.debug("userName--------------------"+userName);
        }
        //查询全部，返回到jsp
        //Page page = userService.findAll(pageNum);
        Page page = userService.findAllByPageAndSearchParam(pageNum,userName,roleId);
        List<Role> roleList = userService.finRoleAll();
        //查询全部角色，作为搜索时的下拉框
        model.addAttribute("page",page);
        model.addAttribute("roleList",roleList);
        /*把关键子传过去，当点击第二页的时候，仍然使用这两个关键字查*/
        model.addAttribute("q_name",userName);
        model.addAttribute("q_role",roleId);

        /*配置过视图解析器*/
        return "user/list";
    }

    @RequestMapping(value="/new",method = RequestMethod.GET)
    public String save(Model model){
        List<Role> roles = userService.finRoleAll();
        model.addAttribute("roleList",roles);
        return "user/new";
    }

    @RequestMapping(value="/new",method = RequestMethod.POST)
    public String save(Integer[] roleIds,User user, RedirectAttributes redirectAttributes){

        userService.saveNewUser(user,roleIds);
        //传值告诉界面添加成功。redirectAttribute
        redirectAttributes.addFlashAttribute("message","添加用户成功！");
        /*添加成功之后，跳转到user显示全部界面。并且传值重定向(重定向不能传值，原理使用过滤器将值传到下一个url)传值到/user，在将值setAtt请求转发到jsp，这个过程mvc自动完成。显示提示信息。使用redirectAttributes*/
        return "redirect:/user";
    }

    @RequestMapping("/{id:\\d+}/del")
    public String del(@PathVariable Integer id,RedirectAttributes redirectAttributes){
        /*同样正则不需要判断是数字了，不是就404*/
        userService.del(id);
        redirectAttributes.addFlashAttribute("message","删除成功！");//这个值会竟重定向，再经请求转发到jsp
        return "redirect:/user";

    }

    @RequestMapping(value = "/{id:\\d+}/edit", method = RequestMethod.GET)
    public String update(@PathVariable Integer id,Model model){
        /*这种动态url传值，不用对参数id做非数字判断了，正则已经判断。不符合就直接404*/
        //先去找此对象
        User user = userService.findById(id);
        List<Role> roles = userService.finRoleAll();
        if(user != null){
            model.addAttribute("roleList",roles);
            model.addAttribute("user",user);
        } else {
            throw new NotFoundException();/*这是使用注解完成的spring配置的自定义异常，找不到情况的404*/
        }
        return "user/edit";
    }

    @RequestMapping(value = "/{id:\\d+}/edit",method=RequestMethod.POST)
    public String update(Integer[] roleids, User user,RedirectAttributes redirectAttributes){

        /*如果不写密码，就是获取不到password，为空。就不装，*/
        userService.update(user,roleids);
        redirectAttributes.addFlashAttribute("message","修改成功！");
        return "redirect:/user";
    }



}
