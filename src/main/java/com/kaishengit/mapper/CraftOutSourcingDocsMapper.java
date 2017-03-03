package com.kaishengit.mapper;

import com.kaishengit.pojo.CraftOutSourcingDocs;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/19.
 */
public interface CraftOutSourcingDocsMapper {
    void saves(List<CraftOutSourcingDocs> docsList);

    List<CraftOutSourcingDocs> findByOutId(Integer id);

    CraftOutSourcingDocs findById(Integer id);
}
