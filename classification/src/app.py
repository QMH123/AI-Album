#!/bin/env python
# -*- coding:utf-8 -*-
import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web
from infer import IdentityLayer,ImageClassifier,infer
from video import *
from oss import storage

import tornado.gen
from tornado.concurrent import run_on_executor
from concurrent.futures import ThreadPoolExecutor
import time
import json
import requests as req
from tornado.options import define, options
import shutil

define("port", default=5000, help="run on the given port", type=int)

class Executor(ThreadPoolExecutor):
    """ 创建多线程的线程池，线程池的大小为10
    创建多线程时使用了单例模式，如果Executor的_instance实例已经被创建，
    则不再创建，单例模式的好处在此不做讲解
    """
    _instance = None

    def __new__(cls, *args, **kwargs):
        if not getattr(cls, '_instance', None):
            cls._instance = ThreadPoolExecutor(max_workers=10)
        return cls._instance


class testHandler(tornado.web.RequestHandler):
    executor = Executor()

    # @tornado.web.asynchronous  # 异步处理
    @tornado.gen.coroutine  # 使用协程调度
    def get(self):
        yield self.sleep()
        self.write('finish\n')
        self.finish()

    @tornado.concurrent.run_on_executor  # 增加并发量
    def sleep(self):
        time.sleep(20)


class indexHandler(tornado.web.RequestHandler):

    def get(self):
        self.write("hello + word")

class BaseHandler(tornado.web.RequestHandler):
 #blog.csdn.net/moshowgame 解决跨域问题
 def set_default_headers(self):
  self.set_header('Access-Control-Allow-Origin', '*')
  self.set_header('Access-Control-Allow-Headers', '*')
  self.set_header('Access-Control-Max-Age', 1000)
  #self.set_header('Content-type', 'application/json')
  self.set_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS')
  self.set_header('Access-Control-Allow-Headers',#'*')
      'authorization, Authorization, Content-Type, Access-Control-Allow-Origin, Access-Control-Allow-Headers, X-Requested-By, Access-Control-Allow-Methods')

class musicHandler (BaseHandler):
    def get(self):
        music_dir = "D://img/img/music/"
        music_list = sorted(os.listdir(music_dir))
        res = list()
        i = 0 
        for title in music_list:   
            music = {} 
            music['id'] = i
            music['title'] = title
            i += 1
            res.append(music)
        self.write({'data':res})
        self.finish()


class videoHandler (BaseHandler):
    def post(self):
        frames_dir = 'D://img/img/'
        video_dst = 'D://img/video.mp4'
        json_byte = self.request.body
        json_str = json_byte.decode('utf-8')
        json_obj = json.loads(json_str)  # json格式的字符串转换成json对象
        files = json_obj.get('file', list())
        music_id = json_obj.get('musicId')
        title = json_obj.get('title')
        for file in files:
            url2img(file)
        pics2video(frames_dir, video_dst)
        editVideo(video_dst, music_id, title)
        res = storage("wonderfulTime.mp4",video_dst)
        shutil.rmtree(frames_dir)
        os.mkdir(frames_dir)
        data = {}
        data["message"] = '请求成功'
        data["status"] = '200'
        data["url"] = res
        self.write(data)
    
    def options(self):
        #返回方法1
        #self.set_status(204)
        self.finish()
        #返回方法2
        #self.write('{"errorCode":"00","errorMessage","success"}')

class ocrHandler(tornado.web.RequestHandler):
    executor = Executor()

    # @tornado.web.asynchronous  # 异步处理
    @tornado.gen.coroutine  # 使用协程调度
    def post(self):
        data = {"message": False, 'status': ''}
        # json_byte = self.request.body
        # json_str = json_byte.decode('utf-8')
        # json_obj = json.loads(json_str)  # json格式的字符串转换成json对象
        # id = json_obj.get('id', '')
        # file = json_obj.get('file', '')

        # id = self.get_argument("id", default=None)
        # 获取上传的文件
        # file = self.request.files['file'][0]
        # front = self.request.get_argument("front",default=None)
        id = self.get_body_argument('id')
        file = self.get_body_argument('file')

        if id is not None and file is not None:
            data['predictions'] = yield self._process(file)
            data["message"] = '请求成功'
            data["status"] = '200'
            data["session_id"] = id
        else:
            data['status'] = '403'
            data = {"message": '请求失败，缺少请求参数'}

        self.write(data)
        self.finish()

    @tornado.gen.coroutine  # 使用协程调度
    def get(self):
        data = {"message": False, 'status': ''}
        id = self.get_query_argument('id')
        file = self.get_query_argument('file')

        if file is not None and id is not None:
            data['predictions'] = yield self._process(file)
            data["message"] = '请求成功'
            data["status"] = '200'
            data["session_id"] = id
        else:
            data['status'] = '403'
            data = {"message": '请求失败，缺少请求参数'}

        self.write(data)
        self.finish()

    @tornado.concurrent.run_on_executor  # 增加并发量
    def _process(self, image):
        # image = base64.b64decode(image)
        # image = np.fromstring(image, np.uint8)
        # image = cv2.imdecode(image, cv2.IMREAD_COLOR)

        # image = image.read()
        # image = cv2.imdecode(np.frombuffer(image, np.uint8), cv2.IMREAD_COLOR)
        result = infer(image)
        return result


if __name__ == "__main__":
    tornado.options.parse_command_line()
    app = tornado.web.Application(handlers=[
        (r"/", indexHandler),
        (r"/classification", ocrHandler),
        (r"/sleep", testHandler),
        (r"/generateVideo", videoHandler),
        (r"/getMusicList", musicHandler),
    ])
    http_server = tornado.httpserver.HTTPServer(app)
    http_server.listen(port=23333, address='0.0.0.0')
    tornado.ioloop.IOLoop.instance().start()
