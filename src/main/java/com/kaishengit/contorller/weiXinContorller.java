package com.kaishengit.contorller;

import com.kaishengit.exception.ServiceException;
import com.kaishengit.service.WeiXinService;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by 刘忠伟 on 2017/2/24.
 * 用于处理微信的处理器
 */
@Controller
@RequestMapping("/weixin")
public class weiXinContorller {


    Logger logger = LoggerFactory.getLogger(weiXinContorller.class);
    @Autowired
    private WeiXinService weiXinService;
    /**
     * 微信企业应用 开启回调模式的初始化方。微信向服务器发送请求，验证服务器，才能建立连接开启回调模式
     *  微信发送请求的格式   http://api.3dept.com/?msg_signature=ASDFQWEXZCVAQFASDFASDFSS&timestamp=13500001234&nonce=123412323
     *  共四个参数   msg_signature   timestamp   nonce
     * @return
     */
    @GetMapping("/init")
    @ResponseBody
    public String init(String msg_signature,String timestamp,String nonce,String echostr) {

        return weiXinService.init(msg_signature,timestamp,nonce,echostr);
    }


}
