package com.sinocare.iotsdkdemo;

import com.sinocare.iotsdk.DataPacketParser;
import com.sinocare.iotsdk.dto.Command;
import com.sinocare.iotsdk.dto.DeviceDataWrapper;
import com.sinocare.iotsdk.dto.GlucoseData;

/**
 * Created with IntelliJ IDEA.
 *
 * @Package:com.sinocare.iotsdk_demo
 * @author: jerry
 * @Date: 2017/12/7
 * @Time: 15:41
 * Description:
 */
public class DemoTester {

    public static void main(String[] args) throws InterruptedException {
        /**假设设备消息内容为：**/
        String deviceId="gh_d56fe15a43c0_2c0cf8ae16d077e1";
        String sn="1AG993R2231";
        String deviceContext = "U04QAAQEEQQLEBskADYAAM0Yog==";
        handleWxDeviceEvent(sn,deviceContext);
    }

    /**
     * 血糖仪通过微信同第三方通信，并且接受第三方的响应指令
     * deviceId与sn匹配，deviceId是在微信设备授权时候获得的；所以理论上需要存储deviceId与sn的匹配关系
     * 微信推送的设备数据结构为：
     *  <xml>
         <ToUserName><![CDATA[%s]]></ToUserName>
         <FromUserName><![CDATA[%s]]></FromUserName>
         <CreateTime>%u</CreateTime>
         <MsgType><![CDATA[%s]]></MsgType>
         <DeviceType><![CDATA[%s]]></DeviceType>
         <DeviceID><![CDATA[%s]]></DeviceID>
         <Content><![CDATA[%s]]></Content>  //设备消息内容
         <SessionID>%lu</SessionID>
         <MsgID>%lu</MsgID>
         <OpenID><![CDATA[%s]]></OpenID>
         </xml>
     **/
    private  static  void handleWxDeviceEvent(String sn,String deviceContext){
        //解析设备数据：必须按照以下步骤，否则会有问题
        DeviceDataWrapper dataWrapper = DataPacketParser.parseDeviceContent(sn, deviceContext);
        if(dataWrapper!=null && dataWrapper.getCommand()!=null && dataWrapper.getPacket()!=null){
            String command=dataWrapper.getCommand();
            /**createResponseContent此方法回依据不同命令，创建不同的响应报文*/
            String responseData = DataPacketParser.createResponseContent(dataWrapper);
            /**开始测量命令*/
            if(Command.START_TEST_COMMAND.equals(command) || Command.SYNC_TIME_COMMAND.equals(command) ){
                //调用微信设备平台接口，同步时间到设备
                responseDataToDevice(command,responseData);
            }else if(Command.CURRENT_RESULT_COMMAND.equals(command) || Command.HISTORY_RESULT_COMMAND.equals(command) ){
                /**实时数据或者历史数据命令，立即响应*/
                responseDataToDevice(command,responseData);
                /**获取血糖值*/
                GlucoseData glucoseData = dataWrapper.getGlucoseData();
                System.out.println(glucoseData.getTestTime());
                System.out.println(glucoseData.getTestResult());
                /**实时数据，再次同步时间*/
                if(Command.CURRENT_RESULT_COMMAND.equals(command)){
                    dataWrapper.setCommand(Command.SYNC_TIME_COMMAND);
                    responseData = DataPacketParser.createResponseContent(dataWrapper);
                    responseDataToDevice(command,responseData);
                }
            }
        }
    }

    /***
     * 调用微信设备平台接口(伪代码，请自行实现)
     * @param responseData
     */
    private static void responseDataToDevice(String cmd,String responseData){
        System.out.println("cmd:"+cmd+" responseData:"+responseData);
    }
}