package com.kaishengit.utiils;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 刘忠伟 on 2017/2/18.
 *
 * 产生序列号的工具类，时间+四位数随机数
 */
public class SerialNumberUtils {

    public static String getSerialNumber(){

        DateTime dateTime = new DateTime();
        String result = dateTime.toString("YYYYMMddHHmmss");
        result = result + RandomStringUtils.randomNumeric(4);
        //RandomStringUtils.random()产生指定位数的随机数
        return result;
    }


}
