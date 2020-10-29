#import <Foundation/Foundation.h>
#import "ChatTransDataEvent.h"
#import <Flutter/Flutter.h>

@interface ChatMessageEventImpl : NSObject<ChatTransDataEvent>

- (instancetype)initWithChannel: (FlutterMethodChannel *)channel;

@end
