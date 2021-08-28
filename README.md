# 需求规格及使用说明书

#### 1. 部署环境

客户端应用在PC端，原则上支持所有主流浏览器；鉴于运行图片分类算法对实时性有较高要求，服务端应具备良好性能，建议部署环境：Ubuntu 16.04/18.04+2.6GHz主频+16G内存+128G固态硬盘。

**软件环境**

JDK：需要安装1.8版本

Python：需要安装3.6版本

Node.js: 需要安装v14.16.0及以上版本

npm:需要安装6.14.11及以上版本

 

#### 2. 部署依赖

##### 2.1 后端依赖部署

首先通过IDE或手动下载pom.xml中项目所需的如下依赖：

**SpringFramework：**Spring Boot 内嵌 servlet 容器，也能够快速 整合各种依赖，大大减少了不必要的配置文件，使得能够更加专注于系统本身的开发； 

**Mybatis** ：相较于传统的 JDBC 更加精简，灵活，与程序代码彻底分离，大大降低了耦合度，并且支持动态 SQL 语句，极大方便了数据库操作。 

**mysql-connector-java：**后台数据库采用 MySQL 关系型数据库，其具有体积小、速度快、高开放的特性，能 

够较高的满足项目要求。 

**aliyun-sdk-oss：**阿里云提供的海量、安全、低成本、高可靠的云存储服务。OSS 可用于图片、音视频、日志等海量文件的存储。各种终端设备、Web 网站程序、移动应用可以直接向 OSS 写入或读取数据。

**springfox-swagger-ui：**自动生成接口文档和客户端服务端代码，做到调用端代码、服务端代码以及接口文档的一致性。

**进入后端源代码文件夹下，进行如下操作部署**

```
// 安装后端项目所需的依赖并打成jar包
maven clean package

// 运行后端项目
nohup java -jar aiphoto_album-0.0.1-SNAPSHOT.jar
```

**访问项目8081端口，出现接口文档说明则证明部署成功**

![image-20210715114930728](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20210715114930728.png)

##### **2.2 算法脚本部署**

**项目依赖库：**

**Tornado：**一个基于Python的Web服务框架和 异步网络库, 通过利用非阻塞网络 I/O, Tornado 可以承载成千上万的活动连接, 完美的实现了 长连接。

**Torch：** 是一个针对深度学习, 并且使用 GPU 和 CPU 来优化的 tensor library (张量库)。一个以Python优先的深度学习框架，不仅能够实现强大的GPU加速，同时还支持动态神经网络。

**PIL：**PIL(Python Image Library)是python的第三方图像处理库。包括了基础的图像处理函数，包括对点的处理，使用众多的卷积核(convolution kernels)做过滤(filter),还有颜色空间的转换。PIL库同样支持图像的大小转换，图像旋转，以及任意的仿射变换。

**Requests：**Python第三方库，处理URL，用于访问网络资源。

**进入算法源代码文件夹下，进行如下操作部署**

```
//安装python项目所需依赖
pip install -r requirements.txt
//运行分类算法脚本(在src目录下)
python app.py
```



##### 2.3 前端部署依赖

前端使用HTML+CSS+Typescript来进行编写，并选用了**React**作为开发框架。同时使用了**Eslint**插件来约束前端代码规范，方便在开发过程中的模式统一以及后期的代码维护。

**UI**方面，使用了**Ant-Design**作为React的前端框架库，在其强大组件之上，进行了进一步封装，形成了自己的组件，例如图像展示组件，分页组件等。

交互方面，采用**Axios**进行前后端交互，并对Axios进行进一步封装，形成业务渗透，使其在使用时即开即用，更加方便。

打包则使用**webpack**工具，在react框架生态之上，引入自己所编写的webpack配置文件，使得其打包时能满足开发者需求（懒加载打包，缓存打包）。

使用**yarn**作为我们快速、可靠、安全的依赖管理工具。

**进入前端源代码文件夹下，进行如下操作部署**

```shell
// 安装前端项目所需的依赖
yarn install

// 运行前端项目
yarn start
```

**运行成功后浏览器将自动开启页面，并可以通过3000端口手动访问**

![](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210715115346108.png)



#### 3.使用说明

##### 3.1 登录功能

在登录界面输入用户名密码进行登录操作，进入主界面。

##### 3.2 上传图片

点击上传照片按钮，进行照片上传，支持单张上传和批量上传

![image-20210715121338239](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210715121338239.png)

**3.3 分类结果展示**

点击左侧分类相册（这里演示的是事物相册），可以看到，在大类相册内还会根据图片特征将其分为多个小相册，在该页面上可以看到该相册下的分类相册名称以及相册内图片数量。点击这些分类相册，将会继续跳转到具体的分类相册中图片展示页面。

![image-20210828100808627](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210828100808627.png)

点击这些分类相册，将会继续跳转到具体的分类相册中图片展示页面。

![image-20210715123020722](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210715123020722.png)

##### 3.4 人脸聚类结果展示

点击左侧分类相册中的人像，可以看到用户上传的照片中人脸的聚类结果。

![image-20210715122433164](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210715122433164.png)

点击具体相册，可以看到包含某一张人脸的全部照片。可以实现集体照片的快速分发。

![image-20210715122610235](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210715122610235.png)

##### 3.5 自动剪辑精彩时刻功能

点击生成精彩时刻按钮，选中要生成视频的照片，然后点击生成按钮，服务端开始进行处理。 

![image-20210828101039055](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210828101039055.png)

待视频生成后，用户则可在前端浏览生成的视频。

![image-20210828101140394](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210828101140394.png)

##### 3.6 照片详情页面展示

点击单个图片，将会弹出 高清图弹窗，该弹窗内容包括图片的高清图，图片标题，图片上传时间，以及图片的分类卡片等信息。点击右下角的删除按钮对该图片进行删除操作，同时可以对照片进行在线编辑。

![image-20210828103251721](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210828103251721.png)

若点击图片本身， 将会进入到全局预览模式，方便用户进行查看以及下载。

![image-20210828103316880](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210828103316880.png)

点击图片缩略图右侧的PS按钮，可以对照片进行在线编辑。支持裁剪、旋转、滤镜、水印等多种操作

![image-20210828103357249](https://oss-album.oss-cn-beijing.aliyuncs.com/test/image-20210828103357249.png)

