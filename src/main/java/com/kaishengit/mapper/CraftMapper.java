package com.kaishengit.mapper;

import com.kaishengit.pojo.Craft;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/18.
 */
public interface CraftMapper {

    List<Craft> findAll();

    Craft findById(Integer id);

    void update(Craft craft);

    Craft findByName(String name);
}
