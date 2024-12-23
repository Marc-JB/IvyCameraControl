//
//  IvyNVR.h
//  IvySdk
//
//  Created by JackChan on 8/3/2022.
//  Copyright © 2022 JackChan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IvyConstants.h"

NS_ASSUME_NONNULL_BEGIN

@interface IvyNVR : NSObject

/// 通过设备UID、以及设备对应的用户名、密码初始化IvyNVR对象
/// @param deviceUID NVR设备UID
/// @param username 设备用户名
/// @param password 设备密码
- (instancetype)initWithDeviceUID:(NSString *)deviceUID username:(NSString *)username password:(NSString *)password;

@end



@interface IvyNVR (Properties)

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

/// NVR下的IPC列表
/// @note 该列表通过IVY_CTRL_MSG_IPCLIST_CHG事件实时更新
@property (nonatomic, strong, readonly) NSArray<IvyNVRChn *> *ipcList;

@end



@interface IvyNVR (Connection)

/// 登录NVR
/// @param resultBlock 登陆结果
- (void)loginNVR:(void(^)(IVYIO_HANDLE_STATE handleState, IVYIO_RESULT cmdResult))resultBlock;

/// 登出NVR
- (void)logoutNVR;

/// 释放NVR
- (void)destroyNVR;

/// 更新用户名密码
/// @param username 设备用户名
/// @param password 设备密码
/// @return 是否成功
- (BOOL)updateUsername:(NSString *)username password:(NSString *)password;

/// 清除缓存的设备信息
- (void)resetConfiguration;

@end



@interface IvyNVR (DeviceInfo)

/// 获取设备信息
/// @param resultBlock 结果
- (void)getDevInfo:(void(^)(IvyDevInfo *devInfo, IVYIO_RESULT cmdResult))resultBlock;

@end



@interface IvyNVR (Display)

/// 获取镜像翻转状态
/// @param channel 通道
/// @param resultBlock 结果
- (void)getVideoMirrorFlip:(NSInteger)channel onCompletion:(void(^)(BOOL isMirror, BOOL isFlip, IVYIO_RESULT cmdResult))resultBlock;

/// 设置镜像翻转状态
/// @param channel 通道
/// @param isMirror 开启镜像
/// @param isFlip 开启翻转
/// @param resultBlock 结果
- (void)setVideoMirrorFlip:(NSInteger)channel isMirror:(BOOL)isMirror isFlip:(BOOL)isFlip onCompletion:(IvyCGIResultBlock)resultBlock;

@end



@interface IvyNVR (Controls)

/// 开始录像
/// @param filepath 文件存储路径
/// @param channel 录像通道
/// @param resultBlock 结果
- (void)startRecord:(NSString *)filepath channel:(NSInteger)channel onCompletion:(IvyCGIResultBlock)resultBlock;

/// 停止录像
/// @param channel 录像通道
/// @param resultBlock 结果
- (void)stopRecord:(NSInteger)channel onCompletion:(IvyCGIResultBlock)resultBlock;

/// 重启设备
/// @param resultBlock 结果
- (void)rebootSystem:(IvyCGIResultBlock)resultBlock;

@end



@interface IvyNVR (Detection)

/// 获取指定通道移动侦测信息
/// @param channel 录像通道
/// @param resultBlock 结果
- (void)getMotionDetectConfig:(NSInteger)channel onCompletion:(void(^)(IvyNVRMotionDetect *config, IVYIO_RESULT cmdResult))resultBlock;

/// 设置指定通道移动侦测信息
/// @param channel 录像通道
/// @param config IvyNVRMotionDetect 对象
/// @param resultBlock 结果
- (void)setMotionDetectConfig:(NSInteger)channel config:(IvyNVRMotionDetect *)config onCompletion:(IvyCGIResultBlock)resultBlock;

@end



@interface IvyNVR (Disk)

/// 获取硬盘信息
/// @param resultBlock 结果
- (void)getDiskInfo:(void(^)(IvyNVRDiskInfo *object, IVYIO_RESULT cmdResult))resultBlock;

/// 获取录像配置 (循环覆盖等信息)
/// @param resultBlock 结果
- (void)getDiskStorageConfig:(void(^)(IvyNVRDiskStorageConfig *object, IVYIO_RESULT cmdResult))resultBlock;

/// 设置录像配置
/// @param object 配置信息
/// @param resultBlock 结果
- (void)setDiskStorageConfig:(IvyNVRDiskStorageConfig *)object onCompletion:(IvyCGIResultBlock)resultBlock;

/// 获取硬盘录像列表
/// @param channel 录像通道
/// @param startTime 开始时间
/// @param endTime 结束时间
/// @param recordType 录像类型
/// @param resultBlock 结果
/// @note recordType 511:all 2:schedule 508:alarm
- (void)getDiskRecordList:(NSInteger)channel startTime:(NSUInteger)startTime endTime:(NSUInteger)endTime recordType:(NSInteger)recordType onCompletion:(void(^)(NSArray<IvyNVRDiskRecordObject *> *records, IVYIO_RESULT cmdResult))resultBlock;

/// 格式化硬盘
/// @param diskId IvyNVRDiskInfo里的index字段
/// @param resultBlock 结果
- (void)formatDisk:(NSInteger)diskId onCompletion:(IvyCGIResultBlock)resultBlock;

@end



@interface IvyNVR (Cloud)

/// NVR固件升级
/// @param URLString NVR升级链接 (升级链接需Base64编码)
/// @param resultBlock 结果
- (void)setNVROnlineUpgrade:(NSString *)URLString onCompletion:(IvyCGIResultBlock)resultBlock;

/// 摄像机固件升级
/// @param URLString IPC升级链接 (升级链接需Base64编码)
/// @param bitChannel 通道号二进制位
/// @param resultBlock 结果
/// @note 如果需要升级通道3下的摄像机，则bitChannel = 0x01 << 3；
- (void)setIPCOnlineUpgrade:(NSString *)URLString bitChannel:(NSInteger)bitChannel onCompletion:(IvyCGIResultBlock)resultBlock;

/// 设置文本推送配置
/// @param portal 网关地址
/// @param userTag 已授权的用户tag
/// @param resultBlock 结果
- (void)setTextPushConfig:(NSString *)portal userTag:(NSString *)userTag onCompletion:(IvyCGIResultBlock)resultBlock;

@end



@interface IvyNVR (Event)

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
