//
//  IvySdkManager.h
//  IvySdk
//
//  Created by JackChan on 26/6/2019.
//  Copyright © 2019 ivyiot. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IvyConstants.h"

NS_ASSUME_NONNULL_BEGIN

@interface IvySdkManager : NSObject

/// 单例对象
/// @return 单例对象
+ (IvySdkManager *)shared;

/// 初始化方法
- (void)initializer;

/// 获取版本信息
/// @return 版本信息
- (NSString *)versionInfo;

/// 设置P2P服务器地区
/// @param region 地区（0: 中国区，1:非中国区）
/// @note 默认使用非中国区P2P服务器
- (void)setP2PRegion:(NSInteger)region;

/// 设置Sdk日志信息
/// @param level 日志等级 (0: NO 1: Error 2:Debug 3: All)
/// @param filepath 日志文件路径
/// @param maxSize 文件最大尺寸(M)
- (void)setLog:(NSInteger)level filepath:(NSString *)filepath maxSize:(NSUInteger)maxSize;

/// 停止局域网搜索
- (void)stopDiscovery;

/// 重启局域网搜索
- (void)restartDiscovery;

/// 搜索局域网设备
/// @param resultBlock 结果
- (void)searchDevices:(void(^)(NSArray<IvyDevLan *> *devices))resultBlock;

/// 二维码配网内容字符串
/// @param SSID Wi-Fi SSID
/// @param password Wi-Fi 密码
/// @param deviceUID 设备UID (若无传nil)
/// @param bindingToken IOT 绑定token (若无传nil)
- (NSString *)qrCodeString:(nonnull NSString *)SSID password:(nonnull NSString *)password deviceUID:(nullable NSString *)deviceUID bindingToken:(nullable NSString *)bindingToken;

/// 通过设备UID获取设备出厂密码
/// @param deviceUID 设备UID
- (NSString *)resetPasswordByDeviceUID:(nonnull NSString *)deviceUID;

/// 通过设备UID判断设备类型
/// @param deviceUID 设备UID
- (IvyDeviceType)deviceTypeByDeviceUID:(nonnull NSString *)deviceUID;

@end

NS_ASSUME_NONNULL_END
