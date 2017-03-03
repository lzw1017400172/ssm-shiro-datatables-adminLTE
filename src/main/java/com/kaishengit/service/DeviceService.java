package com.kaishengit.service;

import com.kaishengit.dto.DeviceRentDto;
import com.kaishengit.pojo.Device;
import com.kaishengit.pojo.DeviceRent;
import com.kaishengit.pojo.DeviceRentDocs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

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

    String saveRent(DeviceRentDto deviceRentDto);

    Map<String,Object> findBySerialNumber(String serialNumber);

    FileInputStream download(Integer id) throws FileNotFoundException, IOException;

    DeviceRentDocs findDocsById(Integer id);

    DeviceRent findRentById(Integer id);

    void zipDownload(DeviceRent rent, ZipOutputStream zipOutputStream) throws IOException;

    List<DeviceRent> findDeviceRent(Integer start, Integer length);

    Integer findDeviceRentCount();

      void finish(Integer id);
}
