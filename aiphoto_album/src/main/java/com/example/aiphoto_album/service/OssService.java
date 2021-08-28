package com.example.aiphoto_album.service;

import com.example.aiphoto_album.model.OssPolicyResult;

import org.springframework.web.multipart.MultipartFile;

import com.example.aiphoto_album.model.OssCallbackResult;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * oss上传管理Service
 * Created by macro on 2018/5/17.
 */
public interface OssService {
    /**
     * oss上传策略生成
     */
    OssPolicyResult policy();

    /** 
     * oss上传成功回调
     */
    OssCallbackResult callback(HttpServletRequest request);
        /** 
     * oss上传成功回调
         * @throws Exception
     */
    public Map<String, Object> fileUpload(MultipartFile file, String userId) throws Exception;
}
