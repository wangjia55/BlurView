# BlurView
高斯模糊效果实现方案及性能对比（Gaussian Blur）,
本项目主要使用2种方式实现高斯模糊：
1. 官方API，使用RenderScript进行实现，但是实现效率不是很高。
2. Java实现, 原理是：先缩小图片，进行模糊处理，再放大到原来尺寸，速度快，但不适合非常大的图片。

## Blog
http://blog.csdn.net/wangjia55/article/details/45742371

## 效果图
![icon](https://github.com/wangjia55/BlurView/blob/master/screen_1.png)
