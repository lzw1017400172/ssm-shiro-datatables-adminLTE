package com.kaishengit.mapper;

import com.kaishengit.pojo.CraftOutSourcingDetail;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/19.
 */
public interface CraftOutSourcingDetailMapper {

    void saves(List<CraftOutSourcingDetail> detailList);

    List<CraftOutSourcingDetail> findByOutId(Integer id);
}
