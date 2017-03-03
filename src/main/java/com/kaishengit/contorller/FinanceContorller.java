package com.kaishengit.contorller;

import com.kaishengit.dto.AjaxResult;
import com.kaishengit.dto.DataTableResult;
import com.kaishengit.vo.Report;
import com.kaishengit.exception.NotFoundException;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.pojo.Finance;
import com.kaishengit.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/23.
 */
@Controller
@RequestMapping("/finance")
public class FinanceContorller {
    @Autowired
    private FinanceService financeService;

    /**
     * 跳转到财务流水详情。不传送数据，使用datatables，跳转之后发送ajax
     * @return
     */
    @GetMapping("/list")
    public String financeList(){
        return "finance/list";
    }

    /**
     * datatales发送的ajax
     * 分页查询全部财务流水数据
     * @return
     */
    @PostMapping("/list/datatables.json")
    @ResponseBody
    public DataTableResult financeList(Integer draw,Integer start,Integer length){
        //分页查询全部
        Integer total = financeService.Count();

        //分页查询的集合
        List<Finance> financeList = financeService.findPage(start,length);

        //以datatables固定的格式返回json。返回一个对象，或者map集合都会自动转换json


        return new DataTableResult(draw,total,total,financeList);
    }

    /**
     * 值跳转到日报，日报发送ajax使用datatables
     *
     * 另一个作用，当点击月报详情时跳转到日报。并且把点击的月报的日期传过来。因为日报前端使用datatables，所以把日期发过去，用日期发送ajax
     *
     * @return
     */
    @GetMapping("/darReport")
    public String darReport(@RequestParam(required = false) String date, Model model){
        model.addAttribute("date",date);//若果时月报点击详情过来的，就会有这个值，传过去。不是就没有传过去null
        System.out.println("============"+date);
        return "finance/darReport";
    }

    /**
     * 根据id，修改流水的状态为已确认
     * @param id
     * @return
     */
    @GetMapping("/finish/dataTable.json")
    @ResponseBody
    public AjaxResult finish(Integer id){

        try {
            financeService.finish(id);
            return new AjaxResult(id);
        } catch (NotFoundException ex){
            return new AjaxResult("没有找到此财务流水");
        }
    }

    /**
     * 客户端的datatable发送ajax请求，根据搜索内容日期查询
     * @return datatables特定返回值json
     */

    @GetMapping("/dayReport/data.json")
    @ResponseBody
    public DataTableResult  dayReport(Integer draw,Integer start,Integer length,String date){

        Integer total = financeService.findByDateCount(date);
        List<Finance> financeList = financeService.findByDate(date,start,length);

        return  new DataTableResult(draw,total,total,financeList);
    }


    /**
     * 导出.csv后缀的excel轻量级，只要按照规定格式输出字符串  就是文件下载
     * @param date
     */
    /*@GetMapping("/dayReport/{date}/data.csv")
    public void getExcel(@PathVariable String date, HttpServletResponse response){


        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        response.setHeader("Content-Disposition", "attachment;filename=\"" + date + ".csv\"");

        financeService.getExcelCsv(date,response);


    }
*/

    /**
     * 导出.xlsx后缀的excel文件。需要使用apache的poi项目。
     * 需要创建表workbook,页签sheet,row行，cell列（即单元格）
     * 导出，就是文件下载。
     * @param date
     */
    @GetMapping("/dayReport/{date}/data.xlsx")
    public void getDayReportExcel(@PathVariable String date,HttpServletResponse response){

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment;filename=\"" + date + ".xlsx\"");

        try {
            financeService.getExcelXlsx(date,response);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }


    /**
     * 跳转到月报。还是datatables的ajax，
     * 当点击日报详情时，需要跳转到月报。
     * 所以会传个参数，月，但不是必须的。点击标题进来就不用传值。
     * 在把月份传到jsp，作为datatable的ajax的参数。就可以获取到年报详情
     * @return
     */

    @GetMapping("/monthReport")
    public String getMonthReport(@RequestParam(required = false) String date,Model model){
        model.addAttribute("date",date);
        return "finance/monthReport";
    }


    /**
     * 月报的datatables发送的ajax请求
     * @param date 日期，搜索关键字
     * @return  datattables的规定值
     */
    @GetMapping("/monthReport/data.json")
    @ResponseBody
    public DataTableResult getMonthReport(Integer draw,Integer start,Integer length,String date){
        Integer total = financeService.countByMonth(date);
        List<Report> reportList = financeService.findByMonth(start,length,date);

        return new DataTableResult(draw,total,total, reportList);
    }


    //导出月报excel，以.xlsx后缀的。

    //导出年报的excel。以.xlsx后缀的。文件下载，把内容写入响应输出流就是下载。名字后缀设置成.xlsx。使用poi框架导出excel

    /**
     * 导出月报excel，文件下载。拷贝到输出流
     * @param date
     */
    @GetMapping("/monthReport/{date}/data.xlsx")
    public void getMonthReportExcel(@PathVariable String date,HttpServletResponse response){

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String name = date + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
        try {
            financeService.getMonthReportExcel(date, response);
        } catch (ServiceException ex){
            ex.printStackTrace();
        }
    }

    /**
     * 请求年报页面。还是datatbles，跳转后才发送ajax的
     * @return
     */
    @GetMapping("/yearReport")
    public String yearReport(){
        return "finance/yearReport";
    }

    /**
     * 年报表，datatables
     * @param date
     * @return
     */
    @GetMapping("/yearReport/data.json")
    @ResponseBody
    public DataTableResult yearReport(String date,Integer draw,Integer start,Integer length){

        Integer total = financeService.countByYear(date);

        List<Report> reportList = financeService.findByYearReport(date,start,length);

        return  new DataTableResult(draw,total,total,reportList);
    }

    /**
     * 导出年报的excle，是下载
     * @param date
     */
    @GetMapping("/yearReport/{date}/excel")
    public void getYearExcel(@PathVariable String date,HttpServletResponse response){
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment;filename=\"" + date + ".xlsx\"");

        financeService.getYearEcxel(date,response);
    }

}
