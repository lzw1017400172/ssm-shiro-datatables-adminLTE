package com.kaishengit.mapper;

import com.kaishengit.pojo.DeviceRent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/17.
 */
public interface DeviceRentMapper {
    //这个insert自带返回值，不需要返回，会自动封装到rent对象
    void save(DeviceRent rent);//传过去只有一个参数对象，不用注解，直接写属性，即可。并且属性名，要和列名相同才能自动封装

    /*不是一个参数，使用注解*/
    void updateCost(@Param("totalCost") float totalCost, @Param("preCost") double preCost,
                    @Param("lastCost") double lastCost, @Param("id") Integer id);

    DeviceRent findBySerialNumber(String serialNumber);

    DeviceRent findById(Integer id);

    List<DeviceRent> findAll(@Param("start") Integer start, @Param("length")Integer length);

    Integer findCount();

    void update(DeviceRent deviceRent);
}
