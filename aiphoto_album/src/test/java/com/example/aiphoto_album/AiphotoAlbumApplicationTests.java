package com.example.aiphoto_album;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.drew.imaging.ImageProcessingException;
import com.example.aiphoto_album.dao.CategoryMapper;
import com.example.aiphoto_album.dao.PhotoMapper;
import com.example.aiphoto_album.model.Category;
import com.example.aiphoto_album.model.Photo;
import com.example.aiphoto_album.service.FaceService;
import com.example.aiphoto_album.service.OssService;
import com.example.aiphoto_album.service.PhotoService;
import com.example.aiphoto_album.util.FaceUtil;
import com.example.aiphoto_album.util.MetadataUtil;
import com.example.aiphoto_album.util.OssUtil;
import com.mysql.cj.xdevapi.JsonArray;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
class AiphotoAlbumApplicationTests {
	@Autowired 
	PhotoService photoService;
	@Autowired
    private RestTemplate restTemplate;
	@Autowired
	PhotoMapper photoMapper;
	@Autowired
	CategoryMapper categoryMapper;
	@Autowired
	FaceService faceService;
	@Autowired
	FaceUtil faceUtil;
	@Autowired
	OssUtil ossUtil;

	@Test
	void contextLoads() {
	}

	// @Test
	// void readPhotoInfoTest() throws ImageProcessingException, IOException{
	// 	//File file = new File("D:/photo/IMG_20210621_165556.jpg");
	// 	File file = new File("D:/photo/IMG_20210621_165556.jpg");
		
	// 	MetadataUtil metadataUtil = new MetadataUtil();
	// 	metadataUtil.getPhotoTime(file);
	// 	//System.out.println(map.get("shootTime"));
	// 	return;
	// }

	// @Test
	// void insertPhotoInfoTest(){
	// 	Photo photo = new Photo("1624383248976.png", "imgUrl=http://oss-album.oss-cn-beijing.aliyuncs.com/userImg/1624383248976.png?Expires=1939743241&OSSAccessKeyId=LTAI5tCpALcnMfkFURnmGgPP&Signature=Xkz7r0jzhKljYQFDGqKebsUuMxU%3D", "userId", 0, "123", "456", "2021-06-23 00:42:27", "2021-06-23 00:42:27");
	// 	int ret = photoService.insertPhotoInfo(photo);
	// 	System.out.println(ret);
	// 	//Photo [fileName=1624383248976.png, heigt=495, imgUrl=http://oss-album.oss-cn-beijing.aliyuncs.com/userImg/1624383248976.png?Expires=1939743241&OSSAccessKeyId=LTAI5tCpALcnMfkFURnmGgPP&Signature=Xkz7r0jzhKljYQFDGqKebsUuMxU%3D, isClassify=0, lastModifyTime=2021-06-23 01:34:01, uploadTime=2021-06-23 01:34:01, userId=2018091609025, width=534]
	// }

	// @Test
    // void getJson(){
	// 	try {
    //         RestTemplate re = new RestTemplate();
    //         String url = "http://localhost:23333/classification";
    //         HttpHeaders headers = new HttpHeaders();
    //         headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    //         MultiValueMap<String, Object> loginJson = new LinkedMultiValueMap<>();
    //         loginJson.add("id", "123");
	// 		loginJson.add("file", "https://oss-album.oss-cn-beijing.aliyuncs.com/userImg/1624891645738.jpg");

    //         JSONObject jsonObject= re.postForObject(url,new HttpEntity<>(loginJson,headers),JSONObject.class);
	// 		//System.out.println(jsonObject.toString());
	// 		JSONObject predictions = jsonObject.getJSONObject("predictions");
	// 		//System.out.println(predictions.toJSONString());
	// 		String pre = predictions.toJSONString();
	// 		Map<String, String> params = JSONObject.parseObject(pre, new TypeReference<Map<String, String>>(){});
	// 		//System.out.println(params);
	// 		// JSONObject predict = JSON.parseObject(pre);
	// 		// System.out.println(predict.get("scenery_categories"));
	// 		// System.out.println(predict.get("object_categories"));
    //     }catch (Exception e){
    //     }
    // }

	// @Test
	// void insertClassifyInfo(){
	// 	Category category = new Category("1.jpg", "sdfds", "gfh");
	// 	int ret = photoService.insertClassifyInfo(category);
	// 	System.out.println(ret);
	// }

	// @Test
	// void getAllItem(){
	// 	Map<String,String> map = new HashMap<String,String>(); 
	// 	map.put("userId", "2018091609025");
	// 	map.put("group", "object_categories");
	// 	String[] items = photoMapper.getAllItems(map);
	// 	for(String item:items){
	// 		System.out.println(item);
	// 	}
	// }

	@Test
	void getAlbumInfo(){
		Long fileId = (long) 34;
		String[] categories = categoryMapper.getImgCategories(fileId);
		for(String category:categories){
			System.out.println(category);
		}
	}

	@Test
	void getPhotoInfo(){
		Long fileId = (long) 50;
		Map<String,String> map =  photoMapper.getPhotoInfo(fileId);
	}

	// @Test
	// void createFaceLib(){
	// 	faceService.createFaceLib("faceLib_online", "人脸库信息");
	// 	//611a5b46177bae0001df3beb
	// }

	@Test
	void detectFace(){
		try {
			String image = "http://oss-album.oss-cn-beijing.aliyuncs.com/userImg/1629165411735.jpg";
			JSONObject res =  faceService.detectFace(image);
		} catch (Exception e) {
			System.out.println("catch");
		}
	}

	// @Test
	// void detectFace(){
	// 	String image = "https://oss-album.oss-cn-beijing.aliyuncs.com/userImg/20210713211939.jpg";
	// 	JSONObject res =  faceService.detectFace(image);
	// 	String memberName = "";
	// 	if(res != null){
	// 		JSONArray faces =  res.getJSONArray("faces");
	// 		for(int i = 0; i < (int)res.get("faceNum"); i++){
	// 			JSONObject face = faces.getJSONObject(i);
	// 			//System.out.println(face);
	// 			int x = ((Double)(face.getJSONObject("area").get("x"))).intValue();
	// 			int y = ((Double)(face.getJSONObject("area").get("y"))).intValue();
	// 			int width = ((Double)(face.getJSONObject("area").get("width"))).intValue();
	// 			int height = ((Double)(face.getJSONObject("area").get("height"))).intValue();
	// 			String tempPath = faceUtil.faceClip(image, x, y, width, height);
	// 			String url = "";
	// 			try {
	// 				url = ossUtil.uploadImg2Oss(tempPath);
	// 				//System.out.println(url); 
	// 				JSONArray ret = faceService.searchFace(url);
	// 				System.out.println(ret); 
	// 				if(ret.isEmpty()){
	// 					int memberId = faceService.getFaceMember();
	// 					faceService.addFace("Person" + memberId, url);
	// 				}else{
	// 					int index;
	// 					for(index = 0;index < ret.size(); index++){
	// 						JSONObject f = ret.getJSONObject(index);
	// 						//人脸识别置信度阈值
	// 						if((Double)f.get("confidence") > 0.7){
	// 							memberName = f.get("memberName").toString();
	// 							break;
	// 						}
	// 					}
    //                     if(index == ret.size()){
    //                         int memberId = faceService.getFaceMember();
	// 					    faceService.addFace("Person" + memberId, url);
    //                     }
	// 				}
	// 				url = "oss-album/" + url.split("/")[url.split("/").length - 1];
	// 				ossUtil.deleteImg(url);
	// 			} catch (IOException e) {
	// 				// TODO Auto-generated catch block
	// 				e.printStackTrace();
	// 			}
	// 			//System.out.println(tempPath);
	// 		}
	// 	}
	// }

	@Test 
	void searchFaceLib(){
		faceService.getFaceLib();
	}

	// @Test 
	// void ossdelete(){
	// 	ossUtil.deleteImg("userImg/13574420210713211939.jpg");
	// }

	// @Test 
	// void deleteFace(){
	// 	faceService.deleteFace("6119148a8b14fb0001552c7a");
	// 	faceService.deleteFace("6119147a177bae0001df3bc7");
	// }

	// @Test
	// void addFace(){
	// 	faceService.addFace("Person0", "https://oss-album.oss-cn-beijing.aliyuncs.com/userImg/20210713211928.jpg");
	// 	faceService.addFace("Person1", "https://oss-album.oss-cn-beijing.aliyuncs.com/userImg/IMG_20210816_205410.jpg.jpg");
	// 	faceService.addFace("Person2", "https://oss-album.oss-cn-beijing.aliyuncs.com/userImg/20210713212423.jpg");
	// }
}
