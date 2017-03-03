package com.kaishengit.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by 刘忠伟 on 2017/2/23.
 */
public class Finance implements Serializable {


    public static final String UNCONFIRMED = "未确认";
    public static final String ACKNOWLEDGED = "已确认";

    private Integer id;
    private String serialNumber;
    private String type;//类型，分为：收入或者支出
    private Float money;
    private String state;//状态，默认为未确认    当设备租赁或者劳务派遣等时插入一条记录默认为确认，收款之后确认
    private String module;  //属于哪个模块的财务
    private String createDate;//创建日期
    private String  createUser;//创建的人。是哪个人插入的
    private String confirmUser;//确认的人。默认未确认，现在确认的人
    private String confirmDate;//确认的日期
    private String remark;//备注，比如什么的尾款。什么的预付款
    private String moduleSerialNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getConfirmUser() {
        return confirmUser;
    }

    public void setConfirmUser(String confirmUser) {
        this.confirmUser = confirmUser;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getModuleSerialNumber() {
        return moduleSerialNumber;
    }

    public void setModuleSerialNumber(String moduleSerialNumber) {
        this.moduleSerialNumber = moduleSerialNumber;
    }
}
