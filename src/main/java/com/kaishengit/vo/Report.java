package com.kaishengit.vo;


import java.io.Serializable;

/**
 * Created by 刘忠伟 on 2017/2/24.
 * 年报和月报查询结果类似。都用这一个vo类去接受结果。查询数据库，得到需要的值的封装类。并且可以作为json返回到客户端自动转成json
 * 接受查询结果的封装类。使用OGNL技术，给字段别名，就会自动封装到属性
 */
public class Report implements Serializable {

    private String expend;//支出
    private String income;//收入
    private String date;

    public String getExpend() {
        return expend;
    }

    public void setExpend(String expend) {
        this.expend = expend;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
