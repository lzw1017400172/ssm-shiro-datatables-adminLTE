package com.kaishengit.service;

import com.kaishengit.vo.Report;
import com.kaishengit.pojo.Finance;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/23.
 */
public interface FinanceService {
    Integer Count();

    List<Finance> findPage(Integer start, Integer length);

    void finish(Integer id);

    List<Finance> findByDate(String date,Integer start,Integer length);

    Integer findByDateCount(String date);



    /*void getExcelCsv(String date, HttpServletResponse response);*/

    void getExcelXlsx(String date, HttpServletResponse response) ;

    Integer countByMonth(String date);

    List<Report> findByMonth(Integer start, Integer length, String date);

    void getMonthReportExcel(String date, HttpServletResponse response);

    List<Report> findByYearReport(String date,Integer start, Integer length);

    Integer countByYear(String date);

    void getYearEcxel(String date, HttpServletResponse response);
}
