# SDK说明
三诺微信蓝牙血糖仪连接微信后，服务器解析数据协议sdk（for JAVA）
# SDK文件
sdk目录,需手动sdk下载并引入自己的项目（目前未上传到maven中央仓库）
# 设备命令
上传实时数据： 04

上传历史数据： 0e

开始测量命令： 03

同步时间命令： 06

# sdk使用流程
背景说明：血糖仪通过微信同第三方通信，并且接受第三方的响应指令；所有的设备必须到微信iot平台进行授权，并保存好相应的deviceId与sn之间的关系，详见微信iot平台：http://iot.weixin.qq.com/wiki/new/index.html?page=3-4-6



