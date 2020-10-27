#import "MobileImPlugin.h"
#import "IMClientManager.h"
#import "ClientCoreSDK.h"
#import "ConfigEntity.h"
#import "LocalUDPDataSender.h"


@implementation MobileImPlugin

static FlutterMethodChannel *_channel = nil;

+ (void)registerWithRegistrar: (NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel *channel = [FlutterMethodChannel
                                     methodChannelWithName: @"mobile_im_plugin"
                                     binaryMessenger: [registrar messenger]];
    _channel = channel;
    MobileImPlugin *instance = [[MobileImPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall: (FlutterMethodCall*)call result: (FlutterResult)result {
    if ([@"getPlatformVersion" isEqualToString:call.method]) {
        result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
    }
    else if ([@"init" isEqualToString: call.method]) {
        [self initClient: call.arguments];
        result(@"init client success");
    }
    else if ([@"login" isEqualToString: call.method]) {
        [self loginWith: call.arguments result: result];
    }
    else if ([@"logout" isEqualToString: call.method]) {
        [self logout: result];
    }
    else if ([@"send" isEqualToString: call.method]) {
        [self sendMessage: call.arguments result: result];
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

- (void)initClient: (NSDictionary *)arguments {
    NSString *appKey = arguments[@"appKey"];
    NSString *serverIP = arguments[@"serverIP"];
    int serverPort = [arguments[@"serverPort"] intValue];
    BOOL debug = [arguments[@"debug"] boolValue];
    [[IMClientManager sharedInstance] initMobileIMSDKWithChannel:_channel appKey:appKey serverIP:serverIP serverPort:serverPort debug:debug];
}

- (void)logout: (FlutterResult)result {
    int code = [[LocalUDPDataSender sharedInstance] sendLoginout];
    if(code == COMMON_CODE_OK) {
        result(@(code));
    }
    else {
        result(@(code));
    }
    // 退出登陆时记得一定要调用此行，不然不退出APP的情况下再登陆时会报 code=203错误哦！
    [[IMClientManager sharedInstance] resetInitFlag];
}

- (void)sendMessage: (NSDictionary *)arguments result: (FlutterResult)result {
    NSString *message = arguments[@"message"];
    NSString *toUserId = arguments[@"toUserId"];
    int code = [[LocalUDPDataSender sharedInstance] sendCommonDataWithStr: message toUserId: toUserId qos: YES fp: nil withTypeu: -1];
    if(code == COMMON_CODE_OK) {
        NSLog(@"发送成功");
        result(@(code));
    } else {
        NSLog(@"发送失败");
        result(@(code));
    }
}

- (void)loginWith: (NSDictionary *)arguments result: (FlutterResult)result {
    NSString *username = arguments[@"username"];
    NSString *password = arguments[@"password"];
    int code = [[LocalUDPDataSender sharedInstance] sendLogin: username withToken: password];
    if(code == COMMON_CODE_OK) {
        NSLog(@"登录成功");
        result(@(code));
    }
    else {
        NSLog(@"登录失败");
        result(@(code));
    }
}

@end
