package com.example.aiphoto_album.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.aiphoto_album.api.CommonResult;
import com.example.aiphoto_album.model.Category;
import com.example.aiphoto_album.model.OssCallbackResult;
import com.example.aiphoto_album.model.OssPolicyResult;
import com.example.aiphoto_album.model.User;
import com.example.aiphoto_album.service.OssService;
import com.example.aiphoto_album.service.PhotoService;
import com.example.aiphoto_album.service.UserService;
import com.example.aiphoto_album.service.ClassifyService;
import com.example.aiphoto_album.service.FaceService;

@Api(tags = "OssController", description = "Oss管理")
@Controller
@RequestMapping("/oss")
public class OssController {
    @Autowired
    private OssService ossService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClassifyService classifyService;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private FaceService faceService;

    @ApiOperation(value = "oss上传签名生成")
    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<OssPolicyResult> policy() {
        OssPolicyResult result = ossService.policy();
        return CommonResult.success(result);
    }

    @ApiOperation(value = "oss上传成功回调")
    @RequestMapping(value = "callback", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<OssCallbackResult> callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = ossService.callback(request);
        System.out.println("进入了回调");
        return CommonResult.success(ossCallbackResult);
    }
    
    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/postfile", headers = "content-type=multipart/form-data")
    @ResponseBody
    public CommonResult<Object> fileUpload(@RequestParam(value = "file", required = true) List<MultipartFile> files, @RequestParam(value = "userId", required = true) String userId) throws Exception {
        User user = userService.searchUserInfo(userId);
        if (user == null){
            return CommonResult.failed("用户不存在");
        }
        int ret = 0;
        for(int i = 0; i < files.size(); i++){
            //上传获取URL
            Map<String, Object> map =  ossService.fileUpload(files.get(i), userId);
            if(map.get("code") == "200"){
                //调用分类脚本预测
                String imgUrl = map.get("imgUrl").toString();
                Map<String,String> predictions = classifyService.getClassifyResult(userId, imgUrl);
                for(Map.Entry<String,String> entry:predictions.entrySet()){
                    String[] arr = entry.getValue().split(",");
                    for(String item:arr){
                        Category category = new Category();
                        category.setFileId((Integer)map.get("fileId"));
                        //如果分类结果为person则检测人脸
                        if(item.equals("person")){
                            //先插入一条object为person的数据
                            category.setGroup("object_categories");
                            category.setItem(item);
                            ret = photoService.insertClassifyInfo(category);
                            if(ret <= 0){
                                return CommonResult.failed("File " + map.get("fileId") + " 生成分类信息时错误！");
                            }

                            ArrayList<String> people = faceService.faceHandler(imgUrl);
                            //若提取不到人脸特征直接开始下一次循环
                            if(people == null){
                                System.out.println("faceHandleError!");
                                continue;
                            }
                            System.out.println(people);
                            for(String person:people){
                                category.setGroup("face_categories");
                                category.setItem(person);
                                ret = photoService.insertClassifyInfo(category);
                                if(ret <= 0){
                                    return CommonResult.failed("File " + map.get("fileId") + " 生成分类信息时错误！");
                                }
                            }
                        }else{
                            category.setGroup(entry.getKey());
                            category.setItem(item);
                            ret = photoService.insertClassifyInfo(category);
                            if(ret <= 0){
                                return CommonResult.failed("File " + map.get("fileId") + " 生成分类信息时错误！");
                            }
                        }
                    }
                } 
            }else if(map.get("code") == "400"){
                return CommonResult.failed(map.get("msg").toString());
            }
        }
        return CommonResult.success(null,"图片上传成功!");
    }
}
