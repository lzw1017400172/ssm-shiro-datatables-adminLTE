package com.kaishengit.service.impl;

import com.google.common.collect.Maps;
import com.kaishengit.exception.NotFoundException;
import com.kaishengit.mapper.DeviceMapper;
import com.kaishengit.pojo.Device;
import com.kaishengit.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 刘忠伟 on 2017/1/17.
 */
/*纳入bean管理,不管理接口*/
@Service
public class DeviceServiceImpl implements DeviceService{

    Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);
    /*扫描过DeviceMapper接口之后，就把实现类对象放入spring容器了。只需要自动注入。*/
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public void save(Device device) {
        //一般在添加数据的时候不直接使用对象传值，可以使用另一种传值方式map
        Map<String,Object> map = Maps.newHashMap();
        map.put("name",device.getName());
        map.put("unit",device.getUnit());
        map.put("totalNum",device.getTotalNum());
        map.put("price",device.getPrice());

        deviceMapper.save(map);

    }

    /**
     *
     * @return 返回所有device设备信息
     */
    @Override
    public List<Device> findAll() {
        return deviceMapper.findAll();
    }

    @Override
    public Device findById(Integer id) {
        Device device = deviceMapper.findById(id);
        if(device == null){
            throw new NotFoundException();//使用spring框架自定义的运行时异常，专门针对找不到
        }
        return device;
    }

    /**
     * 修改设备信息
     * @param device
     */
    @Override
    public void update(Device device) {

        //为了保险，首先先查询一下存在不存在
        if(deviceMapper.findById(device.getId()) == null){
            throw new NotFoundException();//找不到，抛异常
        }


        deviceMapper.update(device);

    }

    /**
     *  datatables插件的ajax查询，分页。不需要page对象了
     * @param
     * @param
     * @return
     */
    @Override
    public List<Device> findPageDataTables(Map<String,Object> map) {
        List<Device> deviceList = deviceMapper.findAllPageDaTables(map);
        return deviceList;
    }

    @Override
    public Long findCount(String deviceName) {
        return deviceMapper.count(deviceName);
    }

    @Override
    public void delete(Integer id) {
        deviceMapper.del(id);
    }


}
