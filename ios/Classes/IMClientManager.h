#import <Foundation/Foundation.h>
#import "ChatBaseEventImpl.h"
#import "ChatMessageEventImpl.h"
#import "MessageQoSEventImpl.h"
#import <Flutter/Flutter.h>

@interface IMClientManager : NSObject

+ (IMClientManager *)sharedInstance;


- (void)initMobileIMSDKWithChannel: (FlutterMethodChannel *)channel appKey: (NSString *)appKey serverIP: (NSString *)serverIP serverPort: (int)serverPort debug: (BOOL)debug;

- (void)releaseMobileIMSDK;

- (void)resetInitFlag;

- (ChatMessageEventImpl *) getTransDataListener;
- (ChatBaseEventImpl *) getBaseEventListener;
- (MessageQoSEventImpl *) getMessageQoSListener;

@end
