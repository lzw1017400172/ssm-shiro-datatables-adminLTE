package com.kaishengit.mapper;

import com.kaishengit.pojo.CraftOutSourcing;
import com.kaishengit.utiils.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/19.
 */
public interface CraftOutSourcingMapper {

    /*这个返回id，会自动封装到对象里面*/
    void save(CraftOutSourcing craftOutSourcing);

    void update(CraftOutSourcing sourcing);

    CraftOutSourcing findBySerialNumber(String serialNumber);

    List<CraftOutSourcing> findAll();

    Integer findCount(@Param("serialNumber") String serialNumber,@Param("companyName") String companyName,
                      @Param("status") String status,@Param("startDate") String startDate,
                      @Param("stopDate") String stopDate);

    List<CraftOutSourcing> findAllPage(@Param("start")int start, @Param("pageSize")int pageSize,
                                       @Param("serialNumber")String serialNumber,@Param("companyName") String companyName,
                                       @Param("status")String status, @Param("startDate")String startDate,
                                       @Param("stopDate")String stopDate);

    CraftOutSourcing   findById(Integer id);
}
