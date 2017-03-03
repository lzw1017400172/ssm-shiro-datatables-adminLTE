package com.kaishengit.mapper;

import com.kaishengit.pojo.DeviceRentDocs;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/17.
 */
public interface DeviceRentDocsMapper {
    void saves(List<DeviceRentDocs> deviceRentDocsList);

    List<DeviceRentDocs> findByRentId(Integer rentId);

    DeviceRentDocs findBytId(Integer id);
}
