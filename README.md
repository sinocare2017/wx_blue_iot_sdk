# SDK说明
三诺微信蓝牙血糖仪连接微信后，服务器解析数据协议sdk（for JAVA）
# SDK文件
sdk目录,需手动sdk下载并引入自己的项目（目前未上传到maven中央仓库）
# 设备命令
上传实时数据： 04

上传历史数据： 0e

开始测量命令： 03

同步时间命令： 06

# 背景说明
背景说明：血糖仪通过微信同第三方通信，并且接受第三方的响应指令
所有的蓝牙血糖仪设备必须到微信iot平台进行授权，并在本地保存好相应的deviceId与sn之间的关系，详见微信iot平台：http://iot.weixin.qq.com/wiki/new/index.html?page=3-4-6
（微信iot平台接入请参照微信iot开发文档：http://iot.weixin.qq.com/wiki/new/index.html?page=2-1）

一切就绪后，打开手机蓝牙->打开微信公众号->打开血糖仪设备；如若连上，则微信会推送一条(03命令)到自己的服务器：
微信推送数据格式为：http://iot.weixin.qq.com/wiki/new/index.html?page=3-4-1

# SDK使用
      ## sdk相关类说明
      DataPacketParser  协议解析器
      DeviceDataWrapper 设备数据协议包
      Command 设备命令
      GlucoseData 血糖数据

## 解析微信设备事件数据（解析微信事件xml后获得deviceId和Content）
            (deviceId与sn匹配，deviceId是在微信设备授权时候获得的；所以理论上需要存储deviceId与sn的匹配关系)
            假如：DeviceID：gh_d56fe15a43c0_2c0cf8ae16d077e1 Content：U04QAAQEEQQLEBskADYAAM0Yog==<br>
           
            /**解析协议，转换为数据对象*/
            DeviceDataWrapper dataWrapper = DataPacketParser.parseDeviceContent(sn, deviceContext);
            /**具体命令*/
            String command=dataWrapper.getCommand();
            /**根据相应处理命令，创建响应报文：*/
            String responseData =DataPacketParser.createResponseContent(dataWrapper);
            /**获取测量数据*/
            GlucoseData glucoseData = dataWrapper.getGlucoseData();
            
  注意：只有当command命令为04和0e时，才有具体的血糖数据，04命令处理完后，立马再次同步一条时间，具体见demo；所有响应给设备的数据，必须通过微信iot平台“主动发送消息给设备”接口调用，详见：http://iot.weixin.qq.com/wiki/new/index.html?page=3-4-3
   
 

