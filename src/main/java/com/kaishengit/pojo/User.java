package com.kaishengit.pojo;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 刘忠伟 on 2017/1/12.
 * 要加入二级缓存，需要是可序列化的类
 */
public class User implements Serializable {

    private Integer id;
    /*已经在mybatis配置中配置了下划线to驼峰，可以使用驼峰命名。或者把sql查出来的列sa命名。查询结果装给对象时，列名下划线对应驼峰*/
    private String userName;
    private String passWord;

    private String mobile;

    /*查询user对应的角色。一对多，返回user。需要包装集合*/
    private List<Role> roleList;

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public User() {
    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\''  +
                '}';
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /*EL调用get。返回所有角色的字符串。每个user对应的所有viewName*/
    public String getViewNames(){

        //使用guawa的过滤集合collication2。只输出viewName。transform返回return结果，filter过滤，return true
        List<String> list = Lists.newArrayList(Collections2.transform(getRoleList(), new Function<Role, String>() {

            @Override
            public String apply(Role role) {

                return role.getViewName();
            }
        }));
        StringBuilder str = new StringBuilder();
        for(String st:list){
            str.append(st).append(" ");//apend添加在末尾。然后再加上格空格
        }

        /*String和Stringbuilder区别String是不可变的只能指向。builder是可变的字符串，可以改变本身，不是指向*/
        return str.toString();//toString将数组编程字符串.返回值带"[]"。重新输出成字符串


    }





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
