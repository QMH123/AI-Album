package com.example.aiphoto_album.util;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.example.aiphoto_album.model.Category;
import com.example.aiphoto_album.model.Photo;
import com.example.aiphoto_album.service.ClassifyService;
import com.example.aiphoto_album.service.PhotoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class OssUtil {
    protected static final Logger log = LoggerFactory.getLogger(OssUtil.class);
 
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
 
    //文件存储目录
    private String filedir = "userImg/";
    @Autowired 
    PhotoService photoService;

    @Autowired
    ClassifyService classifyService;
 
    /**
     *
     * 上传图片
     * @param file
     * @return
     * @throws Exception
     */
    public String uploadImg2Oss(MultipartFile file,String userId) throws Exception {
        if (file.getSize() > 1024 * 1024 *20) {
            return "图片太大";//RestResultGenerator.createErrorResult(ResponseEnum.PHOTO_TOO_MAX);
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name);
        } catch (Exception e) {
            //return "上传失败";//RestResultGenerator.createErrorResult(ResponseEnum.PHOTO_UPLOAD);
            System.out.println("上传失败");
            return name;//RestResultGenerator.createSuccessResult(name);
        }
       /*  String imgUrl = getImgUrl(name) ;
        Map<String,String> predictions = classifyService.getClassifyResult(userId, imgUrl);
        //System.out.println(predictions);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Photo photo = new Photo(null, originalFilename, imgUrl, userId, map.get("height"), map.get("width"), df.format(new Date()));
        System.out.println(photo.toString());
        int ret = photoService.insertPhotoInfo(photo);
            //System.out.println(ret);
        for(Map.Entry<String,String> entry:predictions.entrySet()){
            String[] arr = entry.getValue().split(",");
            for(String i:arr){
                Category category = new Category(photo.getFileId(), entry.getKey(), i);
                ret = photoService.insertClassifyInfo(category);
            }
        } */
        return name;
    }
 
    /**
     * 上传图片获取fileUrl
     * @param instream
     * @param fileName
     * @return
     */
    private String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
 
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }
 
    /**
     * 获取图片路径
     * @param fileUrl
     * @return
     */
    public String getImgUrl(String fileUrl) {
        if (fileUrl != "") {
            String[] split = fileUrl.split("/");
            String url =  this.getUrl(this.filedir + split[split.length - 1]);
            return url;
        }
        return null;
    }
 
    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }
 
 
    /**
     * 多图片上传
     * @param fileList
     * @return
     * @throws Exception
     */
    public String checkList(List<MultipartFile> fileList, String userId) throws Exception {
        String  fileUrl = "";
        String  str = "";
        String  photoUrl = "";
        for(int i = 0;i< fileList.size();i++){
            fileUrl = uploadImg2Oss(fileList.get(i),userId);
            str = getImgUrl(fileUrl);
            if(i == 0){
                photoUrl = str;
            }else {
                photoUrl += "," + str;
            }
        }
        return photoUrl.trim();
    }
 
    /**
     * 单个图片上传
     * @param file
     * @return
     * @throws Exception
     */
    public String checkImage(MultipartFile file, String userId) throws Exception{
        String fileUrl = uploadImg2Oss(file,userId);
        String str = getImgUrl(fileUrl);
        return str.trim();
    }

    public void deleteImg(String url) {
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, "userImg/" + url);
        System.out.println("删除 " + url + " 成功");
        ossClient.shutdown();
    }

    public String uploadImg2Oss(String url) throws IOException {
        File fileOnServer = new File(url);
        FileInputStream fin;
        String ret;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFile2OSS(fin, split[split.length - 1]);
            String name = split[split.length - 1];
            ret = getImgUrl(name).split("[?]")[0];
        } catch (FileNotFoundException e) {
            throw new IOException("图片上传失败");
        }
        return ret;
    } 

}
