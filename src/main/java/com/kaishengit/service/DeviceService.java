package com.kaishengit.service;

import com.kaishengit.pojo.Device;

import java.util.List;
import java.util.Map;

/**
 * Created by 刘忠伟 on 2017/1/17.
 *  逻辑处理层一定要写接口。降低耦合性，便于管理。
 */
public interface DeviceService {

    void save(Device device);

    List<Device> findAll();

    Device findById(Integer id);

    void update(Device device);

    List<Device> findPageDataTables(Map<String,Object> map);

    Long findCount(String deviceName);

    void delete(Integer id);
}
