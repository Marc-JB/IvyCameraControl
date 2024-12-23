//
//  IvySDDownloader.h
//  IvySdk
//
//  Created by JackChan on 18/1/2021.
//  Copyright © 2021 JackChan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "IvyCamera.h"

NS_ASSUME_NONNULL_BEGIN

@class IvySDDownloader;

@protocol IvySDDownloaderDelegate <NSObject>

@required

/// SD卡录像下载成功
/// @param downloader IvySDDownloader对象
/// @param location SD卡录像文件存储路径
- (void)ivySDDownloader:(IvySDDownloader *)downloader didFinishDownloadingToURL:(NSURL *)location;

@optional

/// SD卡录像下载进度
/// @param downloader IvySDDownloader对象
/// @param progress 进度值
/// @note progress范围0～1
- (void)ivySDDownloader:(IvySDDownloader *)downloader progress:(CGFloat)progress;

/// SD卡录像下载失败
/// @param downloader IvySDDownloader对象
/// @param result 失败原因
- (void)ivySDDownloader:(IvySDDownloader *)downloader didFinishDownloadingWithError:(IVYIO_RESULT)result;

@end

@interface IvySDDownloader : NSObject

/// IvySDDownloader单例
+ (IvySDDownloader *)shared;

/// 下载SD卡录像文件
/// @param ivyCamera 设备对象
/// @param recordObject 待下载的SD卡录像对象
/// @param location SD卡录像文件存储路径
- (void)downloadSDCardRecod:(IvyCamera *)ivyCamera recordObject:(id<IvyRecordObject>)recordObject toURL:(NSURL *)location;

/// 取消当前下载
- (void)cancel;

/// 是否正在下载录像
@property (nonatomic, assign, readonly) BOOL isDownloading;

/// 代理
@property (nonatomic, weak) id<IvySDDownloaderDelegate> delegate;

@end


NS_ASSUME_NONNULL_END
