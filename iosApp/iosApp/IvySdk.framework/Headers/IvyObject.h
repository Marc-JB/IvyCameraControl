//
//  IvyObject.h
//  IvySdk
//
//  Created by JackChan on 1/7/2019.
//  Copyright © 2019 ivyiot. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface IvyObject : NSObject

@end



@interface IvyDevAbility : IvyObject

@property (nonatomic, assign) NSInteger val0;
@property (nonatomic, assign) NSInteger val1;
@property (nonatomic, assign) NSInteger val2;
@property (nonatomic, assign) NSInteger val3;
@property (nonatomic, assign) NSInteger val4;
@property (nonatomic, assign) NSInteger val5;
@property (nonatomic, assign) NSInteger val6;
@property (nonatomic, assign) NSInteger val7;
@property (nonatomic, assign) NSInteger val8;

@end


@interface IvyDevAbility (Helper)

/// 是否支持 sd 卡
@property (nonatomic, assign, readonly) BOOL enableSD;

/// 是否支持 outdoor
@property (nonatomic, assign, readonly) BOOL enableOutdoor;

/// 是否支持 pt
@property (nonatomic, assign, readonly) BOOL enablePT;

/// 是否支持 zoom
@property (nonatomic, assign, readonly) BOOL enableZoom;

/// 是否支持 rs485
@property (nonatomic, assign, readonly) BOOL enableRS485;

/// 是否支持 ioAlarm
@property (nonatomic, assign, readonly) BOOL enableIOAlarm;

/// 是否支持 onvif
@property (nonatomic, assign, readonly) BOOL enableOnvif;

/// 是否支持 p2p
@property (nonatomic, assign, readonly) BOOL enableP2P;

/// 是否支持 wps
@property (nonatomic, assign, readonly) BOOL enableWPS;

/// 是否支持 监听（设备提示音量调节）
@property (nonatomic, assign, readonly) BOOL enableAudio;

/// 是否支持 对讲
@property (nonatomic, assign, readonly) BOOL enableTalk;

/// 是否支持 全双工对讲
@property (nonatomic, assign, readonly) BOOL enableDuplexVoice;

/// 是否支持 视频参数网络自适应
@property (nonatomic, assign, readonly) BOOL enableNetworkAdapter;

/// 是否支持 分辨率切换和主次码流切换
/// @note 为NO: 只支持主次码流切换
@property (nonatomic, assign, readonly) BOOL enableStreamMode;

/// 是否支持 区域选择
@property (nonatomic, assign, readonly) BOOL enableMotionArea;

/// 是否支持 一键呼叫
@property (nonatomic, assign, readonly) BOOL enableOneKeyCall;

/// 是否支持 夜视计划
@property (nonatomic, assign, readonly) BOOL enableNightVisionSchedule;

/// 是否支持 SD卡图片下载
@property (nonatomic, assign, readonly) BOOL enableSDCardPictureDownload;

/// 是否支持 dropbox 云存储
/// @note NO表示支持百度云存储（fosbaby内外销系列都只支持百度云存储）
@property (nonatomic, assign, readonly) BOOL enableDropboxStorage;

/// 是否支持 百度推送
@property (nonatomic, assign, readonly) BOOL enableBaiduPush;

/// 是否支持 Foscam 推送
@property (nonatomic, assign, readonly) BOOL enableFoscamPush;

/// 是否支持 Foscam 存储
@property (nonatomic, assign, readonly) BOOL enableFoscamStorage;

/// 是否支持 Foscam 富媒体推送
@property (nonatomic, assign, readonly) BOOL enableFoscamRichPush;

/// 是否支持 Alexa
@property (nonatomic, assign, readonly) BOOL enableAlexa;

/// 是否支持 7*24 小时云存储
@property (nonatomic, assign, readonly) BOOL enable24HStorage;

/// 是否支持 告警录像时长设置
@property (nonatomic, assign, readonly) BOOL enableRecordTimeSet;

/// 是否支持 APP 预置位巡航
@property (nonatomic, assign, readonly) BOOL enablePresetCruise;

/// 是否支持 框外侦测
@property (nonatomic, assign, readonly) BOOL enableOutSideDetect;

/// 是否支持 声音侦测
@property (nonatomic, assign, readonly) BOOL enableAudioDetect;

/// 是否支持 温度侦测
@property (nonatomic, assign, readonly) BOOL enableTemperatureDetect;

/// 是否支持 湿度侦测
@property (nonatomic, assign, readonly) BOOL enableHumidityDetect;

/// 是否支持 PIR侦测
@property (nonatomic, assign, readonly) BOOL enablePIRDetect;

/// 是否支持 IV侦测
@property (nonatomic, assign, readonly) BOOL enableIVDetect;

/// 是否支持 人体侦测--爱华盈通
@property (nonatomic, assign, readonly) BOOL enableHumanDetect_aiwinn;

/// 是否支持 人体侦测--珊口
@property (nonatomic, assign, readonly) BOOL enableHumanDetect_sanko;

/// 是否支持 欧姆龙行人检测
@property (nonatomic, assign, readonly) BOOL enableOmRonDetect;

/// 是否支持 镜头畸变
@property (nonatomic, assign, readonly) BOOL enableCameraDistortion;

/// 是否支持 留言开关
@property (nonatomic, assign, readonly) BOOL enableLeaveMsg;

/// 录像时长标志位
/// 是否支持设备报警录像时长设置, 并且不区分报警类型（移动/PIR/人形等等，目前Foscam不区分，但是IVY设备区分，目前IVY设备只有PIR报警支持录像报警时长设置）
@property (nonatomic, assign, readonly) BOOL enableVideodDration;

/// 是否支持 自定义报警音
@property (nonatomic, assign, readonly) BOOL enableAlarmTone;

/// 是否支持 Siren联动
@property (nonatomic, assign, readonly) BOOL enableSiren;

/// 是否支持 人形跟踪
@property (nonatomic, assign, readonly) BOOL enableSmartTracking;

/// 是否支持 预置点
@property (nonatomic, assign, readonly) BOOL enablePreset;

/// 是否支持 双光源
@property (nonatomic, assign, readonly) BOOL enableDoubleLight;

/// 是否支持 巡航
@property (nonatomic, assign, readonly) BOOL enableCruise;

/// 是否支持 童谣
@property (nonatomic, assign, readonly) BOOL enableBabyMusic;

/// 是否支持 芯片加密
@property (nonatomic, assign, readonly) BOOL enableChipEncrypt;

/// 是否支持 SoftAP
@property (nonatomic, assign, readonly) BOOL enableSoftAP;

/// 是否支持 EZLink
/// @note 不区分芯片，不表示是否支持IPC扫码
@property (nonatomic, assign, readonly) BOOL enableEZLink;

/// 是否支持 PPPoE
@property (nonatomic, assign, readonly) BOOL enablePPPoE;

/// 是否支持 夜视红外功能
@property (nonatomic, assign, readonly) BOOL enableNightIRCut;

/// 是否支持 白光灯功能
@property (nonatomic, assign, readonly) BOOL enableWhiteLight;

/// 是否支持 录像功能
@property (nonatomic, assign, readonly) BOOL enableRecord;

/// 是否支持 定时重启
@property (nonatomic, assign, readonly) BOOL enableTimingReset;

/// 是否支持 布撤防
@property (nonatomic, assign, readonly) BOOL enableDefend;

/// 是否支持  指示灯开关
@property (nonatomic, assign, readonly) BOOL enableLEDLight;

/// 是否支持 隐私遮挡
@property (nonatomic, assign, readonly) BOOL enablePrivacyMask;

/// 是否支持 小夜灯开关
@property (nonatomic, assign, readonly) BOOL enableNightLight;

/// 是否支持 扬声器音量控制
@property (nonatomic, assign, readonly) BOOL enableVolume;

/// 是否支持 镜头遮挡
@property (nonatomic, assign, readonly) BOOL enableLensCover;

/// 是否支持 响铃
@property (nonatomic, assign, readonly) BOOL enableRingBell;

/// 是否支持 1V1 PIR灵敏度调节
@property (nonatomic, assign, readonly) BOOL enablePir1V1;

/// 是否支持 1V3PIR灵敏度调节
@property (nonatomic, assign, readonly) BOOL enablePir1V3;

/// 是否支持 白光灯亮度调节
@property (nonatomic, assign, readonly) BOOL enablePirBrightness;

/// 是否支持 HDR
@property (nonatomic, assign, readonly) BOOL enableHDR;

/// 是否支持 WDR
@property (nonatomic, assign, readonly) BOOL enableWDR;

/// 是否支持 语音提示开关
@property (nonatomic, assign, readonly) BOOL enableVoiceTips;

/// 是否支持 OSD显示设备名
@property (nonatomic, assign, readonly) BOOL enableOSDName;

/// 是否支持 OSD显示时间戳
@property (nonatomic, assign, readonly) BOOL enableOSDTime;

/// 是否支持 图像镜像
@property (nonatomic, assign, readonly) BOOL enableMirror;

/// 是否支持 图像翻转
@property (nonatomic, assign, readonly) BOOL enableFlip;

/// 是否支持 欧姆龙人脸检测算法
@property (nonatomic, assign, readonly) BOOL enableOmronFace;

/// 是否支持 神马全双工算法
@property (nonatomic, assign, readonly) BOOL enableShenMaDuplexVoice;

/// 是否支持 双马达自动聚焦
@property (nonatomic, assign, readonly) BOOL enable2MotorsAutoFocus;

/// 是否支持 sd卡格式化
@property (nonatomic, assign, readonly) BOOL enableSDCardFormat;

/// 是否支持 重对焦
@property (nonatomic, assign, readonly) BOOL enableRefocus;

/// 是否支持 支持NVR关联H265优化
@property (nonatomic, assign, readonly) BOOL enableNVRAssociate;

/// 是否支持 云台巡航看守位
@property (nonatomic, assign, readonly) BOOL enablePTCruise;

/// 是否支持 设备名称Unicode
@property (nonatomic, assign, readonly) BOOL enableUnicodeDevName;

/// 是否支持 3个等级灵敏度
@property (nonatomic, assign, readonly) BOOL enableSensitity3;

/// 是否支持 2个侦测区域选择
@property (nonatomic, assign, readonly) BOOL enableDetectionArea2;

/// 是否支持  SD卡录像时间轴功能
@property (nonatomic, assign, readonly) BOOL enableTimeline;

/// 是否支持 设备休眠
@property (nonatomic, assign, readonly) BOOL enableSleep;

/// 是否支持 SD卡计划录像
@property (nonatomic, assign, readonly) BOOL enableSDCardRecordingSchedule;

/// 是否支持 设备支持休眠功能并且可以设置获取计划时间/模式/状态
@property (nonatomic, assign, readonly) BOOL enableScheduleSleep;

/// 是否支持 云平台和SD卡报警录像是否录制音频
@property (nonatomic, assign, readonly) BOOL enableSDCardAlarmVoice;

/// 是否支持 白光灯计划设置
@property (nonatomic, assign, readonly) BOOL enableWihtelightSchedule;

/// 是否支持 声音侦测支持联动
@property (nonatomic, assign, readonly) BOOL enableSoundDetectSupportWhiteLightLinkage;

/// 是否支持 夏令时
@property (nonatomic, assign, readonly) BOOL enableDST;

/// 是否支持 时间NTP服务器
@property (nonatomic, assign, readonly) BOOL enableNTPServer;

/// 是否支持 PTZ恢复
@property (nonatomic, assign, readonly) BOOL enablePTZReset;

/// 是否支持 电源频率
@property (nonatomic, assign, readonly) BOOL enablePowerFrequency;

/// 是否支持 手动打开关闭夜视
@property (nonatomic, assign, readonly) BOOL enableIPCIRSettingSchedule;

/// 是否支持 移动侦测计划时间设置
@property (nonatomic, assign, readonly) BOOL enableAlertSchedule;

/// 是否支持 移动侦测中区域设置
@property (nonatomic, assign, readonly) BOOL enableSetMotionArea;

/// 是否支持 移动侦测中时间间隔设置
@property (nonatomic, assign, readonly) BOOL enableAlertInterval;

/// 是否支持 告警推送间隔
@property (nonatomic, assign, readonly) BOOL enableAlarmPushInterval;

/// 是否支持 Kvs
@property (nonatomic, assign, readonly) BOOL enableKvs;

/// 是否支持 设置PTZ速度
@property (nonatomic, assign, readonly) BOOL enablePTZSpeed;

/// 是否支持 当启用ALEAX功能，设备自动切换到H264，关闭切换到H265
@property (nonatomic, assign, readonly) BOOL enableAlexaSwitchH264_H265;

/// 是否支持 自动全彩黑白模式
@property (nonatomic, assign, readonly) BOOL enableLightVisionSwitch;

/// 是否支持  PIR侦测中支持设置录像报警时长
@property (nonatomic, assign, readonly) BOOL enablePIRDetectVideodDration;

/// 是否支持  支持连续报警录像到一个文件
@property (nonatomic, assign, readonly) BOOL enableIVYDeviceDynamicRecord;

/// 是否支持 自动/全彩/黑白/关闭所有 夜视模式
@property (nonatomic, assign, readonly) BOOL enableCloseLightVisionSwitch;

/// 是否不支持 夜视计划
@property (nonatomic, assign, readonly) BOOL disableIRLed;

/// 是否支持 手动白光灯
@property (nonatomic, assign, readonly) BOOL enableManualWhiteLight;

/// 是否支持 白光灯联动
@property (nonatomic, assign, readonly) BOOL enableLinkageWhiteLight;

/// 是否支持  SD卡状态
@property (nonatomic, assign, readonly) BOOL enableSDStatus;

@end



@interface IvyDevInfo : NSObject

/// 设备类型
@property (nonatomic, assign) NSInteger devType;

/// 平台类型
@property (nonatomic, assign) NSInteger platType;

/// sensor类型
@property (nonatomic, assign) NSInteger sensorType;

/// wifi类型
@property (nonatomic, assign) NSInteger wifiType;

/// 语言
@property (nonatomic, assign) NSInteger language;

/// 产品名字
@property (nonatomic, strong) NSString *productName;

/// 设备名
@property (nonatomic, strong) NSString *devName;

/// 应用固件版本号
@property (nonatomic, strong) NSString *firmwareVersion;

/// 系统固件版本号
@property (nonatomic, strong) NSString *hardwareVersion;

/// 序列号
@property (nonatomic, strong) NSString *serialNo;

/// 设备UID
@property (nonatomic, strong) NSString *uid;

/// MAC 地址
@property (nonatomic, strong) NSString *mac;

/// OEM 码
@property (nonatomic, assign) NSInteger oemCode;

@end



@interface IvyDevLan : IvyObject

/// Mac地址
@property (nonatomic, strong) NSString *mac;

/// 设备名
@property (nonatomic, strong) NSString *name;

/// IP地址
@property (nonatomic, strong) NSString *ip;

/// mask
@property (nonatomic, assign) NSInteger mask;

/// NDS
@property (nonatomic, assign) NSInteger dns;

/// 类型 0:FOSIPC_H264 1:FOSIPC_MJ 2: FOSIPC_UNKNOW
@property (nonatomic, assign) NSInteger type;

/// 端口
@property (nonatomic, assign) NSInteger port;

/// DHCP 是否开启
@property (nonatomic, assign) NSInteger dhcp_enabled;

/// 设备UID
@property (nonatomic, strong) NSString *uid;

@end



typedef NS_ENUM(NSInteger, IvyMotionDetectType) {
    IvyMotionDetectType0,
    IvyMotionDetectType1,
};

/// 移动侦测告警协议
@protocol IvyMotionDetect <NSObject>

/// 移动侦测告警类类型
@property (nonatomic, assign, readonly) IvyMotionDetectType type;

@end



@interface IvyMotionDetectObject0 : IvyObject <IvyMotionDetect>

/// 开关
/// @note 0:关 1:开
@property (nonatomic, assign) NSInteger isEnable;

/// 移动侦测开关
/// @note 0:关 1:开
@property (nonatomic, assign) NSInteger isMovAlarmEnable;

/// PIR侦测开关
/// @note 0:关 1:开
@property (nonatomic, assign) NSInteger isPirAlarmEnable;

/// 联动
/// @note Bit0:Ring Bit1:发送邮件 Bit2:截图 Bit3:录像 Bit4:Io输出 Bit5:截图上云 Bit6:录像上云 Bit7:手机推送
@property (nonatomic, assign) NSInteger linkage;

/// 抓拍间隔秒
@property (nonatomic, assign) NSInteger snapInterval;

/// 敏感度
@property (nonatomic, assign) NSInteger sensitivity;

/// 触发间隔
@property (nonatomic, assign) NSInteger triggerInterval;

/// 周日
/// @note bit0-bit47表示一天，每个bit表示半个小时
@property (nonatomic, assign) long long schedule0;

/// 周一
@property (nonatomic, assign) long long schedule1;

/// 周二
@property (nonatomic, assign) long long schedule2;

/// 周三
@property (nonatomic, assign) long long schedule3;

/// 周四
@property (nonatomic, assign) long long schedule4;

/// 周五
@property (nonatomic, assign) long long schedule5;

/// 周六
@property (nonatomic, assign) long long schedule6;

/// 侦测告警区域
/// @note 侦测告警区域被划分成N * 10个小区域，每一个area[i]表示画面上某一行的10个区域，area[i]按位表示，取值范围为0～1023
@property (nonatomic, strong) NSMutableArray *area;

@end



@interface IvyMotionDetectObject1 : IvyObject <IvyMotionDetect>

/// 开关
/// @note 0:关 1:开
@property (nonatomic, assign) NSInteger isEnable;

/// 移动侦测开关
/// @note 0:关 1:开
@property (nonatomic, assign) NSInteger isMovAlarmEnable;

/// PIR侦测开关
/// @note 0:关 1:开
@property (nonatomic, assign) NSInteger isPirAlarmEnable;

/// 联动
/// @note Bit0:Ring Bit1:发送邮件 Bit2:截图 Bit3:录像 Bit4:Io输出 Bit5:截图上云 Bit6:录像上云 Bit7:手机推送
@property (nonatomic, assign) NSInteger linkage;

/// 抓拍间隔秒
@property (nonatomic, assign) NSInteger snapInterval;

/// 触发间隔
@property (nonatomic, assign) NSInteger triggerInterval;

/// 周日
/// @note bit0-bit47表示一天，每个bit表示半个小时
@property (nonatomic, assign) long long schedule0;

/// 周一
@property (nonatomic, assign) long long schedule1;

/// 周二
@property (nonatomic, assign) long long schedule2;

/// 周三
@property (nonatomic, assign) long long schedule3;

/// 周四
@property (nonatomic, assign) long long schedule4;

/// 周五
@property (nonatomic, assign) long long schedule5;

/// 周六
@property (nonatomic, assign) long long schedule6;

/// 可指定三个侦测区域
/// 第一个区域: Rect(x0, y0, width0, height0)
/// 第二个区域: Rect(x1, y1, width1, height1)
/// 第三个区域: Rect(x2, y2, width2, height2)
@property (nonatomic, assign) NSInteger x0;
@property (nonatomic, assign) NSInteger x1;
@property (nonatomic, assign) NSInteger x2;
@property (nonatomic, assign) NSInteger y0;
@property (nonatomic, assign) NSInteger y1;
@property (nonatomic, assign) NSInteger y2;
@property (nonatomic, assign) NSInteger width0;
@property (nonatomic, assign) NSInteger width1;
@property (nonatomic, assign) NSInteger width2;
@property (nonatomic, assign) NSInteger height0;
@property (nonatomic, assign) NSInteger height1;
@property (nonatomic, assign) NSInteger height2;

/// 第一个区域的告警敏感度
@property (nonatomic, assign) NSInteger sensitivity0;

/// 第二个区域的告警敏感度
@property (nonatomic, assign) NSInteger sensitivity1;

/// 第三个区域的告警敏感度
@property (nonatomic, assign) NSInteger sensitivity2;

/// 是否开启第一个区域的侦测告警
@property (nonatomic, assign) NSInteger valid0;

/// 是否开启第二个区域的侦测告警
@property (nonatomic, assign) NSInteger valid1;

/// 是否开启第三个区域的侦测告警
@property (nonatomic, assign) NSInteger valid2;

@end



@interface IvyAudioDetect : IvyObject

/// 开关
/// @note 0:关 1:开
@property (nonatomic, assign) NSInteger isEnable;

/// 联动
/// @note Bit0:Ring Bit1:发送邮件 Bit2:截图 Bit3:录像 Bit4:Io输出 Bit5:截图上云 Bit6:录像上云 Bit7:手机推送
@property (nonatomic, assign) NSInteger linkage;

/// 敏感度
@property (nonatomic, assign) NSInteger sensitivity;

/// 触发间隔
@property (nonatomic, assign) NSInteger triggerInterval;

/// 抓拍间隔
@property (nonatomic, assign) NSInteger snapInterval;

/// 周日
/// @note bit0-bit47表示一天，每个bit表示半个小时
@property (nonatomic, assign) long long schedule0;

/// 周一
@property (nonatomic, assign) long long schedule1;

/// 周二
@property (nonatomic, assign) long long schedule2;

/// 周三
@property (nonatomic, assign) long long schedule3;

/// 周四
@property (nonatomic, assign) long long schedule4;

/// 周五
@property (nonatomic, assign) long long schedule5;

/// 周六
@property (nonatomic, assign) long long schedule6;

@end


@interface IvySDInfo : NSObject

/// 是否存在SD卡
@property (nonatomic, assign) BOOL isExist;

/// SD卡格式是否有错误 (0: 正常  其他:异常)
@property (nonatomic, assign) NSInteger sdFormatError;

/// SD的剩余空间
@property (nonatomic, assign) unsigned long long freeSpace;

/// SD卡总空间
@property (nonatomic, assign) unsigned long long totalSpace;

@end



@interface IvyDevTime : IvyObject

/// 年
@property (nonatomic, assign) NSInteger year;

/// 月
@property (nonatomic, assign) NSInteger month;

/// 日
@property (nonatomic, assign) NSInteger day;

/// 小时
@property (nonatomic, assign) NSInteger hour;

/// 分钟
@property (nonatomic, assign) NSInteger min;

/// 秒
@property (nonatomic, assign) NSInteger sec;

/// ntp服务器
@property (nonatomic, strong) NSString *ntpServer;

/// 日期格式
/// @note 0:YYYY-MM-DD 1:DD/MM/YYYY 2:MM/DD/YYYY
@property (nonatomic, assign) NSInteger dateFormat;

/// 夏令时时长
@property (nonatomic, assign) NSInteger dst;

/// 是否有夏令时
/// @note 0:没有夏令时 1:有夏令时
@property (nonatomic, assign) NSInteger isDst;

/// 时间格式
/// @note 0:24小时 1:12小时
@property (nonatomic, assign) NSInteger timeFormat;

/// 时间来源
/// @note 0:NTP 1:手动同步
@property (nonatomic, assign) NSInteger timeSource;

/// 时区
@property (nonatomic, assign) NSInteger timeZone;

@end



@interface IvyWiFiDetail : IvyObject

/// SSID
@property (nonatomic, strong) NSString *ssid;

/// Mac
@property (nonatomic, strong) NSString *mac;

/// 是否加密
@property (nonatomic, assign) BOOL encryption;

/// 信号质量
/// @note (0-100) 值越大，信号质量越高
@property (nonatomic, assign) NSInteger quality;

/// 模式
@property (nonatomic, assign) NSInteger mode;

/// 加密类型
@property (nonatomic, assign) NSInteger encryptType;


@end



@interface IvyWiFiConfig : IvyObject

/// SSID
@property (nonatomic, strong) NSString *ssid;

/// Wi-Fi 加密类型
@property (nonatomic, assign) NSInteger encryptType;

@end



@interface IvyStreamParam : NSObject

/// GOP
@property (nonatomic, assign) int GOP;

/// bitRate
@property (nonatomic, assign) int bitRate;

/// frameRate
@property (nonatomic, assign) int frameRate;

/// isVBR
@property (nonatomic, assign) int isVBR;

/// resolution
@property (nonatomic, assign) int resolution;

@end



@interface IvyPresetPoint : IvyObject

/// 结果
/// @note 0: 成功 1:预置点不存在  2:参数错误 3: 添加出厂预置点 4: 已存在
@property (nonatomic, assign) NSInteger result;

/// 预置点名称
@property (nonatomic, strong) NSArray<NSString *> *pointNames;

@end



@interface IvyPedestrianDetect : IvyObject

/// 开关
@property (nonatomic, assign) BOOL isEnable;

/// 追踪开关
@property (nonatomic, assign) BOOL isTrackEnable;

/// 联动告警
@property (nonatomic, assign) NSInteger linkage;

/// 灵敏度
/// @note 0：low 1:middle 2:high
@property (nonatomic, assign) NSInteger sensitivity;

/// 抓拍时间间隔
@property (nonatomic, assign) NSInteger snapInterval;

/// 触发时间间隔
@property (nonatomic, assign) NSInteger triggerInterval;

@end



@interface IvyScheduleRecordConfig : IvyObject

/// 是否开启计划录像
@property (nonatomic, assign) BOOL isEnable;

/// 录像级别
@property (nonatomic, assign) NSInteger recordLevel;

/// 空间满后录像处理方式
/// @note 0:覆盖 1:停止
@property (nonatomic, assign) NSInteger spaceFullMode;

/// 是否包含音频
/// @note 0:不包含音频 1:包含音频
@property (nonatomic, assign) NSInteger isEnableAudio;

/// 录像码流类型
/// @note 0:主码流 1:子码流
@property (nonatomic, assign) NSInteger streamType;

/// 周一
/// @note bit0-bit47表示一天，每个bit表示半个小时
@property (nonatomic, assign) long long schedule0;

/// 周二
@property (nonatomic, assign) long long schedule1;

/// 周三
@property (nonatomic, assign) long long schedule2;

/// 周四
@property (nonatomic, assign) long long schedule3;

/// 周五
@property (nonatomic, assign) long long schedule4;

/// 周六
@property (nonatomic, assign) long long schedule5;

/// 周日
@property (nonatomic, assign) long long schedule6;

@end


typedef NS_ENUM(NSInteger, IvyRecordType) {
    IvyRecordType0,
    IvyRecordType1,
};

@protocol IvyRecordObject <NSObject>

/// 录像对象类类型
@property (nonatomic, assign, readonly) IvyRecordType type;

@end

@interface IvyRecordObject0  : IvyObject <IvyRecordObject>

/// 通道号
@property (nonatomic, assign) NSInteger channel;

/// 录像开始时间
@property (nonatomic, assign) long long sTime;

/// 录像结束时间
@property (nonatomic, assign) long long eTime;

/// 录像类型
@property (nonatomic, assign) NSInteger recordType;

@end



@interface IvyRecordObject1  : IvyObject <IvyRecordObject>

/*
 20210121/20210121_135433/MDalarm_20210121_154541.mp4

 文件名格式: 类型_时间
 
 schedule: 计划录像
 manual: 手动录像
 MDalarm: 移动侦测录像
 SDalarm: 声音侦测录像
 IOalarm: IO侦测录像
 TDalarm: 温度侦测录像
 HDalarm: 湿度侦测录像
 HMalarm: 人体侦测录像
 FDalarm: 人脸侦测录像
 CLalarm: 越线侦测录像
 LMalarm: 留言侦测录像
 BKalarm: 门铃侦测录像
 */
/// 录像路径
@property (nonatomic, strong) NSString *filepath;

@end




@interface IvyRecordList : NSObject

/// Total number of videos
@property (nonatomic, assign) NSInteger totalCnt;

/// Start number
@property (nonatomic, assign) NSInteger startNo;

/// Video list
@property (nonatomic, strong) NSArray<id<IvyRecordObject>> *list;

@end



typedef NS_ENUM(NSInteger, IvyPictureInfoType) {
    IvyPictureInfoType0,
    IvyPictureInfoType1,
};

@protocol IvyPictureInfo <NSObject>

/// 类型
@property (nonatomic, assign, readonly) IvyPictureInfoType infoType;

/// 图片格式
@property (nonatomic, assign) NSInteger format;

/// 时间戳
@property (nonatomic, assign) unsigned long long time;

/// 图片类型
@property (nonatomic, assign) NSInteger type;

@end


@interface IvyPictureInfoObject0 : IvyObject <IvyPictureInfo>

/// 图片格式
@property (nonatomic, assign) NSInteger format;

/// 时间戳
@property (nonatomic, assign) unsigned long long time;

/// 图片类型
@property (nonatomic, assign) NSInteger type;

@end



@interface IvyPictureInfoObject1 : IvyObject <IvyPictureInfo>

/// 图片格式
@property (nonatomic, assign) NSInteger format;

/// 时间戳
@property (nonatomic, assign) unsigned long long time;

/// 图片类型
@property (nonatomic, assign) NSInteger type;

/// 方向
@property (nonatomic, assign) NSInteger direction;

/// 重量
@property (nonatomic, assign) NSInteger weight;

@end



@interface IvyPictureList : IvyObject

/// 通道号
@property (nonatomic, assign) NSInteger channel;

/// 总数
@property (nonatomic, assign) NSUInteger totalCount;

/// 当前计数
@property (nonatomic, assign) NSUInteger curCount;

/// 照片列表
@property (nonatomic, strong) NSArray<id<IvyPictureInfo>> *list;

@end



@interface IvyOSDSettings : IvyObject

/// 是否显示OSD
@property (nonatomic, assign) BOOL isEnableOSDMask;

/// 是否显示时间戳
@property (nonatomic, assign) BOOL isEnableTimeStamp;

/// 是否显示设备名称
@property (nonatomic, assign) BOOL isEnableDevName;

/// 是否显示温度、湿度
@property (nonatomic, assign) BOOL isEnableTempAndHumid;

/// dispPos
@property (nonatomic, assign) NSInteger dispPos;

@end



@interface IvyNVRChnDevInfo : IvyObject

@property (nonatomic, assign) NSInteger channel;

@property (nonatomic, assign) NSInteger isOnLine;

@property (nonatomic, strong) NSString *devName;

@property (nonatomic, strong) NSString *ip;

@property (nonatomic, strong) NSString *macAddr;

@property (nonatomic, strong) NSString *sysVer;

@property (nonatomic, strong) NSString *appVer;

@end



@interface IvyNVRChnAbility : IvyObject

@property (nonatomic, assign) NSInteger val0;

@property (nonatomic, assign) NSInteger val1;

@property (nonatomic, assign) NSInteger val2;

@property (nonatomic, assign) NSInteger val3;

@property (nonatomic, assign) NSInteger val4;

@property (nonatomic, assign) NSInteger val5;

@property (nonatomic, assign) NSInteger val6;

@property (nonatomic, assign) NSInteger val7;

@property (nonatomic, assign) NSInteger val8;

@end



@interface IvyNVRChn : IvyObject

@property (nonatomic, strong) IvyNVRChnDevInfo *devInfo;

@property (nonatomic, strong) IvyNVRChnAbility *ability;

@end



@interface IvyNVRDiskInfo : IvyObject

@property (nonatomic, assign) NSInteger index;

@property (nonatomic, assign) NSInteger busId;

// 硬盘类型 0:SATA 其他:USB
@property (nonatomic, assign) NSInteger busType;

@property (nonatomic, assign) NSInteger busy;

// 1:可以格式化 2:不可以格式化
@property (nonatomic, assign) NSInteger canFormat;

@property (nonatomic, assign) NSInteger isFormated;

// 硬盘状态 1:正在录像 2:备份
@property (nonatomic, assign) NSInteger state;

// 总空间
@property (nonatomic, assign) long long totalSpace;

// 剩余空间
@property (nonatomic, assign) long long freeSpace;

@property (nonatomic, strong) NSString *name;

@end



@interface IvyNVRDiskStorageConfig : IvyObject

// 是否开启循环覆盖 1:开启，0:不开启
@property (nonatomic, assign) NSInteger diskRewrite;

// 预录时间
@property (nonatomic, assign) NSInteger previewTime;

// 是否开启智能录像 1:开启，0:不开启
@property (nonatomic, assign) NSInteger smartMode;

@end



@interface IvyNVRDiskRecordObject : IvyObject

/// 当前通道
@property (nonatomic, assign) NSInteger channel;

/// 录像开始时间
@property (nonatomic, assign) unsigned long long st;

/// 录像结束时间
@property (nonatomic, assign) unsigned long long et;

/// 录像类型
@property (nonatomic, assign) NSInteger type;

@end



@interface IvyNVRMotionDetect : IvyObject

/// 是否启用 0:关闭 1:开启
@property (nonatomic, assign) NSInteger isEnable;

/// 通道
@property (nonatomic, assign) NSInteger channel;

/// 联动，按位表示
@property (nonatomic, assign) NSInteger linkage;

/// 移动侦测报警开关 0：关闭 1：开启
@property (nonatomic, assign) NSInteger moveAlarmEnable;

/// pir侦测开关 0：关闭 1：开启
@property (nonatomic, assign) NSInteger pirAlarmEnable;

/// 灵敏度0：low 1:normal 2:high 3:lower 4:lowest
@property (nonatomic, assign) NSInteger sensitivity;

/// 截图间隔, 单位秒
@property (nonatomic, assign) NSInteger snapInterval;

/// 触发间隔, 单位秒
@property (nonatomic, assign) NSInteger triggerInterval;

/// 周日
/// @note bit0-bit47表示一天，每个bit表示半个小时
@property (nonatomic, assign) long long schedule0;

/// 周一
@property (nonatomic, assign) long long schedule1;

/// 周二
@property (nonatomic, assign) long long schedule2;

/// 周三
@property (nonatomic, assign) long long schedule3;

/// 周四
@property (nonatomic, assign) long long schedule4;

/// 周五
@property (nonatomic, assign) long long schedule5;

/// 周六
@property (nonatomic, assign) long long schedule6;

/// 画格子方式，总行数
@property (nonatomic, assign) NSInteger rowNumber;

/// 画格子方式，总列数
@property (nonatomic, assign) NSInteger colNumber;

/// 区域设置
@property (nonatomic, strong) NSArray *area;

@end

NS_ASSUME_NONNULL_END
