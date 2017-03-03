package com.kaishengit.pojo;

import java.io.Serializable;

/**
 * Created by 刘忠伟 on 2017/2/18.
 */
public class Craft implements Serializable {

    private Integer id;
    private String name;
    private Integer totalNum;//总数
    private Integer currNum;//库存
    private Float price;//单价


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getCurrNum() {
        return currNum;
    }

    public void setCurrNum(Integer currNum) {
        this.currNum = currNum;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
