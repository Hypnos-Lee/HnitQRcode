# HnitQRcode
用于生成湖工一卡通二维码，解决带卡的麻烦

## 自定义用户数据方法

1.电脑浏览器打开并登录 [校园一卡通](http://59.51.114.201:8080/easytong_webapp/index.html#/virtualCard)

2.F12 打开浏览器的开发者模式，并选择 NetWork

3.点击 “刷新二维码” 按钮

4.找到 getH5QRCode 请求

5.往下翻找到 AccNum,Time,Sign 三个参数

6.长按软件的“关于”按钮，将自己的数据填入，保存

