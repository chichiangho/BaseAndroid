package com.chichiangho.base.base;

import java.io.Serializable;


public class BaseResponse implements Serializable {
    public static final String CODE_SUCCESS = "1000"; // 返回数据成功
    public static final String CODE_TOKEN_TIMEOUT = "1007"; // 用户Token过期，需要重新登录
    private String code;
    private String msg;
    private String reqUrl;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }
}
