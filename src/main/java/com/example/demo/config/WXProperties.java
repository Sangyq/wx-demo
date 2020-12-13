package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 微信相关配置类
 * @author Administrator
 */
@Component
@ConfigurationProperties(prefix = "wx")
public class WXProperties {
    private String appId;
    private String sercet;
    private String token;
    private String EncodingAESKey;

    public String getAppId() {
        return appId;
    }

    public String getSercet() {
        return sercet;
    }

    public String getToken() {
        return token;
    }

    public String getEncodingAESKey() {
        return EncodingAESKey;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setSercet(String sercet) {
        this.sercet = sercet;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        EncodingAESKey = encodingAESKey;
    }
}
