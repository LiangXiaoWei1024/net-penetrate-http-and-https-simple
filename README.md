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

## 原理

- 利用服务器，和本地电脑，建立一条专属的通道，并将外网的请求转发到本地，从而实现穿透
- 基于socket通讯技术，支持断线重连

## 示例

2. 设置映射名称，可以自定义，也可以使用默认值(方便)。
3. 填写需要穿透的IP(默认127.0.0.1)与端口(默认8080)。
4. 点击启动
5. 看到图片中**启动成功**，并且明确的告诉你外网请求地址转到内网那个地址，就可以开始使用了。
6. 本地地址为： http://127.0.0.1:20001/ 远程地址为http://101.35.221.134:80/cimjfYUW/ 当访问http://101.35.221.134:80/cimjfYUW/
   的时候，请求会被转发到http://127.0.0.1:20001/ 实现内网可外网访
7. 图1 **过滤映射名称**，图2 **未过滤映射名称**

![图1](https://img-blog.csdnimg.cn/9f54039063954f259e14560a216210e1.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5Y-v5LmQX3Z2,size_19,color_FFFFFF,t_70,g_se,x_16)

![图2](https://img-blog.csdnimg.cn/100e4ba39ac146209e7414e6fddae20f.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5Y-v5LmQX3Z2,size_19,color_FFFFFF,t_70,g_se,x_16)

系统托盘，windows的又下角的位置

![在这里插入图片描述](https://img-blog.csdnimg.cn/6f0ba1fbb50d4ac78a99e7d382a731e6.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5Y-v5LmQX3Z2,size_20,color_FFFFFF,t_70,g_se,x_16)

## 附加小功能，右键日志页面，方便大家调试使用

![在这里插入图片描述](https://img-blog.csdnimg.cn/bd1e74106348428ab741a0de6fa5e522.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA5Y-v5LmQX3Z2,size_20,color_FFFFFF,t_70,g_se,x_16)


> 源码&下载地址 [https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple](https://github.com/LiangXiaoWei1024/net-penetrate-http-and-https-simple)

大家有什么问题可以加入到QQ群，有什么需要改进以及需要新增的功能大家一起交流。

## 问题

- 如果重定向的地址是外网无法访问的地址是用不了的。
- 如果是一个地址是入口，随后加载的css,js什么东西也要带上映射名称才能访问(如图)

![在这里插入图片描述](https://wxlc.oss-cn-shenzhen.aliyuncs.com/491650887818_.pic_hd.jpg)

## 赞助💰

如果你觉得对你有帮助，你可以赞助我们一杯咖啡，鼓励我们继续开发维护下去。

![在这里插入图片描述](https://wxlc.oss-cn-shenzhen.aliyuncs.com/481650887363_.pic_hd.jpg)
