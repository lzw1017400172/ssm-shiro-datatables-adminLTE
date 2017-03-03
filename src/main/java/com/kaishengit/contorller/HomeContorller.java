package com.kaishengit.contorller;

import com.kaishengit.utiils.ShiroUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Decoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created by 刘忠伟 on 2017/1/19.
 * 扫描注解，控制器
 */
@Controller
public class HomeContorller {

    @GetMapping("/")
    public String login(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        String userName = null;
        String passWord = null;


        //获取cookie
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            //获取帐号
            if("userName2".equals(cookie.getName())){
                userName = cookie.getValue();

            }
            if("passWord2".equals(cookie.getName())){
                passWord=cookie.getValue();

            }
        }
        if(StringUtils.isNotEmpty(userName)&&StringUtils.isNotEmpty(passWord)){

            //进行base64解密
            try {
                userName = new String(Base64.decodeBase64(userName.getBytes("utf-8")), "utf-8");
                passWord = new String(Base64.decodeBase64(passWord.getBytes("utf-8")), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            System.out.println(userName+","+passWord);
            //shiro登录认证。张阿红或者密码不正确，返回null，这里抛出异常
            try {
                ShiroUtils.getSubject().login(new UsernamePasswordToken(userName, passWord));
                return "shiro/home";
            }catch (AuthenticationException ex){//cookie验证的帐号密码不正确，就跳转到登录页面
                ex.printStackTrace();
                model.addAttribute("message","帐号或者密码错误！");
                return "shiro/login";
            }
        } else {
            return "shiro/login";
        }

        //return "shiro/login";
    }
    @PostMapping("/")
    public String login(String userName, String passWord, RedirectAttributes redirectAttributes){

        //shiro方式登录
        //需要登录认证，就需要重写shiroDbRelm里面的登录验证
        //原理是这里调用登录认证的方法。会把两个参数传过去，如果出现异常，捕获到。说明登录验证失败。没有异常就认证成功
        //登录认证的原理return null表示这里就会抛出异常。

        //创建subject用户对象。
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(new UsernamePasswordToken(userName, passWord));//此方法及将两个值当参数封装给一个对象token传给登录认证的方法。那边程序设计。验证是否ruturn null就会抛出异常
            //这一步相当于调用ShiroDbRelm的登录认证方法。哪个方法验证后返回null，代表这里抛出异常

            return "redirect:/home";

        }catch (AuthenticationException ex){
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","帐号或者密码错误！");
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response){
        //shiro安全退出和登录类似。
        //1获取用户subject
        Subject subject = SecurityUtils.getSubject();
        //2logout
        subject.logout();

        //3安全退出不再自动登录，把cookie删除

        //获取cookie
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie:cookies){
            //获取帐号
            if("userName2".equals(cookie.getName())){
               //找到键为userName的cookie并且删除
                cookie.setMaxAge(0);
                response.addCookie(cookie);

            }
            if("passWord2".equals(cookie.getName())){
                //找到键为password的cookie并且删除
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }


        redirectAttributes.addFlashAttribute("message","你已经安全退出");

        return "redirect:/";
    }


    @GetMapping("/home")
    public String home(){
        return "shiro/home";
    }
    @GetMapping("/403")
    public String noAccess(){
        return "shiro/403";
    }
}
