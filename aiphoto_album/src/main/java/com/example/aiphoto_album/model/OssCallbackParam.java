package com.example.aiphoto_album.model;

import io.swagger.annotations.ApiModelProperty;

public class OssCallbackParam {
    @ApiModelProperty("请求的回调地址")
    private String callbackUrl;
    @ApiModelProperty("回调是传入request中的参数")
    private String callbackBody;
    @ApiModelProperty("回调时传入参数的格式，比如表单提交形式")
    private String callbackBodyType;

    public OssCallbackParam(String callbackUrl, String callbackBody, String callbackBodyType) {
        this.callbackUrl = callbackUrl;
        this.callbackBody = callbackBody;
        this.callbackBodyType = callbackBodyType;
    }
    
    public OssCallbackParam() {
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    public String getCallbackBody() {
        return callbackBody;
    }
    public void setCallbackBody(String callbackBody) {
        this.callbackBody = callbackBody;
    }
    public String getCallbackBodyType() {
        return callbackBodyType;
    }
    public void setCallbackBodyType(String callbackBodyType) {
        this.callbackBodyType = callbackBodyType;
    }
}