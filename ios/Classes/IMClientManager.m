#import "IMClientManager.h"
#import "ClientCoreSDK.h"
#import "ConfigEntity.h"
#import "TCPFrameCodec.h"


///////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - 私有API
///////////////////////////////////////////////////////////////////////////////////////////

@interface IMClientManager ()

/* MobileIMSDK是否已被初始化. true表示已初化完成，否则未初始化. */
@property (nonatomic) BOOL _init;
//
@property (strong, nonatomic) ChatBaseEventImpl *baseEventListener;
//
@property (strong, nonatomic) ChatMessageEventImpl *transDataListener;
//
@property (strong, nonatomic) MessageQoSEventImpl *messageQoSListener;

@property (strong, nonatomic) FlutterMethodChannel *channel;

@end


///////////////////////////////////////////////////////////////////////////////////////////
#pragma mark - 本类的代码实现
///////////////////////////////////////////////////////////////////////////////////////////

@implementation IMClientManager

// 本类的单例对象
static IMClientManager *instance = nil;

+ (IMClientManager *)sharedInstance {
    if (instance == nil) {
        instance = [[super allocWithZone:NULL] init];
    }
    return instance;
}

/*
 *  重写init实例方法实现。
 *
 *  @return
 *  @see [NSObject init:]
 */
- (id)init {
    if (![super init]) return nil;
    return self;
}

- (void)initMobileIMSDKWithChannel: (FlutterMethodChannel *)channel appKey: (NSString *)appKey serverIP: (NSString *)serverIP serverPort: (int)serverPort debug: (BOOL)debug  {
    if(!self._init) {
        // 设置AppKey 服务器IP和服务器端口
        [ConfigEntity registerWithAppKey:appKey];
        [ConfigEntity setServerIp:serverIP];
        [ConfigEntity setServerPort:serverPort];
        
        // 使用以下代码表示不绑定固定port（由系统自动分配），否则使用默认的7801端口
        //      [ConfigEntity setLocalSendAndListeningPort:-1];
        
        // RainbowCore核心IM框架的敏感度模式设置
        //      [ConfigEntity setSenseMode:SenseMode10S];
        
        // 设置最大TCP帧内容长度（不设置则默认最大是 6 * 1024字节）
        //      [TCPFrameCodec setTCP_FRAME_MAX_BODY_LENGTH:60 * 1024];
        
        // 开启DEBUG信息输出
        [ClientCoreSDK setENABLED_DEBUG:debug];
        
        // 设置事件回调
        self.baseEventListener = [[ChatBaseEventImpl alloc] initWithChannel: channel];
        self.transDataListener = [[ChatMessageEventImpl alloc] initWithChannel: channel];
        self.messageQoSListener = [[MessageQoSEventImpl alloc] initWithChannel: channel];
        [ClientCoreSDK sharedInstance].chatBaseEvent = self.baseEventListener;
        [ClientCoreSDK sharedInstance].chatMessageEvent = self.transDataListener;
        [ClientCoreSDK sharedInstance].messageQoSEvent = self.messageQoSListener;
        
        self._init = YES;
    }
}

- (void)releaseMobileIMSDK {
    [[ClientCoreSDK sharedInstance] releaseCore];
    [self resetInitFlag];
}

- (void)resetInitFlag {
    self._init = NO;
}

- (ChatMessageEventImpl *) getTransDataListener {
    return self.transDataListener;
}

- (ChatBaseEventImpl *) getBaseEventListener {
    return self.baseEventListener;
}

- (MessageQoSEventImpl *) getMessageQoSListener {
    return self.messageQoSListener;
}

@end
