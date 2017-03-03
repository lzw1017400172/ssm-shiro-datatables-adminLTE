package com.kaishengit.service.impl;


import com.kaishengit.vo.Report;
import com.kaishengit.exception.NotFoundException;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.mapper.FinanceMapper;
import com.kaishengit.pojo.Finance;
import com.kaishengit.service.FinanceService;
import com.kaishengit.utiils.ShiroUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/23.
 */
@Service
public class FinanceServiceimpl implements FinanceService {


    @Autowired
    private FinanceMapper financeMapper;


    /**
     * 查询财务流水的全部个数count
     * @return
     */
    @Override
    public Integer Count() {
        return financeMapper.findCount();
    }

    /**
     * 根据start,length分页查询
     * @param start
     * @param length
     * @return
     */
    @Override
    public List<Finance> findPage(Integer start, Integer length) {
        return financeMapper.findPage(start,length);
    }

    /**
     * 根据id。财务流水的状态修改为   确定
     * @param id
     */
    @Override
    public void finish(Integer id) {
        Finance finance = financeMapper.findById(id);
        if(finance != null){
            finance.setState(Finance.ACKNOWLEDGED);
            //修改为已确认之后，会出现确认人，和确认时间
            finance.setConfirmDate(DateTime.now().toString("YYYY-MM-dd HH:mm:ss"));
            finance.setConfirmUser(ShiroUtils.getUserName());
            financeMapper.update(finance);
        } else {
            throw new NotFoundException();
        }
    }

    /**
     * 根据日期，查询财务流水。日报
     * @param date 日期
     * @return
     */
    @Override
    public List<Finance> findByDate(String date,Integer start,Integer length) {
        List<Finance> financeList = financeMapper.findByDate(date,start,length);
        return financeList;
    }

    /**
     * 根据日期查询个数
     * @param date
     * @return
     */
    @Override
    public Integer findByDateCount(String date) {
        return financeMapper.findByDateCount(date);
    }

    /**
     * 根据日期的格式，查询全部的。年 月 日
     * @param date
     * @return
     */
/*    @Override
    public List<Finance> findByDateAll(String date) {
        if(date.matches("\\d{4}-\\d{2}-\\d{2}")){
            //日报
            return
        } else if(date.matches("\\d{4}-\\d{2}")){

        } else if(date.matches("\\d{4}")){
            //年报
            return financeMapper.findByYearAll(date);
        }
        return null;

    }*/





    /**
     * 输出.vsc后缀的excel表格。按照规定格式输出字符串即可
     * @param date
     * @param response
     */
 /*   @Override
    public void getExcelCsv(String date, HttpServletResponse response) {

        //先查出来当日所有的流水
        List<Finance> financeList =findByDateAll(date);
        //输出文件，向输出流中写入字符串。使用转换流OutPutStreamWrite。写入
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(response.getOutputStream());
            //使用.write方法向转换流输入字符
            outputStreamWriter.write("流水号,创建时间,类型,金钱,业务模块,业务流水,状态,备注,创建人,确定人,确定时间\r\n");
            for(Finance finance:financeList){
                outputStreamWriter.write(finance.getSerialNumber()+","+finance.getCreateDate()+","+finance.getType()
                +","+finance.getMoney()+","+finance.getModule()+","+finance.getModuleSerialNumber()+","+finance.getState()
                        +","+finance.getRemark()+","+finance.getCreateUser()+","+finance.getConfirmUser()+","+finance.getConfirmDate()
                        +"\r\n");

            }
            outputStreamWriter.flush();
            outputStreamWriter.close();
            //只要写入输出流即可

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    /**
     * 日报的导出excel,.xlsx后缀的。使用apache poi。   直接将内容写入输出流
     * @param date
     * @param response
     */
    @Override
    public void getExcelXlsx(String date, HttpServletResponse response)  {


        //获取全部的数据
        List<Finance> financeList = financeMapper.findByDateAll(date);

        //创建workbook
        //1workbook
        Workbook workbook = new HSSFWorkbook();
        //2创建页签。设置页签的名字
        Sheet sheet = workbook.createSheet(date+"的财务流水报表");

        //3创建行和列即单元格。都是从0开始的，为第一行和第一列
        Row row = sheet.createRow(0);
        //单元格
        Cell cell = row.createCell(0);
        cell.setCellValue("业务流水号");//设置单元格的内容
        row.createCell(1).setCellValue("创建日期");
        row.createCell(2).setCellValue("类型");
        row.createCell(3).setCellValue("金额");
        row.createCell(4).setCellValue("业务模块");
        row.createCell(5).setCellValue("业务流水号");
        row.createCell(6).setCellValue("状态");
        row.createCell(7).setCellValue("备注");
        row.createCell(8).setCellValue("创建人");
        row.createCell(9).setCellValue("确认人");
        row.createCell(10).setCellValue("确认日期");

        //使用循环创建其他行

        for(int i = 0;i<financeList.size();i++){
            Row rows = sheet.createRow(i+1);
            rows.createCell(0).setCellValue(financeList.get(i).getSerialNumber());
            rows.createCell(1).setCellValue(financeList.get(i).getCreateDate());
            rows.createCell(2).setCellValue(financeList.get(i).getType());
            rows.createCell(3).setCellValue(financeList.get(i).getMoney());
            rows.createCell(4).setCellValue(financeList.get(i).getModule());
            rows.createCell(5).setCellValue(financeList.get(i).getModuleSerialNumber());
            rows.createCell(6).setCellValue(financeList.get(i).getState());
            rows.createCell(7).setCellValue(financeList.get(i).getRemark());
            rows.createCell(8).setCellValue(financeList.get(i).getCreateUser());
            rows.createCell(9).setCellValue(financeList.get(i).getConfirmUser());
            rows.createCell(10).setCellValue(financeList.get(i).getConfirmDate());

        }
        //对sheet页签的列进行设置。宽度自动
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(10);


        //只需要将workbook放入输出流

        try {
            OutputStream outputStream = response.getOutputStream();

        //将work写入输出流
        workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new ServiceException(e);
        }

    }

    /**
     * 根据月，返回所有数据的数量
     * @param date
     * @return
     */
    @Override
    public Integer countByMonth(String date) {
        return financeMapper.countByMonth(date);
    }

    /**
     * 根据月份分页
     * @param start
     * @param length
     * @param date
     * @return
     */
    @Override
    public List<Report> findByMonth(Integer start, Integer length, String date) {
        return financeMapper.findByMonth(start,length,date);
    }

    /**
     * 月报导出excle,。xlsx后缀的。使用poi
     * @param date
     * @param response
     */
    @Override
    public void getMonthReportExcel(String date, HttpServletResponse response) {
        //根据date查询该月的全部。收入支出
       List<Report> reportList = financeMapper.findByMonthAll(date);
        //使用poi插件解决
        //创建一个workbook
        Workbook workbook = new HSSFWorkbook();
        //创建一个sheet标签页，可创建多个
        Sheet sheet = workbook.createSheet(date+"财务报表");
        //创建行列（单元格），从0开始为第一行
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("日期");
        row.createCell(1).setCellValue("当日收入");
        row.createCell(2).setCellValue("当日支出");
        row.createCell(3).setCellValue("备注");

        for(int i = 0; i< reportList.size(); i++){
            //多少数据，多少行
            Row rows = sheet.createRow(i+1);
            rows.createCell(0).setCellValue(reportList.get(i).getDate());
            rows.createCell(1).setCellValue(reportList.get(i).getIncome());
            rows.createCell(2).setCellValue(reportList.get(i).getExpend());
            rows.createCell(3).setCellValue("无");

        }

        //另外可以设置sheet标签页样式，宽度自动
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);

        try {
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);//将workbook写入输出流
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new ServiceException(date+"财务报表导出失败！",e);
        }

    }

    /**
     * 年报报表。传过去的是年份，返回的是月份以及其他
     * @param date
     * @return
     */
    @Override
    public List<Report> findByYearReport(String date, Integer start, Integer length) {
        return financeMapper.findByYear(date,start,length);
    }

    /**
     * 年报分页需要查总数，
     * @param date
     * @return
     */
    @Override
    public Integer countByYear(String date) {
        return financeMapper.countByYear(date);
    }

    /**
     * 年报导出excel，使用poi包，文件下载，年报查询出的是所有月
     * @param date
     * @param response
     */
    @Override
    public void getYearEcxel(String date, HttpServletResponse response) {
        List<Report> reportList = financeMapper.findByYearAll(date);

        //创建workbook
        Workbook workbook = new HSSFWorkbook();
        //创建一个sheet标签页
        Sheet sheet = workbook.createSheet(date+"年报表");
        //创建行列（单元格），并切赋值单元格
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("月份");
        row.createCell(1).setCellValue("当月收入");
        row.createCell(2).setCellValue("当月支出");
        row.createCell(3).setCellValue("备注");
        for(int i = 0;i<reportList.size();i++){
            Row rows = sheet.createRow(i+1);
            rows.createCell(0).setCellValue(reportList.get(i).getDate());
            rows.createCell(1).setCellValue(reportList.get(i).getIncome());
            rows.createCell(2).setCellValue(reportList.get(i).getExpend());
            rows.createCell(3).setCellValue("无");
        }

        try {
            OutputStream outputStream =response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new ServiceException(date+"年报表导出失败",e);
        }

    }


}
