package com.example.mobile_im_plugin.events;

import android.content.Context;
import android.util.Log;


import net.openmob.mobileimsdk.android.event.MessageQoSEvent;
import net.openmob.mobileimsdk.server.protocal.Protocal;

import java.util.ArrayList;

import io.flutter.plugin.common.MethodChannel;

public class MessageQoSEventImpl implements MessageQoSEvent {
    private final static String TAG = MessageQoSEventImpl.class.getSimpleName();

    private Context mainGUI = null;
    private MethodChannel channel;

    public MessageQoSEventImpl(MethodChannel channel) {
        this.channel = channel;
    }

    @Override
    public void messagesLost(ArrayList<Protocal> lostMessages) {
        Log.d(TAG, "【DEBUG_UI】收到系统的未实时送达事件通知，当前共有" + lostMessages.size() + "个包QoS保证机制结束，判定为【无法实时送达】！");
    }

    @Override
    public void messagesBeReceived(String theFingerPrint) {
        if (theFingerPrint != null) {
            Log.d(TAG, "【DEBUG_UI】收到对方已收到消息事件的通知，fp=" + theFingerPrint);
        }
    }

    public MessageQoSEventImpl setMainGUI(Context mainGUI) {
        this.mainGUI = mainGUI;
        return this;
    }

}
