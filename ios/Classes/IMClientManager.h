#import <Foundation/Foundation.h>
#import "ChatBaseEventImpl.h"
#import "ChatMessageEventImpl.h"
#import "MessageQoSEventImpl.h"
#import <Flutter/Flutter.h>

@interface IMClientManager : NSObject

/*!
 * 取得本类实例的唯一公开方法。
 * 本类目前在APP运行中是以单例的形式存活，请一定注意这一点哦。
 */
+ (IMClientManager *)sharedInstance;


- (void)initMobileIMSDKWithChannel: (FlutterMethodChannel *)channel appKey: (NSString *)appKey serverIP: (NSString *)serverIP serverPort: (int)serverPort debug: (BOOL)debug;

- (void)releaseMobileIMSDK;

/**
 * 重置init标识。
 * 重要说明：不退出APP的情况下，重新登陆时记得调用一下本方法，
 * 不然再次调用 {@link #initMobileIMSDK()} 时也不会重新初始化MobileIMSDK
 * 详见 {@link #initMobileIMSDK()}代码）而报 code=203错误！
 */
- (void)resetInitFlag;

- (ChatMessageEventImpl *) getTransDataListener;
- (ChatBaseEventImpl *) getBaseEventListener;
- (MessageQoSEventImpl *) getMessageQoSListener;

@end
