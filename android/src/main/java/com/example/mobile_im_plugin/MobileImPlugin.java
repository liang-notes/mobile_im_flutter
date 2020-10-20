package com.example.mobile_im_plugin;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import net.x52im.mobileimsdk.android.core.LocalDataSender;
import net.x52im.mobileimsdk.android.core.LocalSocketProvider;

import java.util.HashMap;
import java.util.Observer;


import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


/**
 * MobileImPlugin
 */
public class MobileImPlugin implements FlutterPlugin, MethodCallHandler {

    private MethodChannel channel;
    private Context context;

    /**
     * 收到服务端的登陆完成反馈时要通知的观察者（因登陆是异步实现，本观察者将由
     * ChatBaseEvent 事件的处理者在收到服务端的登陆反馈后通知之）
     */
    private Observer onLoginSuccessObserver = null;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "mobile_im_plugin");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
        initIMClientManager();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    private void initIMClientManager() {
        // 确保MobileIMSDK被初始化哦（整个APP生生命周期中只需调用一次哦）
        // 提示：在不退出APP的情况下退出登陆后再重新登陆时，请确保调用本方法一次，不然会报code=203错误哦！
        IMClientManager.getInstance(this.context, this.channel).initMobileIMSDK();
        initOthers();
    }

    private void initOthers() {
        // Set MainGUI instance reference to listeners
        // * 说明：正式的APP项目中，建议在Application中管理IMClientManager类，确保SDK的生命周期同步于整个APP的生命周期
        IMClientManager.getInstance(context, channel).getTransDataListener().setMainGUI(context);
        IMClientManager.getInstance(context, channel).getBaseEventListener().setMainGUI(context);
        IMClientManager.getInstance(context, channel).getMessageQoSListener().setMainGUI(context);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("initSDK")) {
//            doLogin();
//            doSendMessage("hello flutter", "100");
            result.success("xxx success");
        } else if (call.method.equals("login")) {
            doLogin(call.arguments, result);
        } else if (call.method.equals("sendMessage")) {
            doSendMessage(call.arguments, result);
        } else {
            result.notImplemented();
        }
    }

    private void doSendMessage(Object arguments, final Result result) {
        final HashMap<String, String> map = (HashMap<String, String>) arguments;
        // 发送消息（Android系统要求必须要在独立的线程中发送哦）
        String message = map.get("message");
        String friendId = map.get("friendId");
        if (message.length() > 0 && friendId.length() > 0) {
            new LocalDataSender.SendCommonDataAsync(message, friendId)//, true)
            {
                @Override
                protected void onPostExecute(Integer code) {
                    result.success(code);
                    if (code == 0)
                        Log.d("xxx", "2数据已成功发出！");
                    else
                        Log.d("xxx", "数据发送失败。错误码是：" + code + "！");

                }
            }.execute();
        } else {
            //数据发送失败
            result.success(10000);
        }

    }


    /**
     * 真正的登陆信息发送实现方法。
     */
    private void doLogin(Object arguments, Result result) {
        final Result _result = result;
        final HashMap<String, String> map = (HashMap<String, String>) arguments;

        // 无条件重置socket，防止首次登陆时用了错误的ip或域名，下次登陆时sendData中仍然使用老的ip
        // 说明：本行代码建议仅用于Demo时，生产环境下是没有意义的，因为你的APP里不可能连IP都搞错了
        LocalSocketProvider.getInstance().closeLocalSocket();
        // * 立即显示登陆处理进度提示（并将同时启动超时检查线程）

        // * 设置好服务端反馈的登陆结果观察者（当客户端收到服务端反馈过来的登陆消息时将被通知）
        IMClientManager.getInstance(context, channel).getBaseEventListener()
                .setLoginOkForLaunchObserver(onLoginSuccessObserver);

        String user_id = map.get("username");
        String user_token = map.get("password");
        // 异步提交登陆id和token
        new LocalDataSender.SendLoginDataAsync(user_id, user_token) {
            /**
             * 登陆信息发送完成后将调用本方法（注意：此处仅是登陆信息发送完成
             * ，真正的登陆结果要在异步回调中处理哦）。
             *
             * @param code 数据发送返回码，0 表示数据成功发出，否则是错误码
             */
            @Override
            protected void fireAfterSendLogin(int code) {
                _result.success(code);
                if (code == 0) {
                    Log.d("xxx", "登陆/连接信息已成功发出！");
                } else {
                    Log.d("xxx", "数据发送失败。错误码是：" + code + "！");
                }
            }
        }.execute();
    }

}
