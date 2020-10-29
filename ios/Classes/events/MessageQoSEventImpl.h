#import <Foundation/Foundation.h>
#import "MessageQoSEvent.h"
#import <Flutter/Flutter.h>

@interface MessageQoSEventImpl : NSObject<MessageQoSEvent>

- (instancetype)initWithChannel: (FlutterMethodChannel *)channel;

@end
