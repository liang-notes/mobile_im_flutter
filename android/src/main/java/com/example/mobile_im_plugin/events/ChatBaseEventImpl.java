package com.example.mobile_im_plugin.events;

import android.content.Context;
import android.util.Log;

import net.x52im.mobileimsdk.android.event.ChatBaseEvent;

import java.util.Observer;

import io.flutter.plugin.common.MethodChannel;

public class ChatBaseEventImpl implements ChatBaseEvent {
    private final static String TAG = ChatBaseEventImpl.class.getSimpleName();
    private Context mainGUI = null;
    private MethodChannel channel;
    // 本Observer目前仅用于登陆时（因为登陆与收到服务端的登陆验证结果
    // 是异步的，所以有此观察者来完成收到验证后的处理）
    private Observer loginOkForLaunchObserver = null;

    public ChatBaseEventImpl(MethodChannel channel) {
        this.channel = channel;
    }

    /**
     * 本地用户的登陆结果回调事件通知。
     *
     * @param errorCode 服务端反馈的登录结果：0 表示登陆成功，否则为服务端自定义的出错代码（按照约定通常为>=1025的数）
     */
    @Override
    public void onLoginResponse(int errorCode) {
        if (errorCode == 0) {
            Log.i(TAG, "【DEBUG_UI】IM服务器登录/重连成功！");

            // TODO 以下代码仅用于DEMO哦
        } else {
            Log.e(TAG, "【DEBUG_UI】IM服务器登录/连接失败，错误代码：" + errorCode);

            // TODO 以下代码仅用于DEMO哦
        }

        // 此观察者只有开启程序首次使用登陆界面时有用
        if (loginOkForLaunchObserver != null) {
            loginOkForLaunchObserver.update(null, errorCode);
            loginOkForLaunchObserver = null;
        }
    }

    /**
     * 与服务端的通信断开的回调事件通知。
     * <br>
     * 该消息只有在客户端连接服务器成功之后网络异常中断之时触发。<br>
     * 导致与与服务端的通信断开的原因有（但不限于）：无线网络信号不稳定、WiFi与2G/3G/4G等同开情况下的网络切换、手机系统的省电策略等。
     *
     * @param errorCode 本回调参数表示表示连接断开的原因，目前错误码没有太多意义，仅作保留字段，目前通常为-1
     */
    @Override
    public void onLinkClose(int errorCode) {
        Log.e(TAG, "【DEBUG_UI】与IM服务器的网络连接出错关闭了，error：" + errorCode);

        // TODO 以下代码仅用于DEMO哦
    }

    public void setLoginOkForLaunchObserver(Observer loginOkForLaunchObserver) {
        this.loginOkForLaunchObserver = loginOkForLaunchObserver;
    }

    public ChatBaseEventImpl setMainGUI(Context mainGUI) {
        this.mainGUI = mainGUI;
        return this;
    }

}
