package com.example.demo.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Configuration
public class WXConfig {

    @Resource
    WXProperties wxProperties;


    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        // 设置微信公众号的appid
        config.setAppId(wxProperties.getAppId());
        // 设置微信公众号的app corpSecret
        config.setSecret(wxProperties.getSercet());
        // 设置微信公众号的token
        config.setToken(wxProperties.getToken());
        // 设置微信公众号的EncodingAESKey
        config.setAesKey(wxProperties.getEncodingAESKey());
        return config;
    }

    @Bean
    public WxMpService wxMpService(WxMpConfigStorage wxMpConfigStorage){
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxService;
    }
}
