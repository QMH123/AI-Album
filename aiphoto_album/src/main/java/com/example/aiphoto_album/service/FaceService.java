package com.example.aiphoto_album.service;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface FaceService {
    public void createFaceLib(String name,String description);

    public JSONObject detectFace(String image);

    public void getFaceLib();

    public void addFace(String name,String image);

    public void deleteFace(String memberId);

    public JSONArray searchFace(String image);

    public int getFaceMember();

    public ArrayList<String> faceHandler(String image);
}
