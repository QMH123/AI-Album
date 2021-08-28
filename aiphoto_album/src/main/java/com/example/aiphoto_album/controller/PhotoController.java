package com.example.aiphoto_album.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.aiphoto_album.api.CommonResult;
import com.example.aiphoto_album.model.AlbumInfo;
import com.example.aiphoto_album.model.PhotoInfo;
import com.example.aiphoto_album.model.User;
import com.example.aiphoto_album.service.PhotoService;
import com.example.aiphoto_album.service.UserService;
import com.example.aiphoto_album.util.OssUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "PhotoController", description = "图片相册管理")
@Controller
@RequestMapping("/photo")
public class PhotoController {
    @Autowired
    PhotoService photoService;
    @Autowired
    UserService userService;
    @Autowired 
    OssUtil ossUtil;

    @ApiOperation(value = "获取相册或图片信息")
    @GetMapping("/getPhotoInfo")
    @ResponseBody
    public CommonResult<Object> getPhotoInfo(@RequestParam(value = "userId", required = true) String userId,
    @RequestParam(value = "id", required = true) Long id,
    @RequestParam(value = "isAlbum", required = true) Boolean isAlbum){
        User user = userService.searchUserInfo(userId);
        if (user == null){
            return CommonResult.failed("用户不存在");
        }
        if(isAlbum){
            if(id > 2){
                return CommonResult.failed("传入的id参数不合法");
            }
            String[] groups = {"object_categories","scenery_categories","face_categories"};
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("userId", userId);
            map.put("group", groups[id.intValue()]);
            List<AlbumInfo> albumInfos = photoService.getAlbumInfo(map);
            if(albumInfos.size() == 0){
                return CommonResult.failed("获取的数据为空!");
            }
            return CommonResult.success(albumInfos, "获取相册信息成功");
        }else{
            PhotoInfo photoInfo = photoService.getPhotoInfo(id);
            if(photoInfo == null){
                return CommonResult.failed("图片ID不存在");
            }
            return CommonResult.success(photoInfo, "获取照片信息成功");
        }
    }

    @ApiOperation(value = "获取指定图片列表")
    @GetMapping("/getImgList")
    @ResponseBody
    CommonResult<Object> getImgList(@RequestParam(value = "userId", required = true) String userId,@RequestParam(value = "item", required = true) String item,
    @RequestParam(value = "curPage", required = true) Integer curPage, @RequestParam(value = "pageSize", required = true) Integer pageSize){
        User user = userService.searchUserInfo(userId);
        if (user == null){
            return CommonResult.failed("用户不存在");
        }
        if (curPage <= 0 || pageSize <=0){
            return CommonResult.failed("curPage与pageSize不能小于0");
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", userId);
        map.put("item", item);
        map.put("curIndex", (curPage - 1) * pageSize);
        map.put("pageSize", pageSize);
        Map<String,Object> imgList = new HashMap<String,Object>();
        imgList = photoService.getImgList(map);
        if (imgList == null){
            return CommonResult.failed("图片列表信息为空");
        }
        return CommonResult.success(imgList,"获取图片列表信息成功");
    }

    @ApiOperation(value = "获取全部图片列表(时间降序)")
    @GetMapping("/getAllImg")
    @ResponseBody
    CommonResult<Object> getAllImg(@RequestParam(value = "userId", required = true) String userId, @RequestParam(value = "curPage", required = true) Integer curPage,
    @RequestParam(value = "pageSize", required = true) Integer pageSize){
        User user = userService.searchUserInfo(userId);
        if (user == null){
            return CommonResult.failed("用户不存在");
        }
        if (curPage <= 0 || pageSize <=0){
            return CommonResult.failed("curPage与pageSize不能小于0");
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", userId); 
        map.put("curIndex", (curPage - 1) * pageSize);
        map.put("pageSize", pageSize);
        Map<String,Object> imgList = new HashMap<String,Object>();
        imgList = photoService.getAllImg(map);
        if (imgList == null){
            return CommonResult.failed("图片列表信息为空");
        }
        return CommonResult.success(imgList,"获取图片列表信息成功");
    }

    @ApiOperation(value = "删除图片信息")
    @GetMapping("/deletePhotoInfo")
    @ResponseBody
    CommonResult<Object> deletePhotoInfo(@RequestParam(value = "fileId", required = true) Long fileId, @RequestParam(value = "imgUrl", required = true) String imgUrl){
        int res = photoService.deletePhotoInfo(fileId);
        String fileName = imgUrl.split("/")[imgUrl.split("/").length - 1];
        ossUtil.deleteImg(fileName);
        if (res > 0){
            return CommonResult.success(1, "删除图片信息成功");
        }else if (res == 0){
            return CommonResult.failed("fileId不存在");
        }else{
            return CommonResult.failed("删除失败");
        }
    }

    @ApiOperation(value = "模糊查询图片")
    @GetMapping("/vagueSearch")
    @ResponseBody
    CommonResult<Object> vagueSearch(@RequestParam(value = "keyWord", required = true) String keyWord, @RequestParam(value = "curPage", required = true) Integer curPage,
    @RequestParam(value = "pageSize", required = true) Integer pageSize, @RequestParam(value = "userId", required = true) String userId){
        User user = userService.searchUserInfo(userId);
        if (user == null){
            return CommonResult.failed("用户不存在");
        }
        if (curPage <= 0 || pageSize <=0){
            return CommonResult.failed("curPage与pageSize不能小于0");
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", userId);
        map.put("keyWord", keyWord);
        map.put("curIndex", (curPage - 1) * pageSize);
        map.put("pageSize", pageSize);
        Map<String,Object> imgList = new HashMap<String,Object>();
        imgList = photoService.vagueSearch(map);
        if (imgList == null){
            return CommonResult.failed("图片列表信息为空");
        }
        return CommonResult.success(imgList,"获取图片列表信息成功");
    }
}
