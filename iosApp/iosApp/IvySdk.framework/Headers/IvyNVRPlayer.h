//
//  IvyNVRPlayer.h
//  IvySdk
//
//  Created by JackChan on 11/3/2022.
//  Copyright © 2022 JackChan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "IvyConstants.h"

NS_ASSUME_NONNULL_BEGIN

@class IvyNVRPlayer;
@class IvyNVR;

@protocol IvyNVRPlayer <NSObject>

@end


@protocol IvyNVRPlayerDelegate <NSObject>

@required

/// 解码到一帧图像
/// @param player 当前播放器
/// @param channel 通道号
/// @param image 一帧图像
/// @param isFirstFrame 是否为第一帧数据
- (void)ivyNVRPlayer:(id<IvyNVRPlayer>)player channel:(NSInteger)channel didReciveFrame:(UIImage *)image isFirstFrame:(BOOL)isFirstFrame;

@optional

/// 操作命令结果
/// @param player 当前播放器
/// @param channel 通道号
/// @param command 命令
/// @param result 结果
- (void)ivyNVRPlayer:(id<IvyNVRPlayer>)player channel:(NSInteger)channel playerCommand:(IvyPlayerCommand)command result:(IVYIO_RESULT)result;

/// 回放进度
/// @param player 当前播放器
/// @param channel 通道号
/// @param totalTime 总时长
/// @param timePosition 当前播放位置
- (void)ivyNVRPlayer:(id<IvyNVRPlayer>)player channel:(NSInteger)channel totalTime:(NSTimeInterval)totalTime timePosition:(NSTimeInterval)timePosition;

/// 播放流量
/// @param player 当前播放器
/// @param channel 通道号
/// @param transmitSpeed 流量大小(Kb)
- (void)ivyNVRPlayer:(id<IvyNVRPlayer>)player channel:(NSInteger)channel mediaTransmitSpeed:(NSUInteger)transmitSpeed;

@end



/// NVR直播 播放器
@interface IvyNVRLivePlayer : NSObject <IvyNVRPlayer>

/// 播放指定通道
/// @param ivyNVR 当前NVR对象
/// @param channel 通道号
/// @param streamType 码流类型
- (void)playLive:(IvyNVR *)ivyNVR channel:(NSInteger)channel streamType:(IVYIO_VIDEO_STREAM_TYPE)streamType;

/// 停止播放指定通道
/// @param channel 通道号
- (void)stop:(NSInteger)channel;

/// 停止所有通道播放
- (void)stop;

/// 代理
@property (nonatomic, weak) id<IvyNVRPlayerDelegate> delegate;

@end



/// NVR硬盘回放 播放器
@interface IvyNVRDiskPlayer : NSObject <IvyNVRPlayer>

/// 播放指定通道硬盘录像
/// @param ivyNVR 当前NVR对象
/// @param recordObject 录像信息
- (void)playBack:(IvyNVR *)ivyNVR recordObject:(IvyNVRDiskRecordObject *)recordObject;

/// 停止播放
- (void)stop;

/// 暂停
- (IVYIO_RESULT)pause;

/// 恢复
- (IVYIO_RESULT)resume;

/// seek
/// @param pts 位置
- (IVYIO_RESULT)seek:(long long)pts;

/// 代理
@property (nonatomic, weak) id<IvyNVRPlayerDelegate> delegate;

@end

NS_ASSUME_NONNULL_END
