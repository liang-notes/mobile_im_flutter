package com.example.mobile_im_plugin.events;

import android.content.Context;
import android.util.Log;


import net.openmob.mobileimsdk.android.event.ChatTransDataEvent;

import io.flutter.plugin.common.MethodChannel;

public class ChatMessageEventImpl implements ChatTransDataEvent {
    private final static String TAG = ChatMessageEventImpl.class.getSimpleName();
    private Context mainGUI = null;
    private MethodChannel channel;

    public ChatMessageEventImpl(MethodChannel channel) {
        this.channel = channel;
    }

    @Override
    public void onTransBuffer(String fingerPrintOfProtocal, String userid, String dataContent, int typeu) {
        Log.d(TAG, "【DEBUG_UI】[typeu=" + typeu + "]收到来自用户" + userid + "的消息:" + dataContent);
        this.channel.invokeMethod("onReceiveMessage", dataContent);
    }

    @Override
    public void onErrorResponse(int errorCode, String errorMsg) {
        Log.d(TAG, "【DEBUG_UI】收到服务端错误消息，errorCode=" + errorCode + ", errorMsg=" + errorMsg);
    }

    public ChatMessageEventImpl setMainGUI(Context mainGUI) {
        this.mainGUI = mainGUI;
        return this;
    }

}
