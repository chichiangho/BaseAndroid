package com.chichiangho.base.http;

/**
 * http参数类
 */

public class RequestParam {

    private String appKey;
    private String sign;
    private Long userId;
    private String token;
    private BizParam params;
    private Long timestamp;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BizParam getParams() {
        return params;
    }

    public void setParams(BizParam params) {
        this.params = params;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
