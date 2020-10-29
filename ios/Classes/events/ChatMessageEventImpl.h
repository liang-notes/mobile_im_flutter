#import <Foundation/Foundation.h>
#import "ChatMessageEvent.h"
#import <Flutter/Flutter.h>

@interface ChatMessageEventImpl : NSObject<ChatMessageEvent>

- (instancetype)initWithChannel: (FlutterMethodChannel *)channel;

@end
