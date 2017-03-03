
package com.kaishengit.dto;


/**
 * Created by 刘忠伟 on 2017/2/16.
 *  由于每次返回json，所以封装一个对象，返回这个对象，springmvc自动把对象或者map转换成json
 *
 */


public class AjaxResult {
    public static final String ERROR = "error";
    public static final String SUCCESS="success";

    private String status;
    private String message;
    private Object data;


    public AjaxResult(){}


    //还是原理相同，如果是字符串就来这个，其他的才是object

    public AjaxResult(String status,Object data){
        this.status = status;
        this.data = data;
    }


/**
     * 构造方法，如果是传入Object就说明成功，除了字符串
     * @param date
     */

    public AjaxResult(Object date){
        this.data = date;
        this.status = SUCCESS;
    }


/**
     * 传入字符串说明失败，虽然data是object,先后顺序是没有找到单独字符串的才回去object
     * @param message
     */

   public AjaxResult(String message){
        this.message = message;
        this.status = ERROR;
    }



        public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}

