package com.example.aiphoto_album.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class ClassifyService {
    @Autowired
    private RestTemplate restTemplate;
    public Map<String,String> getClassifyResult(String userId, String fileUrl){
        try {
            String url = "http://localhost:23333/classification"; 
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> Json = new LinkedMultiValueMap<>();
            Json.add("id", userId);
			Json.add("file", fileUrl);

            JSONObject jsonObject= restTemplate.postForObject(url,new HttpEntity<>(Json,headers),JSONObject.class);
            //System.out.println(jsonObject.toString());
            if(jsonObject.get("status").toString().equals("200")){
                JSONObject predictions = jsonObject.getJSONObject("predictions");
                System.out.println(predictions.toJSONString());
                String pre = predictions.toJSONString();
                Map<String, String> params = JSONObject.parseObject(pre, new TypeReference<Map<String, String>>(){});
                // System.out.println(params);
                return params;
            }
            
        }catch (Exception e){
        }
        return null;
    }
}
