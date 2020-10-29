#import <Foundation/Foundation.h>
#import "ChatBaseEvent.h"
#import "CompletionDefine.h"
#import <Flutter/Flutter.h>

@interface ChatBaseEventImpl : NSObject<ChatBaseEvent>

- (instancetype)initWithChannel: (FlutterMethodChannel *)channel;

/** 本Observer目前仅用于登陆时（因为登陆与收到服务端的登陆验证结果是异步的，所以有此观察者来完成收到验证后的处理）*/
@property (nonatomic, copy) ObserverCompletion loginOkForLaunchObserver;// block代码块一定要用copy属性，否则报错！

@end
