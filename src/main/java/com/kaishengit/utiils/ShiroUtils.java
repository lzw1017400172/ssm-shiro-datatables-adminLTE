package com.kaishengit.utiils;

import com.kaishengit.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Created by 刘忠伟 on 2017/1/19.
 * 获取当前登录用户信息的工具类
 */
public class ShiroUtils {


    /**
     * 获取用户对象。每次都要现获取suject用户，才能执行login，logout,获取当前登录对象等操作
     * @return
     */
    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录用户。前提是在登陆认证里面传的第一个参数，是对象user。第一个参数就是上传session,所以能获取的到。传什么获取的就是什么
     * @return
     */
    public static User getUser(){
        return (User)getSubject().getPrincipal();
    }

    /**
     * 获取当前登录对象的username
     * @return
     */
    public static String getUserName(){
        return getUser().getUserName();
    }

    /**
     * 获取当前登录用户的id
     * @return
     */
    public static Integer getUserId(){
        return getUser().getId();
    }

    /**
     * 判断当前用户是否是市场部的
     * @return
     */
    public static boolean isSales(){
        //需要subject对象
        return getSubject().hasRole("role_sales"); //判断subject中是否含有市场部。返货boolean类型
        //这里是在服务端判断。权限认证中返回的当前对象的所有角色中是否含有。suject对象就包含了权限认证中返回的对象authorizationInfo
        //另一种在客户端判断。是否包含指定角色。就是shiro:hasRole节点
    }

}
