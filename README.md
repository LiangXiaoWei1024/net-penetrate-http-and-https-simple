## 內网穿透工具(net-penetrate)，跨平台兼容(windows,mac)
---
## 技术交流QQ群:704592910
---

## 介绍

- 完全开源,免费使用，不会有人窃取你的信息(可看源码)，有需要的可以自己下载源码独立部署使用。
- 全新GUI界面，操作简单，一键安装，这是一个托盘系统要是觉得窗口碍事可以随时关闭打开
- 为你穿透端口绑定域名，不再是IP+端口裸奔访问
- 支持http/https等多种模式使用客户端网络代理上网，家里轻松访问公司网络
- 支持GET | POST | PUT | PATCH | DELETE
- 多用户支持，同时满足多人日常穿透需求
- 支持正常的数据交互，上传下载文件，重定向等等。
- 内置日志模块，方便开发测试。
- 常见应用：开发调试， 支付接口回调、微信接口、个人电脑搭建网站，远程办公等等。
- 支持自定义二级域名
## 原理

- 利用服务器，和本地电脑，建立一条专属的通道，并将外网的请求转发到本地，从而实现穿透
- 基于socket通讯技术，支持断线重连

## 示例

1. 设置二级域名，可以自定义，也可以使用默认值(方便)。
2. 填写需要穿透的IP(默认127.0.0.1)与端口(默认8080)。
3. 看到日志**连接成功**,点击启动
4. 看到日志中**启动成功**，并且明确的告诉你外网请求地址，就可以开始使用了。
5. 本地地址为： http://127.0.0.1:20001/ 远程地址为http://test.kele.plus/ 当访问http://test.kele.plus/ 的时候，请求会被转发到http://127.0.0.1:20001/ 实现内网可外网访

![图1](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple/blob/master/image/examples_1.png?raw=true)


系统托盘，windows的又下角的位置

![在这里插入图片描述](https://img-blog.csdnimg.cn/6f0ba1fbb50d4ac78a99e7d382a731e6.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5Y-v5LmQX3Z2,size_20,color_FFFFFF,t_70,g_se,x_16)

## 附加小功能，右键日志页面，方便大家调试使用

![在这里插入图片描述](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple/blob/master/image/examples_4.png?raw=true)


> 源码&下载地址 [https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple)

大家有什么问题可以加入到QQ群，有什么需要改进以及需要新增的功能大家一起交流。

## 安装教程

### Windows
![在这里插入图片描述](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple/blob/master/image/install_windows_1.png?raw=true)
![在这里插入图片描述](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple/blob/master/image/install_windows_2.png?raw=true)
![在这里插入图片描述](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple/blob/master/image/install_windows_3.png?raw=true)

### Mac
![在这里插入图片描述](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple/blob/master/image/install_mac_1.png?raw=true)
![在这里插入图片描述](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple/blob/master/image/install_mac_2.png?raw=true)
![在这里插入图片描述](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple/blob/master/image/install_mac_4.png?raw=true)

- 出现下面的问题，打开终端 进入当前目录， chmod +x penetrate-start.command
![在这里插入图片描述](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple/blob/master/image/install_mac_3.png?raw=true)



## 问题

- 如果重定向的地址是外网无法访问的地址是用不了的。
- 如果是一个地址是入口，随后加载的css,js什么东西也要带上映射名称才能访问(如图)

![在这里插入图片描述](https://wxlc.oss-cn-shenzhen.aliyuncs.com/491650887818_.pic_hd.jpg)
