package com.kaishengit.pojo;

import java.io.Serializable;

/**
 * Created by 刘忠伟 on 2017/2/17.
 */
public class DeviceRentDocs implements Serializable {

    private Integer id;
    private String sourceName;
    private String newName;
    private Integer rentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public Integer getRentId() {
        return rentId;
    }

    public void setRentId(Integer rentId) {
        this.rentId = rentId;
    }
}
