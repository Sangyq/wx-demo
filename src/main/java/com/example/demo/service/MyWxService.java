package com.example.demo.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.example.demo.config.WXConfig;
import com.example.demo.config.WXProperties;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Service
public class MyWxService implements IMyWXService {

    @Resource
    private WXProperties wxProperties;


    @PostConstruct
    public void init(){
        String token = getToken(wxProperties.getAppId(),wxProperties.getSercet());
        System.out.println("TOKEN:"+token);
    }

    @Override
    public String getToken(String appId, String secret) {
        String token = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx4b8c7e4a1d56236c&secret=51a908d833906a0e32d27ddbb9f36f02");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            org.apache.http.HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                token = EntityUtils.toString(responseEntity);
                token = (String)JSONUtil.toBean(token,Map.class).get("access_token");
            }
            System.out.println("响应状态为:" + response.getStatusLine());
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    @Override
    public void upload(String token) {

    }

    @Override
    public String addMaterialFilePath(String filePath, String type) {
        RestTemplate restTemplate = new RestTemplate();
        String accessToken = this.getToken(wxProperties.getAppId(),wxProperties.getSercet());
        if (accessToken != null) {
            String url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token="+accessToken+"&type="+type;
            //设置请求体，注意是LinkedMultiValueMap
            MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();

            //设置上传文件
            FileSystemResource fileSystemResource = new FileSystemResource(filePath);
            data.add("media", fileSystemResource);

            //上传文件,设置请求头
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            httpHeaders.setContentLength(fileSystemResource.getFile().length());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data,
                    httpHeaders);
            try{
                //这里RestTemplate请求返回的字符串直接转换成JSONObject会报异常,后续深入找一下原因
                String resultString = restTemplate.postForObject(url, requestEntity, String.class);
                System.out.println(resultString);
                return resultString;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
