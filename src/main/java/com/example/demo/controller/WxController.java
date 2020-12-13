package com.example.demo.controller;

import com.example.demo.dto.MaterialDTO;
import com.example.demo.service.MyWxService;
import com.example.demo.util.SignUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMaterial;
import me.chanjar.weixin.mp.bean.result.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.result.WxMpMaterialUploadResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @author Administrator
 */
@RestController
public class WxController {
    @Resource
    private WxMpService wxMpService;

    @Resource
    private MyWxService myWxService;

    /**
     * 获取 access_token
     */
    @PostMapping("/wx/getToken")
    public void getToken(){
        // 从缓存中查询token
        // 查不到，获取token
        String access_token = myWxService.getToken(null,null);
        // 更新到缓存中
    }

    /**
     * wx服务器配置接口
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @param request
     * @param response
     */
    @GetMapping("/wx/security")
    public void verify(@RequestParam(value = "signature", required = true) String signature,
                       @RequestParam(value = "timestamp", required = true) String timestamp,
                       @RequestParam(value = "nonce", required = true) String nonce,
                       @RequestParam(value = "echostr", required = true) String echostr,
                       HttpServletRequest request,
                       HttpServletResponse response){
        try {
            // 校验加密串
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            } else {
                System.out.println("这里存在非法请求！");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 测试接口
     * @return
     */
    @GetMapping("/")
    public String sayHello(){
        return "I say : hello world";
    }


    // 添加客服账号 POST https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN
    // 修改客服账号 POST https://api.weixin.qq.com/customservice/kfaccount/update?access_token=ACCESS_TOKEN
    // 删除客户账号 POST https://api.weixin.qq.com/customservice/kfaccount/del?access_token=ACCESS_TOKEN

    // 客服发送消息 POST https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
    @PostMapping("/custom/sendMessage/text")
    public void sendMessageTest(){

    }

    @PostMapping("/custom/sendMessage/image")
    public void sendMessageImage(){

    }

    @PostMapping("/custom/openId")
    public String getOpenId(){
        return "";
    }

    // 获取用户基本信息（包括UnionID机制 GET https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
    @GetMapping("/custom/UnionId")
    public boolean getUnionId(){

        return false;
    }

    /**
     * 上传永久素材
     * @return
     */
    @Deprecated
    @GetMapping("/media/upload")
    public String upload(){
        WxMediaUploadResult result  = null;
        WxMpMaterialUploadResult result1 = null;
        try {
            //result = wxMpService.mediaUpload(WxConsts.MEDIA_IMAGE, WxConsts.FILE_JPG,file.getInputStream());
            result1 = wxMpService.materialFileUpload(WxConsts.MEDIA_IMAGE,new WxMpMaterial("timg",new File("H:\\timg.jpg"),null,null));
        } catch (WxErrorException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("MediaId : "+result1.getMediaId());
        return  result1.getMediaId();
    }

    /**
     * 下载素材
     * @param mediaId
     * @return
     */
    @GetMapping("/material/dowload/{mediaId}")
    public String dowload(@PathVariable("mediaId")String mediaId){
        InputStream inputStream = null;
        FileOutputStream os = null;
        String fileName = UUID.randomUUID().toString().substring(1,6);
        try {
            inputStream = wxMpService.materialImageOrVoiceDownload(mediaId);
            os = new FileOutputStream("H:\\"+fileName+".jpg");
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取素材列表
     * @return
     */
    @GetMapping("/material/list")
    public List<String> getMediaList(){
        WxMpMaterialFileBatchGetResult result = null;
        List<String> resultList = new ArrayList<>();
        try {
            result = wxMpService.materialFileBatchGet("image",0,10);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        if(result!=null&&result.getTotalCount()>0){
            resultList = result.getItems().stream().map(e->e.getMediaId()).collect(Collectors.toList());
        }
        return resultList;
    }

    /**
     * 上传素材
     * @param material
     * @return
     */
    @PostMapping("/material/upload")
    public String addMaterialFilePath(@RequestBody MaterialDTO material){
        if(material==null){
            material.setFilePath("H:\\timg.jpg");
            material.setType("image");
        }
        return myWxService.addMaterialFilePath(material.getFilePath(),material.getType());
    }

}
