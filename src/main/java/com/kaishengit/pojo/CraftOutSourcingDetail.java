package com.kaishengit.pojo;

import java.io.Serializable;

/**
 * Created by 刘忠伟 on 2017/2/19.
 */
public class CraftOutSourcingDetail implements Serializable {

    private Integer id;
    private String craftName;
    private Float craftPrice;
    private Integer craftNum;
    private Float subTotal;
    private Integer outId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCraftName() {
        return craftName;
    }

    public void setCraftName(String craftName) {
        this.craftName = craftName;
    }

    public Float getCraftPrice() {
        return craftPrice;
    }

    public void setCraftPrice(Float craftPrice) {
        this.craftPrice = craftPrice;
    }

    public Integer getCraftNum() {
        return craftNum;
    }

    public void setCraftNum(Integer craftNum) {
        this.craftNum = craftNum;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getOutId() {
        return outId;
    }

    public void setOutId(Integer outId) {
        this.outId = outId;
    }
}
