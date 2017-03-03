package com.kaishengit.dto.wx;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/25.
 * weixin的user，所需要的固定返回值，用于赋值后转化成json，跨域发送给
 * Https请求方式: POST

 https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=ACCESS_TOKEN
 */
public class User {

    /**
     * userid : zhangsan
     * name : 张三
     * department : [1,2]
     * mobile : 15913215421
     */

    private String userid;
    private String name;
    private String mobile;
    private List<Integer> department;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<Integer> getDepartment() {
        return department;
    }

    public void setDepartment(List<Integer> department) {
        this.department = department;
    }
}
