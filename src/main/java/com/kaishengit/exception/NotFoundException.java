package com.kaishengit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by 刘忠伟 on 2017/1/12.
 */
/*使用springmvc新特性注解来配置此类为找不到404错误异常*/
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
}
