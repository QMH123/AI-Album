package com.example.aiphoto_album.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.aiphoto_album.service.FaceService;
import com.example.aiphoto_album.util.FaceUtil;
import com.example.aiphoto_album.util.OssUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class FaceServiceImpl implements FaceService{
    @Autowired
    private FaceUtil faceUtil;
    @Autowired
    private OssUtil ossUtil;
    @Value("${yidongyun.faceStoreId}")
    private String faceStoreId;

    public void createFaceLib(String name,String description){
        String URI = "/api/human/face/v1/store/create";
        JSONObject params = new JSONObject();
        params.put("name", name);
        params.put("description", description);
        JSONObject res = faceUtil.faceRequest(params, URI);
    }

    public void getFaceLib(){
        String URI = "/api/human/face/v1/store/get";
        JSONObject params = new JSONObject();
        params.put("faceStoreId", faceStoreId);
        JSONObject res = faceUtil.faceRequest(params, URI);
    }

    public void addFace(String name,String image){
        String URI = "/api/human/face/v1/store/member/create";
        JSONObject params = new JSONObject();
        params.put("faceStoreId", faceStoreId);
        params.put("name", name);
        params.put("imageType", "URL");
        params.put("image", image);
        JSONObject res = faceUtil.faceRequest(params, URI);
    }

    public void deleteFace(String memberId){
        String URI = "/api/human/face/v1/store/member/delete";
        JSONObject params = new JSONObject();
        params.put("memberId", memberId);
        JSONObject res = faceUtil.faceRequest(params, URI);
    }

    public JSONArray searchFace(String image){
        String URI = "/api/human/face/v1/search";
        List<String> faceStoreIds = new ArrayList<String>();
        faceStoreIds.add(faceStoreId);
        JSONObject params = new JSONObject();
        params.put("faceStoreIds", faceStoreIds);
        params.put("imageType", "URL");
        params.put("image", image);
        params.put("maxFaceNum", 1);
        JSONObject res = faceUtil.faceRequest(params, URI);
        if(res.getString("state") == "OK"){
            return res.getJSONObject("body").getJSONArray("results");
        }
        return null;
    }
    
    public JSONObject detectFace(String image){
        String URI = "/api/human/face/v1/detect";
        JSONObject params = new JSONObject();
        params.put("imageType", "URL");
        params.put("image", image);
        params.put("maxFaceNum", 5);
        JSONObject res = faceUtil.faceRequest(params, URI);
        if(res.get("state").toString() == "OK"){
            return (JSONObject)res.get("body");
        }
        return null;
    }

    public int getFaceMember(){
        String URI = "/api/human/face/v1/store/get";
        JSONObject params = new JSONObject();
        params.put("faceStoreId", faceStoreId);
        JSONObject res = faceUtil.faceRequest(params, URI);
        if(res.get("state").toString() == "OK"){
            return (int)res.getJSONObject("body").get("memberCount");
        }
        return -1;
    }

    public ArrayList<String> faceHandler(String image){
        try {
            JSONObject res =  detectFace(image);
            ArrayList<String>  memberNames = new ArrayList<String>();
            String memberName = "";
            JSONArray faces =  res.getJSONArray("faces");
            for(int i = 0; i < (int)res.get("faceNum"); i++){
                JSONObject face = faces.getJSONObject(i);
                int x = ((Double)(face.getJSONObject("area").get("x"))).intValue();
                int y = ((Double)(face.getJSONObject("area").get("y"))).intValue();
                int width = ((Double)(face.getJSONObject("area").get("width"))).intValue();
                int height = ((Double)(face.getJSONObject("area").get("height"))).intValue();
                String tempPath = faceUtil.faceClip(image, x, y, width, height);
                String url = "";
                try {
                    url = ossUtil.uploadImg2Oss(tempPath);
                    System.out.println(url); 
                    JSONArray ret = searchFace(url);
                    System.out.println(ret); 
                    if(ret.isEmpty()){
                        int memberId = getFaceMember();
                        memberName = "Person" + memberId;
                        addFace(memberName, url);
                        memberNames.add(memberName);
                    }else{
                        int index;
                        for(index = 0;index < ret.size(); index++){
                            JSONObject f = ret.getJSONObject(index);
                            //人脸识别置信度阈值
                            if((Double)f.get("confidence") > 0.5){
                                memberName = f.get("memberName").toString();
                                memberNames.add(memberName);
                                break;
                            }
                        }
                        if(index == ret.size()){
                            memberName = "Person" + getFaceMember();
                            addFace(memberName, url);
                            memberNames.add(memberName);
                        }
                    }
                    url = url.split("/")[url.split("/").length - 1];
                    ossUtil.deleteImg(url);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //System.out.println(tempPath);
            }
            return memberNames;
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
	}
    
}
