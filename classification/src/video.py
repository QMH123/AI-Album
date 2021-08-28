import os
import moviepy
import moviepy.video.io.ImageSequenceClip
from moviepy.editor import *
from moviepy.video.VideoClip import TextClip
from PIL import Image
import requests as req
from io import BytesIO
import base64
import random

def url2img(file):
    frames_dir = 'D://img/img/'
    # 图片的base64编码
    response = req.get(file)
    filename = file.split("/")[-1]
    filepath = frames_dir + filename
    # print(filename)
    ls_f = base64.b64encode(BytesIO(response.content).read())
    # base64编码解码
    imgdata = base64.b64decode(ls_f)
    # 图片文件保存
    file = open(filepath,'wb')
    file.write(imgdata)
    file.close()

def setpic_pixel(filename,maxw,maxh):
    im = Image.open(filename)
        #im = im.resize((1024,48),PIL.Image.ANTIALIAS)
        # 制作最大尺寸 1024*48 背景用白色填充
    newim = Image.new('RGB', (maxw, maxh), 'white')
    w, h = im.size
    scale= min(maxw/w,maxh/h)
    print(w,h)
    out = im.resize((int(w*scale),int(h*scale)))
    newim.paste(out,(0,0))
    # newim.show()
    newim.save(filename)

def angleF(t):
    ret = [0 ,20 ,-20]
    return ret[random.randint(0,2)]

def pics2video(frames_dir, video_dst, fps=0.5):
    frames_name = sorted(os.listdir(frames_dir))
    frames_path = [frames_dir+frame_name for frame_name in frames_name]
    for image in frames_path:
        setpic_pixel(image,800,600)  
    clip = moviepy.video.io.ImageSequenceClip.ImageSequenceClip(frames_path, fps)
    clip = clip.rotate(angleF,expand=True).fx(vfx.resize,(764,764))
    clip.write_videofile(video_dst, codec='libx264')

def editVideo(video_dst,music_id,title):
    #需添加背景音乐的视频
    video_clip = VideoFileClip(video_dst)
    #提取视频对应的音频，并调节音量
    #video_audio_clip = video_clip.audio.volumex(0.5)

    # 制作文字，指定文字大小和颜色
    txt_clip = (TextClip(title, color='white', align='West', font='simhei.ttf', fontsize=40, method='label')
                .set_position((0.5,0.2),relative=True)
                #.set_position("center")
                #.set_position(("center", "top"))
                # .set_position(lambda t: (150*t, 50*t))  # 随着时间移动
                .set_duration(video_clip.duration))  # 水印持续时间

    video_clip = CompositeVideoClip([video_clip, txt_clip])  # 在视频上覆盖文本
    # result.write_videofile("/home/huangjx/视频/heng_5_sec.mp43.mp4", fps=25)  # fps:视频文件中每秒的帧数

    #背景音乐
    music_dir = 'D://img/music/' 
    music_list = sorted(os.listdir(music_dir))
    music = music_dir + music_list[music_id]
    audio_clip = AudioFileClip(music).volumex(0.8)
    #设置背景音乐循环，时间与视频时间一致
    audio = afx.audio_loop( audio_clip, duration=video_clip.duration)
    #视频声音和背景音乐，音频叠加
    #audio_clip_add = CompositeAudioClip([video_audio_clip,audio])

    #视频写入背景音
    final_video = video_clip.set_audio(audio)

    #将处理完成的视频保存
    final_video.write_videofile(video_dst)
    #final_video.ipython_display(width=280)
