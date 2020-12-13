package com.example.demo.service;

public interface IMyWXService {

    /**
     * 获取Token
     * @param appId
     * @param secret
     * @return
     */
    String getToken(String appId,String secret);

    void upload(String token);

    String addMaterialFilePath(String filePath,String type);
}
