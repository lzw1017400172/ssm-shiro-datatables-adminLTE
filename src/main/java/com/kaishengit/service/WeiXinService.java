package com.kaishengit.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.kaishengit.dto.wx.Message;
import com.kaishengit.dto.wx.User;
import com.kaishengit.exception.ServiceException;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by 刘忠伟 on 2017/2/24.
 */
@Service
public class WeiXinService {

    Logger logger = LoggerFactory.getLogger(WeiXinService.class);

    @Value("${weixin.Secret}")
    private String  srcret;
    @Value("${weixin.Token}")
    private String token;
    @Value("${weixin.EncodingAESKey}")
    private String aesKey;
    @Value("${weixin.CorpID}")
    private String corpid;

    //发出get跨域的网址，带参数，为了获取token凭证。｛0｝，｛1｝是占位符，不用字符串加了
    public static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={0}&corpsecret={1}";

    //微信创建人员的接口url是post请求的跨域
    public static final String CREATE_USER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token={0}";

    //接入微信接口，给人员发消息.post跨域
    private static final String SEND_MESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={0}";



    /*主动调用，可以向用户发消息。但是要主动调用微信的接口。主动调用微信接口，需要有凭证access_token*/
    /*access_token凭证的获取，需要跨域访问get的url https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=id&corpsecret=secrect
    gcorpid，srcret作为参数。获取跨域微信返回的值（json键值形式），解析出access_token凭证*/

    //由于 access_token凭证，是有有效期的，7200秒。过了有效期，就需要再获取一次。所以需要使用缓存，重写的方法中获取token。
    //这样根据缓存的机制，超过7200秒后，缓存中没有了，就执行重写的方法获取新token，存入缓存

    //创建缓存。使用common的缓存
    private LoadingCache<String,String> cache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(7200, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    String url = MessageFormat.format(ACCESS_TOKEN_URL,corpid,srcret);

                    OkHttpClient httpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = httpClient.newCall(request).execute();
                    String result = response.body().string();
                    response.close();


                    Map<String,Object> map = new Gson().fromJson(result, HashMap.class);
                    if(map.containsKey("errcode")) {
                        logger.error("获取微信AccessToken异常:{}",map.get("errcode"));
                        throw new ServiceException("获取AccessToken异常:" + map.get("errcode"));
                    } else {
                        return map.get("access_token").toString();
                    }
                }
            });
   /* private LoadingCache<String,String> cache = CacheBuilder.newBuilder()
            .maximumSize(10).expireAfterWrite(7200, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override//此重写的方法，只在缓存中获取不到时调用。并且返回值会自动存入缓存。不需要键。只用''空字符串获取即可
                public String load(String s) throws Exception {
                    String url = MessageFormat.format(ACCESS_TOKEN_URL,corpid,srcret);//替换占位符
                    //获取token，向网址发送get跨域。跨域使用OKHttp
                    OkHttpClient client = new OkHttpClient();
                    //创建一个请求
                    Request request = new Request.Builder().url(url).build();
                    //发送请求，返回一个响应
                    Response response = client.newCall(request).execute();
                    //获取接受的响应中的值。api说了正确的json结果。键值，所以转化
                    String result = response.body().string();
                    response.close();
                    //转换result
                    Map<String,Object> map = new Gson().fromJson(result, HashMap.class);//json格式转换成map
                    //api规定了正确和错误的json。所以判断
                    if(map.containsKey("errcode")){
                        //错误的
                        logger.error("获取access_token异常:{}",map.containsKey("errcode"));
                        throw new ServiceException("获取access_token异常:{}"+map.containsKey("errcode"));
                    } else {
                        logger.debug("token-----{}",map.get("access_token").toString());
                        return map.get("access_token").toString();

                    }

                }
            });*/


    /**
     * 获取当前的token，去缓存中找即可。没有给键，直接   ''获取
     * @return
     */
    public String getToken(){
        try {
            return cache.get("");
        } catch (ExecutionException e) {
            throw new ServiceException("从缓存中获取token异常");
        }
    }


    /**
     * 微信企业号初始化方法
     * @return
     */
    public String init(String msg_signature,String timestamp,String nonce,String echostr) {
        try {
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(token,aesKey,corpid);
            return crypt.VerifyURL(msg_signature,timestamp,nonce,echostr);
        } catch (AesException e) {
            throw new ServiceException("微信初始化异常",e);
        }
    }


    //微信创建用户。需要在服务端能获取信息，然后跨域post请求接口url,需要传值token，
    // 以及微信接口需要的值，微信接口需要规定的json格式，所以生成一个封装类，装完之后，再转成json，跨域时发送requestBody。微信就可以生成用户
    //和获取token时跨域类似，响应的结果中判断是否成功

    /**
     * 创建一个user时，需要更新到微信显示。所以给微信接口发送指定json参数。
     * 把指定参数转成对象，赋值之后再转json，跨域发出
     * @param user weixin里面的user,就是固定json，把服务器user，的值赋给weixin
     */
    public void wxCeateUser(User user){
        String url = MessageFormat.format(CREATE_USER_URL,getToken());

        String json = new Gson().toJson(user);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),json);
        Request request = new Request.Builder().post(requestBody).url(url).build();
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            String resultJson = response.body().string();
            response.close();

            Map<String,Object> result = new Gson().fromJson(resultJson,HashMap.class);
            Object errorCode = result.get("errcode");
            logger.debug(errorCode.toString());
            if(!"0.0".equals(errorCode.toString())) {
                logger.error("微信创建用户异常:{}",resultJson);
                throw new ServiceException("微信创建用户异常:"+resultJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        /*String url = MessageFormat.format(CREATE_USER_URL,getToken());
        //将user转换成json，使用跨域发送过去，获取响应内容。判断是否成功。使用OKHttpClien

        //转成json
        String json = new Gson().toJson(user);
        //跨域发送json。通过请求发送
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),json);
        //创建一个请求
        Request request = new Request.Builder().post(requestBody).url(url).build();

        //将获取一个响应
        try {
            Response response = new OkHttpClient().newCall(request).execute();//客户端发送请求
            //解析相应中的内容。是json。需要再转成对象。是键值对类型，所以转成map集合即可
            String result = response.body().string();
            response.close();
            Map<String,Object> map = new Gson().fromJson(result,HashMap.class);
            //判断返回的值是否是 0.0是表示错误
            if("0.0".equals(map.get("errcode").toString())){
                logger.error("微信添加新成员异常：{}",request);
                throw new ServiceException("微信添加新成员错误："+result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }


    /**
     * 使用微信接口，给微信成员发消息。又是跨域.
     * toparty  部门id 用 | 隔开
     */
    public void wxSendMessage(Message message){
        String url = MessageFormat.format(SEND_MESSAGE_URL,getToken());

        //需要跨域发送的Message对象。转成json发送
        String json = new Gson().toJson(message);

        //使用OKHttpClient跨域
        //1创建客户端
        OkHttpClient client = new OkHttpClient();
        //创建请求的内蓉，是特定的json
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),json);
        //2创建一个请求
        Request request = new Request.Builder().post(body).url(url).build();
        //客户端发送请求，并且结接受响应
        try {
            Response response = client.newCall(request).execute();
            //解析响应中的值是json，
            String result = response.body().string();
            response.close();
            //把json转化成map
            Map<String,Object> map = new Gson().fromJson(result,HashMap.class);
            if(!"0.0".equals(map.get("errcode").toString())){
                //出错了
                logger.error("向微信人员发送消息失败:{}",result
                );
                throw new ServiceException("向微信人员发送消息失败:{}"+result);
            }

        } catch (IOException e) {
            throw  new ServiceException("向微信接口发送跨域请求失败");
        }


    }


}
