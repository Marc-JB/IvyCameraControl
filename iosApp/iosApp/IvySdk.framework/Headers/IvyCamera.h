//
//  IvyCamera.h
//  IvySdk
//
//  Created by JackChan on 13/6/2019.
//  Copyright © 2019 ivyiot. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IvyConstants.h"

typedef void(^IvyCameraLoginBlock)(IVYIO_HANDLE_STATE handleState, IVYIO_RESULT cmdResult);

typedef void(^IvyCameraResultBlock)(_Nullable id obj, IVYIO_RESULT cmdResult);

NS_ASSUME_NONNULL_BEGIN

@interface IvyCamera : NSObject

/// 通过设备UID、以及设备对应的用户名、密码初始化IvyCamera对象
/// @param deviceUID 设备UID
/// @param username 设备用户名
/// @param password 设备密码
/// @return IvyCamera对象
- (instancetype)initWithDeviceUID:(NSString *)deviceUID username:(NSString *)username password:(NSString *)password;

@end



@interface IvyCamera (Properties)

/// 设备句柄
@property (nonatomic, assign, readonly) IVYHANDLE handle;

/// 设备UID
@property (nonatomic, strong, readonly) NSString *deviceUID;

/// 设备用户名
@property (nonatomic, strong, readonly) NSString *username;

/// 设备密码
@property (nonatomic, strong, readonly) NSString *password;

/// 设备句柄状态
@property (nonatomic, assign, readonly) IVYIO_HANDLE_STATE handleState;

@end



@interface IvyCamera (Connection)

/// 登录Camera
/// @param resultBlock 登陆结果
- (void)loginCamera:(void(^)(IVYIO_HANDLE_STATE handleState, IVYIO_RESULT cmdResult))resultBlock;

/// 登出Camera
- (void)logoutCamera;

/// 释放Camera
- (void)destroyCamera;

/// 更新用户名密码
/// @param username 设备用户名
/// @param password 设备密码
/// @return 是否成功
- (BOOL)updateUsername:(NSString *)username password:(NSString *)password;

/// 清除缓存的设备信息
- (void)resetConfiguration;

@end



@interface IvyCamera (DeviceInfo)

/// 获取设备能力级
/// @param resultBlock 结果
- (void)getDevAbility:(void(^)(IvyDevAbility *devAbility, IVYIO_RESULT cmdResult))resultBlock;

/// 获取设备信息
/// @param resultBlock 结果
- (void)getDevInfo:(void(^)(IvyDevInfo *devInfo, IVYIO_RESULT cmdResult))resultBlock;

/// 修改设备用户名密码
/// @param newUserName 新用户名
/// @param newPassword 新密码
/// @param resultBlock 结果
/// @note 修改用户名、密码成功后，需要重新调用loginCamera登陆摄像机
- (void)modifyUserNameAndPassword:(NSString *)newUserName newPassword:(NSString *)newPassword onCompletion:(IvyCameraResultBlock)resultBlock;

@end



@interface IvyCamera (Display)

/// 获取镜像翻转状态
/// @param resultBlock 结果
- (void)getVideoMirrorFlip:(void(^)(BOOL isMirror, BOOL isFlip, IVYIO_RESULT cmdResult))resultBlock;

/// 设置镜像状态
/// @param isMirror 开启镜像
/// @param resultBlock 结果
- (void)setMirrorVideo:(BOOL)isMirror onCompletion:(IvyCameraResultBlock)resultBlock;

/// 设置翻转状态
/// @param isFlip 开启翻转
/// @param resultBlock 结果
- (void)setFlipVideo:(BOOL)isFlip onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取红外灯状态
/// @param resultBlock 结果
- (void)getDayNightConfig:(void(^)(NSInteger mode, BOOL onoff, IVYIO_RESULT cmdResult))resultBlock;

/// 设置红外灯状态
/// @param onoff 红外灯开关 (只有在手动模式下，onoff才会有效)
/// @param mode 红外灯模式 (0：自动 1：手动 2：计划)
/// @param resultBlock 结果
- (void)setDayNightConfig:(BOOL)onoff mode:(NSInteger)mode onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取设置支持的清晰度类型
/// @param resultBlock 结果
/// @note types数组值参考IvyDefinitionType
- (void)getSupportedStreamTypes:(void(^)(NSArray *types, IVYIO_RESULT cmdResult))resultBlock;

/// 获取当前清晰度类型
/// @param resultBlock 结果
- (void)getStreamType:(void(^)(IvyDefinitionType streamType, IVYIO_RESULT cmdResult))resultBlock;

/// 设置摄像机清晰度
/// @param streamType 清晰度
/// @param resultBlock 结果
- (void)setStreamType:(IvyDefinitionType)streamType onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取OSD设置状态
/// @param resultBlock 结果
- (void)getOSDSettings:(void(^)(IvyOSDSettings *obj, IVYIO_RESULT cmdResult))resultBlock;

/// 设置OSD设置状态
/// @param obj OSD对象
/// @param resultBlock 结果
- (void)setOSDSettings:(IvyOSDSettings *)obj onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取摄像机WDR开关状态
/// @param resultBlock 结果
/// @note mode: 0关闭, 1开启
- (void)getCameraWDRState:(void(^)(NSInteger mode, IVYIO_RESULT cmdResult))resultBlock;

/// 设置摄像机WDR开关状态
/// @param mode 状态
/// @param resultBlock 结果
- (void)setCameraWDRState:(NSInteger)mode onCompletion:(IvyCameraResultBlock)resultBlock;

/// 设置设备名称
/// @param devName 名称
/// @param resultBlock 结果
/// @note devName 如能力集(enableUnicodeDevName)不支持Unicode，则只能设置如下字符"^[A-Za-z0-9_\\-\\s]{1,20}$"。
- (void)setDevName:(NSString *)devName onCompletion:(IvyCameraResultBlock)resultBlock;

@end



@interface IvyCamera (PTZ)

/// 云台控制
/// @param ptzCmd 控制命令
/// @param resultBlock 结果
- (void)setPTZCmd:(IVY_PTZ_CMD)ptzCmd onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取预置点列表
/// @param resultBlock 结果
- (void)getPresetPointList:(void(^)(IvyPresetPoint *presetPoint, IVYIO_RESULT cmdResult))resultBlock;

/// 转到指定预置点
/// @param pointName 预置点
/// @param resultBlock 结果
- (void)goToPresetPoint:(NSString *)pointName onCompletion:(IvyCameraResultBlock)resultBlock;

/// 添加预置点
/// @param pointName 预置点
/// @param resultBlock 结果
- (void)addPresetPoint:(NSString *)pointName onCompletion:(IvyCameraResultBlock)resultBlock;

/// 删除预置点
/// @param pointName 预置点
/// @param resultBlock 结果
- (void)deletePresetPoint:(NSString *)pointName onCompletion:(void(^)(IvyPresetPoint *presetPoint, IVYIO_RESULT cmdResult))resultBlock;

/// 垂直巡航
/// @param resultBlock 结果
- (void)startVerticalCruise:(IvyCameraResultBlock)resultBlock;

/// 水平巡航
/// @param resultBlock 结果
- (void)startHorizontalCruise:(IvyCameraResultBlock)resultBlock;

/// 定点巡航
/// @param resultBlock 结果
/// @note 该模式要求预置位不得少于2个
- (void)startPresetPointCruise:(IvyCameraResultBlock)resultBlock;

/// 停止巡航
/// @param resultBlock 结果
- (void)stopCruise:(IvyCameraResultBlock)resultBlock;

@end



@interface IvyCamera (Detection)

/// 获取移动侦测信息
/// @param resultBlock 结果
/// @note config 需根据type判断类型，使用对应的对象
- (void)getMotionDetectConfig:(void(^)(id<IvyMotionDetect> _Nullable config, IVYIO_RESULT cmdResult))resultBlock;

/// 设置移动侦测信息
/// @param config DevMotionDetect对象
/// @param resultBlock 结果
- (void)setMotionDetectConfig:(id<IvyMotionDetect>)config onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取人体侦测信息
/// @param resultBlock 结果
- (void)getPedestrianDetectConfig:(void(^)(IvyPedestrianDetect *config, IVYIO_RESULT cmdResult))resultBlock;

/// 设置人体侦测信息
/// @param config DevMotionDetect对象
/// @param resultBlock 结果
- (void)setPedestrianDetectConfig:(IvyPedestrianDetect *)config onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取人体侦测设备信息
/// @param resultBlock 结果
- (void)getPedestrianLicenseDeviceInfo:(void(^)(NSString *info, IVYIO_RESULT cmdResult))resultBlock;

/// 设置人体侦测License
/// @param license license
/// @param resultBlock 结果
- (void)setPedestrianLicense:(NSString *)license onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取声音侦测
/// @param resultBlock 结果
- (void)getAudioDetectConfig:(void(^)(IvyAudioDetect *config, IVYIO_RESULT cmdResult))resultBlock;

/// 设置声音侦测
/// @param config DevMotionDetect对象
/// @param resultBlock 结果
- (void)setAudioDetectConfig:(IvyAudioDetect *)config onCompletion:(IvyCameraResultBlock)resultBlock;

@end



@interface IvyCamera (Network)

/// 获取设备Wi-Fi配置
/// @param resultBlock 结果
- (void)getWiFiConfig:(void(^)(IvyWiFiConfig *config, IVYIO_RESULT cmdResult))resultBlock;

/// 获取Wi-Fi列表
/// @param resultBlock 结果
- (void)getWiFiList:(IvyCameraResultBlock)resultBlock;

/// 设置Wi-Fi
/// @param model 选择的Wi-Fi类型
/// @param password Wi-Fi密码
/// @param resultBlock 结果
- (void)setWiFiConfig:(IvyWiFiDetail *)model password:(NSString *)password onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取网络自适应开关状态
/// @param resultBlock 结果
/// @note mode: 0关闭, 1开启
- (void)getNetworkAutoAdaptability:(void(^)(NSInteger mode, IVYIO_RESULT cmdResult))resultBlock;

/// 设置网络自适应开关状态
/// @param mode 状态
/// @param resultBlock 结果
- (void)setNetworkAutoAdaptability:(NSInteger)mode onCompletion:(IvyCameraResultBlock)resultBlock;

@end



@interface IvyCamera (Time)

/// 获取设备系统时间
/// @param resultBlock 结果
- (void)getSystemTime:(void(^)(IvyDevTime *devTime, IVYIO_RESULT cmdResult))resultBlock;

/// 设置系统时间
/// @param devTime 时间对象
/// @param resultBlock 结果
- (void)setSystemTime:(IvyDevTime *)devTime onCompletion:(IvyCameraResultBlock)resultBlock;

/// 同步手机时间到设备
/// @param resultBlock 结果
- (void)syncSystemTime:(IvyCameraResultBlock)resultBlock;

@end



@interface IvyCamera (SDCard)

/// 获取设备SD卡信息
/// @param resultBlock 结果
- (void)getSDCardInfo:(void(^)(IvySDInfo *ivySDInfo, IVYIO_RESULT cmdResult))resultBlock;

/// 格式化SD卡
/// @param resultBlock 结果
- (void)setSDCardFormat:(IvyCameraResultBlock)resultBlock;

/// 获取SD卡录像列表
/// @param startTime 开始时间
/// @param endTime 结束时间
/// @param recordType 录像类型
/// @param resultBlock 结果
/// @note 一次查询时间不能超过一天
- (void)getSDCardRecordList:(NSUInteger)startTime endTime:(NSUInteger)endTime recordType:(NSInteger)recordType onCompletion:(IvyCameraResultBlock)resultBlock;

/// 分页获取SD卡录像列表
/// @param startTime Start time
/// @param endTime End time
/// @param recordType record Type
/// @param startNo Start number
/// @param resultBlock result
/// @note The query time cannot exceed one day. startNo starts at 0.
- (void)getSDCardRecordList:(NSUInteger)startTime endTime:(NSUInteger)endTime recordType:(NSInteger)recordType startNo:(NSInteger)startNo onCompletion:(void(^)(IvyRecordList *recordList, IVYIO_RESULT cmdResult))resultBlock;

/// 获取SD卡图片列表
/// @param startTime 开始时间
/// @param endTime 结束时间
/// @param type 图片类型
/// @param startNo 起始位置
/// @param resultBlock 结果
/// @note type 1表示手动录像、2表示计划录像、4表示移动侦测、8表示声音侦测、16表示IO侦测、32表示温度侦测、64表示湿度侦测、128表示人行侦测、256表示一键告警
- (void)getSDCardPictureList:(unsigned long long)startTime endTime:(unsigned long long)endTime type:(NSInteger)type startNo:(NSInteger)startNo onCompletion:(IvyCameraResultBlock)resultBlock;

/// 下载SD卡图片
/// @param info IvyPictureInfo 对象
/// @param resultBlock 结果
/// @note 图片下载成功，则以NSData返回
- (void)downloadSDCardPicture:(id<IvyPictureInfo>)info onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取计划录像配置
/// @param resultBlock 结果
- (void)getScheduleRecordConfig:(void(^)(IvyScheduleRecordConfig *config, IVYIO_RESULT cmdResult))resultBlock;

/// 设置计划录像配置
/// @param config IvyScheduleRecordConfig 对象
/// @param resultBlock 结果
- (void)setScheduleRecordConfig:(IvyScheduleRecordConfig *)config onCompletion:(IvyCameraResultBlock)resultBlock;

@end



@interface IvyCamera (Controls)

/// 开始录像
/// @param filepath 文件存储路径
/// @param resultBlock 结果
- (void)startRecord:(NSString *)filepath onCompletion:(IvyCameraResultBlock)resultBlock;

/// 停止录像
/// @param resultBlock 结果
- (void)stopRecord:(IvyCameraResultBlock)resultBlock;

/// 获取摄像机音量
/// @param resultBlock 结果
/// @note volume的取值范围0~100
- (void)getAudioVolume:(void(^)(NSInteger volume, IVYIO_RESULT cmdResult))resultBlock;

/// 设置摄像机音量
/// @param volume 音量大小
/// @param resultBlock 结果
- (void)setAudioVolume:(NSInteger)volume onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取对讲音量
/// @param resultBlock 结果
/// @note volume的取值范围0~100
- (void)getSpeakerVolume:(void(^)(NSInteger volume, IVYIO_RESULT cmdResult))resultBlock;

/// 设置对讲音量
/// @param volume 音量大小
/// @param resultBlock 结果
- (void)setSpeakerVolume:(NSInteger)volume onCompletion:(IvyCameraResultBlock)resultBlock;

/// 重启设备
/// @param resultBlock 结果
- (void)rebootSystem:(IvyCameraResultBlock)resultBlock;

/// 获取电源频率
/// @param resultBlock 结果
/// @note mode: 0表示50HZ，1表示60HZ，2表示户外模式
- (void)getFrequency:(void(^)(NSInteger mode, IVYIO_RESULT cmdResult))resultBlock;

/// 设置电源频率
/// @param mode IPC电源频率
/// @param resultBlock 结果
- (void)setFrequency:(NSInteger)mode onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取休眠状态
/// @param resultBlock 结果
/// @note mode: 0未启用, 1休眠状态，2唤醒状态，3结束休眠
- (void)getCameraSleepState:(void(^)(NSInteger mode, IVYIO_RESULT cmdResult))resultBlock;

/// 唤醒设备
/// @param resultBlock 结果
- (void)setCameraWakeUp:(IvyCameraResultBlock)resultBlock;

/// 休眠设备
/// @param resultBlock 结果
- (void)setCameraSleep:(IvyCameraResultBlock)resultBlock;

/// 获取指示灯开关状态
/// @param resultBlock 结果
/// @note mode: 0关闭, 1开启
- (void)getLedEnableState:(void(^)(NSInteger mode, IVYIO_RESULT cmdResult))resultBlock;

/// 设置指示灯开关状态
/// @param mode 状态
/// @param resultBlock 结果
- (void)setLedEnableState:(NSInteger)mode onCompletion:(IvyCameraResultBlock)resultBlock;

/// 获取提示语音开关状态
/// @param resultBlock 结果
/// @note mode: 0关闭, 1开启
- (void)getVoiceState:(void(^)(NSInteger mode, IVYIO_RESULT cmdResult))resultBlock;

/// 设置提示语音开关状态
/// @param mode 状态
/// @param resultBlock 结果
- (void)setVoiceState:(NSInteger)mode onCompletion:(IvyCameraResultBlock)resultBlock;

@end



@interface IvyCamera (Cloud)

/// 在线升级
/// @param URLString 升级链接
/// @param resultBlock 结果
- (void)setOnlineUpgrade:(NSString *)URLString onCompletion:(IvyCameraResultBlock)resultBlock;

/// 设置云存储配置
/// @param portal 网关地址
/// @param deviceMac 设备Mac
/// @param streamId 已授权的云存储Id
/// @param userTag 已授权的用户tag
/// @param resultBlock 结果
- (void)setCloudStorageConfig:(NSString *)portal deviceMac:(NSString *)deviceMac streamId:(NSString *)streamId userTag:(NSString *)userTag onCompletion:(IvyCameraResultBlock)resultBlock;

/// 设置富媒体推送配置
/// @param portal 网关地址
/// @param userTag 已授权的用户tag
/// @param resultBlock 结果
- (void)setRichMediaPushConfig:(NSString *)portal userTag:(NSString *)userTag onCompletion:(IvyCameraResultBlock)resultBlock;

/// 设置文本推送配置
/// @param portal 网关地址
/// @param userTag 已授权的用户tag
/// @param resultBlock 结果
- (void)setTextPushConfig:(NSString *)portal userTag:(NSString *)userTag onCompletion:(IvyCameraResultBlock)resultBlock;

@end



@interface IvyCamera (Command)

/// 通用命令
/// @param cgiCommand 命令
/// @param parameters 输入参数
/// @param iTimeout 超时时长
/// @param resultBlock 结果
- (void)sendCommand:(int)cgiCommand parameters:(NSDictionary *)parameters iTimeout:(int)iTimeout onCompletion:(IvyCameraResultBlock)resultBlock;

/// 通用命令，发送CGI命令
/// @param cgiCommand 命令字符串
/// @param iTimeout 超时时长
/// @param resultBlock 结果
- (void)sendCGICommand:(NSString *)cgiCommand iTimeout:(int)iTimeout onCompletion:(IvyCameraResultBlock)resultBlock;

@end



@interface IvyCamera (Event)

/// 添加一个事件的观察者
/// @param observer 观察者
/// @param aSelector 选择子
/// @note 必须调用removeEventObserver:移除当前观察者
- (void)addEventObserver:(id)observer selector:(SEL)aSelector;

/// 移除一个观察者
/// @param observer 观察者
- (void)removeEventObserver:(id)observer;

@end

NS_ASSUME_NONNULL_END
