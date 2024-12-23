//
//  IvyVoicePlayer.h
//  IvyAppDemo
//
//  Created by JackChan on 7/1/2020.
//  Copyright © 2020 JackChan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IvyConstants.h"

NS_ASSUME_NONNULL_BEGIN

@class IvyVoicePlayer;

@protocol IvyVoicePlayerDelegate <NSObject>

@optional
/// 声波配网完成
/// @param player 当前对象
/// @param flag 结果
- (void)ivyVoicePlayerDidFinishPlaying:(IvyVoicePlayer *)player successfully:(BOOL)flag;

/// 声波配网异常
/// @param player 当前对象
/// @param error 异常
- (void)ivyVoicePlayerDecodeErrorDidOccur:(IvyVoicePlayer *)player error:(NSError * __nullable)error;

@end



@interface IvyVoicePlayer : NSObject

/// 初始化方法
/// @param SSID Wi-Fi SSID
/// @param password Wi-Fi 密码
/// @param deviceUID 设备UID (若无传nil)
/// @param bindingToken IOT 绑定token (若无传nil)
/// @param formatType 声波格式
/// @note 存在设备UID 时，formatType传IvyWiFiConfigTypeDefault；没有设备UID时，需确认当前设备支持的是哪一种内容格式。
- (instancetype)initWithSSID:(nonnull NSString *)SSID password:(nonnull NSString *)password deviceUID:(nullable NSString *)deviceUID bindingToken:(nullable NSString *)bindingToken formatType:(IvyWiFiConfigType)formatType;

/// 播放
- (void)play;

/// 停止播放
- (void)stop;

/// 代理
@property (nonatomic, weak) id<IvyVoicePlayerDelegate> delegate;

@end

NS_ASSUME_NONNULL_END
