package com.kaishengit.service;

import com.kaishengit.dto.OutSourcingDto;
import com.kaishengit.pojo.Craft;
import com.kaishengit.pojo.CraftOutSourcing;
import com.kaishengit.pojo.CraftOutSourcingDocs;
import com.kaishengit.utiils.Page;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * Created by 刘忠伟 on 2017/2/18.
 */
public interface OutSourcingService {
    List<Craft> findCraftAll();

    Craft findCraftById(Integer carftId);

    String saveOutSourcingContract(OutSourcingDto outSourcingDto);

    Map<String,Object> findOutSourcing(String serialNumber);


    List<CraftOutSourcing> findBySearch(Integer start, Integer length, String serialNumber, String companyName, String status, String startDate, String stopDate);

    Integer findBySearchCount(String serialNumber, String companyName, String status, String startDate, String stopDate);

    FileInputStream getIntPutStream(Integer id) throws FileNotFoundException;

    CraftOutSourcingDocs findDocsById(Integer id);

    CraftOutSourcing findOutSourcingById(Integer id);

    void zipDownload(Integer id, ZipOutputStream zipOutputStream) throws IOException;

    void updateStatus(Integer id);
}
