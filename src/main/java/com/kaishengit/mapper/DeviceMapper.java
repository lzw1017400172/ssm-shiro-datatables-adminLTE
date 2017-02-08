package com.kaishengit.mapper;


import com.kaishengit.pojo.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 刘忠伟 on 2017/1/17.
 * device表对应的接口。mybatis框架
 */
public interface DeviceMapper {

    void save(Map<String,Object> map);//添加的时候传一个对象，不符合厂里，所以用另一种传参数的方式，map集合
    void del(Integer id);
    void update(Device device);
    List<Device> findAll();
    Device findById(Integer id);

                                            //传两个参数以上，需要注解，或者paramn。根据注解里面的值获取
    List<Device> findAllPageDaTables(Map<String,Object> map);

    Long count(@Param("deviceName") String deviceName);
}
