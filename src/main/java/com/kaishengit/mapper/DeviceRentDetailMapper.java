package com.kaishengit.mapper;

import com.kaishengit.pojo.DeviceRentDetail;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/17.
 */
public interface DeviceRentDetailMapper {
    void saves(List<DeviceRentDetail> deviceRentDetailList);

    List<DeviceRentDetail> findByRentId(Integer rentId);
}
