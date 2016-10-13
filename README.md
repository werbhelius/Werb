# 业余时间写了一个第三方微博（不使用官方SDK）

### 问题说明
* 目前sina规定只能设置15个测试账号，而且现在单单提供测试账号貌似有很多问题，我建议你：
1. 自己去新浪新建一个应用，
2. 把我的项目通过签名生成工具生成签名，
3. 把包名和签名配置到sina的应用上，
4. 替换自己的appkey，这样就可以自己玩了，这是最保险也是最好的方法
5. 授权是出现文件不存在c8998，重新下载新浪的微博签名设置签名就好啦

### Tips

* 前段时间，想要写一个练手的项目，所以决定开发一个第三方微博 App
* 分析 Sina WeiboSDK源码，发现官方Api请求在实现、代码风格上存在很多弊端
* 本项目中所有的请求，都是利用 RxJava + Retrofit 重新封装请求，并实现无网缓存
* 采用 MVP 设计模式，充分解耦，在代码阅读、后期维护上有很好的体验
* 由于新浪暂停对第三方微博的审核，所以未能通过应用审核，获取更多权限，来完成后续开发，当然也是不能直接运行的
* 所以想要运行项目的同学，可以替换成自己的新浪APP key，或者联系我给你添加测试账号
* 分享的目的是，我觉得项目中功能还是很不错的，希望可以给予大家一些参考
* GitHub 项目地址 : [https://github.com/Werb/Werb](https://github.com/Werb/Werb)



### 微博主界面

* 实现微博主页信息流浏览、可以在每条微博中，实现点赞，评论，转发功能
* 使用自定义 View 实现九宫格图片显示，同时点击浏览图片，支持左右滑动
* 支持下拉刷新，上滑加载，快速回到顶端
* 闪屏界面做了类似于 Twitter 闪屏界面的动画处理
* 其中对微博正文的文字处理，我觉得是比较有意思的地方，通过正则表达式匹配出 @ ，# ，url，做不同的颜色及点击处理

![main](https://raw.githubusercontent.com/Werb/Werb/master/screenshots/werb_main.png)

### 微博详情界面

* 在主页点击微博，可以进入详情界面
* 实现了对该条微博的转发，评论功能，同时还可以对该微博下的评论进行回复，转发操作
* 利用 CoordinatorLayout 实现父布局与子布局嵌套滑动
* 由于微博接口限制，这里只能获取微博下的评论数据，转发和点赞是获取不到的，但在代码编写上三者几乎相同，只是请求的接口不同



![detail](https://raw.githubusercontent.com/Werb/Werb/master/screenshots/werb_detail.png)

### 微博个人主页

* 显示用户的粉丝数，关注数，微博数
* 查看个人相册，个人收藏，好友列表（包括粉丝，关注，互粉）
* 由于微博接口限制，只能获取部分数据，忍不住吐槽一下，真是太坑了...
* 相册是通过个人界面中获取的微博信息，提取出图片参数，自己做处理的，新浪并没有提供获取相册列表的接口

![user](https://raw.githubusercontent.com/Werb/Werb/master/screenshots/werb_user.png)

![收藏和相册](https://raw.githubusercontent.com/Werb/Werb/master/screenshots/werb.png)

### 发微博界面

* 通过自定义View，实现emoji表情的添加，采用 ViewPager + GridView，具体请看代码实现
* 支持 TAG 话题添加
* 通过 TextWatcher 实现输入字符监听
* 定位和 @ 还没有实现

![send_weibo](https://raw.githubusercontent.com/Werb/Werb/master/screenshots/werb_send.png)

### 消息界面
* 实现了微博 @ ，评论 @ ，收到的评论，发出的评论四大功能
* 点击消息图片可以快捷回复
* 点击整个 item 可以跳转到微博详情界面

![message](https://raw.githubusercontent.com/Werb/Werb/master/screenshots/werb_comment.png)

### 闲聊一下
* 这个项目我会继续做的，但由于接口的限制，进度应该会很缓慢，有好想法的同学可以联系我，我们来一起做
* 想学习 MVP + RxJava + Retrofit 的同学，可以看一下我的这个项目
* [实践！业余时间做的一款阅读类App （MVP + RxJava + Retrofit）](https://github.com/Werb/GankWithZhihu)
* 单纯的使用 MVP + RxJava + Retrofit 构建一个项目，没有其他复杂的功能，很适合学习
* 欢迎 Star 和 Fork

### License
* 同时希望可以帮助到其他人


                Copyright 2016 Werb

                Licensed under the Apache License, Version 2.0 (the "License");
                you may not use this file except in compliance with the License.
                You may obtain a copy of the License at

                http://www.apache.org/licenses/LICENSE-2.0

                Unless required by applicable law or agreed to in writing, software
                distributed under the License is distributed on an "AS IS" BASIS,
                WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                See the License for the specific language governing permissions and
                limitations under the License.



### Contact Me
* Email: 1025004680@qq.com
* Blog : [Werb's blog](http://werb.github.io/)
* Weibo: [UMR80](http://weibo.com/singerwannber )
* GitHub: [Werb](https://github.com/Werb)
