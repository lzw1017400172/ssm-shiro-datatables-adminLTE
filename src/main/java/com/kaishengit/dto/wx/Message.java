package com.kaishengit.dto.wx;

import java.io.Serializable;

/**
 * Created by 刘忠伟 on 2017/2/26.
 * 微信发消息，向url跨域需要传的值的json，转成对象，赋值之后再转成json，发送。收到响应值是json，再转成map就可以了。判断是否发送成功
 */
public class Message implements Serializable {

    /**
     * toparty :  PartyID1 | PartyID2
     * msgtype : text
     * agentid : 1
     * text : {"content":"Holiday Request For Pony(http://xxxxx)"}
     * safe : 0
     */

    private String toparty;
    private String msgtype;
    private int agentid;
    private TextBean text;
    private int safe;

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public int getAgentid() {
        return agentid;
    }

    public void setAgentid(int agentid) {
        this.agentid = agentid;
    }

    public TextBean getText() {
        return text;
    }

    public void setText(TextBean text) {
        this.text = text;
    }

    public int getSafe() {
        return safe;
    }

    public void setSafe(int safe) {
        this.safe = safe;
    }

    public static class TextBean {
        /**
         * content : Holiday Request For Pony(http://xxxxx)
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
