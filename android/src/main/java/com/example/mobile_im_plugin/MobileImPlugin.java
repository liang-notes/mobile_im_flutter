package com.example.mobile_im_plugin;

import android.content.Context;
import android.os.AsyncTask;
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
    private final static String TAG = MobileImPlugin.class.getSimpleName();
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
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    private void initClientManager(Object arguments) {
        // 确保MobileIMSDK被初始化哦（整个APP生生命周期中只需调用一次哦）
        // 提示：在不退出APP的情况下退出登陆后再重新登陆时，请确保调用本方法一次，不然会报code=203错误哦！
        final HashMap map = (HashMap) arguments;
        String appKey = (String) map.get("appKey");
        String serverIP = (String) map.get("serverIP");
        int serverPort = (int) map.get("serverPort");
        boolean debug = (boolean) map.get("debug");
        IMClientManager.getInstance().initMobileIMSDK(context, channel, appKey, serverIP, serverPort, debug);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("init")) {
            initClientManager(call.arguments);
        } else if (call.method.equals("login")) {
            login(call.arguments, result);
        } else if (call.method.equals("logout")) {
            logout(result);
        } else if (call.method.equals("send")) {
            doSendMessage(call.arguments, result);
        } else {
            result.notImplemented();
        }
    }

    private void logout(final Result result) {

        new AsyncTask<Object, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Object... params) {
                int code = -1;
                try {
                    code = LocalDataSender.getInstance().sendLoginout();
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
                // 退出登陆时记得一定要调用此行，不然不退出APP的情况下再登陆时会报 code=203错误哦！
                IMClientManager.getInstance().resetInitFlag();
                return code;
            }

            @Override
            protected void onPostExecute(Integer code) {
                if (code == 0)
                    Log.d(TAG, "注销登陆请求已完成！");
                else
                    Log.d(TAG, "注销登陆请求发送失败(错误码:" + code + ")");
                result.success(code);
            }
        }.execute();
    }

    private void doSendMessage(Object arguments, final Result result) {
        final HashMap<String, String> map = (HashMap<String, String>) arguments;
        String message = map.get("message");
        String toUserId = map.get("toUserId");
        if (message.length() > 0 && toUserId.length() > 0) {
            // 发送消息（Android系统要求必须要在独立的线程中发送）
            new LocalDataSender.SendCommonDataAsync(message, toUserId) {
                @Override
                protected void onPostExecute(Integer code) {
                    result.success(code);
                    if (code == 0)
                        Log.d(TAG, "数据已成功发出");
                    else
                        Log.d(TAG, "数据发送失败(错误码:" + code + ")");

                }
            }.execute();
        } else {
            //自定义错误码
            result.success(10000);
        }
    }


    private void login(Object arguments, final Result result) {
        final HashMap<String, String> map = (HashMap<String, String>) arguments;
        // 无条件重置socket，防止首次登陆时用了错误的ip或域名，下次登陆时sendData中仍然使用老的ip
        // 说明：本行代码建议仅用于Demo时，生产环境下是没有意义的，因为你的APP里不可能连IP都搞错了
        LocalSocketProvider.getInstance().closeLocalSocket();
        // * 立即显示登陆处理进度提示（并将同时启动超时检查线程）
        // * 设置好服务端反馈的登陆结果观察者（当客户端收到服务端反馈过来的登陆消息时将被通知）
        IMClientManager.getInstance().getBaseEventListener()
                .setLoginOkForLaunchObserver(onLoginSuccessObserver);

        String user_id = map.get("username");
        String user_token = map.get("password");
        // 异步提交登陆id和token
        new LocalDataSender.SendLoginDataAsync(user_id, user_token) {
            @Override
            protected void fireAfterSendLogin(int code) {
                if (code == 0) {
                    Log.d(TAG, "登陆信息已成功发出！");
                } else {
                    Log.d(TAG, "登录信息发送失败(错误码:" + code + ")");

                }
                result.success(code);
            }
        }.execute();
    }

}
