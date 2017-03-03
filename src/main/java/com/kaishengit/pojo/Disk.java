package com.kaishengit.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by 刘忠伟 on 2017/2/21.
 *
 *  网盘的设计：  新名字用于下载查找文件。    旧名字用于在html显示。   type:用于表示此资源的类型是文件还是文件夹
 *  fid: 用于表示所属关系。表示此资源（可能文件或者文件夹）所属资源（文件夹，文件不能够）的id。这样就能够表示很多关系。包括多级文件夹也能做到
 *  当fid=0时表示，此资源在根下，没有所属。有所属fid就表示是文件夹的id
 */
public class Disk implements Serializable{


    public static final String FILE = "file";//文件
    public static final String FOLDER = "folder";//文件夹


    private Integer id;
    private String newName;
    private String sourceName;
    private Integer fid;    //资源的对应关系，a所属b，fid就是b资源的id。这样设计，也可以表示多级文件夹
    private String size;
    private Timestamp createTime;
    private String createUser;
    private String type;//类型是文件还是文件夹


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
