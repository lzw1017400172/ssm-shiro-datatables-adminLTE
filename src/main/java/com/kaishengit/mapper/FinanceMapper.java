package com.kaishengit.mapper;

import com.kaishengit.vo.Report;
import com.kaishengit.pojo.Finance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 刘忠伟 on 2017/2/23.
 */
public interface FinanceMapper {

    void save(Finance finance);

    Integer findCount();

    List<Finance> findPage(@Param("start") Integer start,@Param("length") Integer length);

    Finance findById(Integer id);

    void update(Finance finance);

    List<Finance> findByDate(@Param("date") String date,@Param("start") Integer start,@Param("length") Integer length);

    Integer findByDateCount(@Param("date") String date);
        //这里如果只有一个参数是字符串，就必须使用注解。默认调用get方法，然而字符串无，即报错
    List<Finance> findByDateAll(@Param("date")String date);

    Integer countByMonth(@Param("date") String date);//发送字符串参数一定使用注解，。不然会找不到get方法报错


    List<Report> findByMonth(@Param("start") Integer start, @Param("length") Integer length, @Param("date") String date);


    List<Report> findByMonthAll(@Param("date")String date);

    List<Report> findByYearAll(@Param("date")String date);

    List<Report> findByYear(@Param("date")String date,@Param("start") Integer start, @Param("length") Integer length);

    Integer countByYear(@Param("date")String date);
}
