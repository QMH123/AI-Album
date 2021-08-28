package com.example.aiphoto_album.util;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.cmss.sdk.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.http.constant.Region;
import com.chinamobile.cmss.sdk.http.signature.Credential;
import com.chinamobile.cmss.sdk.request.IECloudRequest;
import com.chinamobile.cmss.sdk.request.face.FaceRequestFactory;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.File;

@Component
public class FaceUtil {
    @Value("${yidongyun.accesskey}")
    private String accesskey;
    @Value("${yidongyun.secretkey}")
    private String secretkey;
    @Autowired
    OssUtil ossUtil;

    public JSONObject faceRequest(JSONObject params,String api){
        Credential credential = new Credential(accesskey, secretkey);
        ECloudDefaultClient client = new ECloudDefaultClient(credential, Region.POOL_SZ);
        IECloudRequest faceDetectRequest = FaceRequestFactory.getFaceRequest(api, params);
        try{
            JSONObject response = (JSONObject) client.call(faceDetectRequest);
            System.out.println(response.toString());
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String faceClip(String image,int x, int y, int width, int height){
        String tempPath;
        try {
            URL url = new URL(image);
            BufferedImage bufImage = ImageIO.read(url);
            BufferedImage Subimage = bufImage.getSubimage(x, y, width, height);
            String tempName = (int)((Math.random()*9+1)*100000) + image.split("/")[image.split("/").length-1];
            tempPath = "D:/faceTmp/" + tempName;
            System.out.println(tempPath);
            ImageIO.write(Subimage, "JPEG", new File(tempPath));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        
        return tempPath;
    }
}
