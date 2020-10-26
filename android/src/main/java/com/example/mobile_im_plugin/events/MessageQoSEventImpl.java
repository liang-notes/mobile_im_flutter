package com.example.mobile_im_plugin.events;

import android.content.Context;
import android.util.Log;

import net.x52im.mobileimsdk.android.event.MessageQoSEvent;
import net.x52im.mobileimsdk.server.protocal.Protocal;

import java.util.ArrayList;

import io.flutter.plugin.common.MethodChannel;

public class MessageQoSEventImpl implements MessageQoSEvent {
    private final static String TAG = MessageQoSEventImpl.class.getSimpleName();

    private Context mainGUI = null;
    private MethodChannel channel;

    public MessageQoSEventImpl(MethodChannel channel) {
        this.channel = channel;
    }

    /**
     * 消息未送达的回调事件通知.
     *
     * @param lostMessages 由MobileIMSDK QoS算法判定出来的未送达消息列表（此列表中的Protocal对象是原对象的
     *                     clone（即原对象的深拷贝），请放心使用哦），应用层可通过指纹特征码找到原消息并可
     *                     以UI上将其标记为”发送失败“以便即时告之用户
     * @see net.x52im.mobileimsdk.server.protocal.Protocal
     */
    @Override
    public void messagesLost(ArrayList<Protocal> lostMessages) {
        Log.d(TAG, "【DEBUG_UI】收到系统的未实时送达事件通知，当前共有" + lostMessages.size() + "个包QoS保证机制结束，判定为【无法实时送达】！");
    }

    /**
     * 消息已被对方收到的回调事件通知.
     * <p>
     * <b>目前，判定消息被对方收到是有两种可能：</b><br>
     * <ul>
     * <li>1) 对方确实是在线并且实时收到了；</li>
     * <li>2) 对方不在线或者服务端转发过程中出错了，由服务端进行离线存储成功后的反馈（此种情况严格来讲不能算是“已被
     * 		收到”，但对于应用层来说，离线存储了的消息原则上就是已送达了的消息：因为用户下次登陆时肯定能通过HTTP协议取到）。</li>
     * </ul>
     *
     * @param theFingerPrint 已被收到的消息的指纹特征码（唯一ID），应用层可据此ID来找到原先已发生的消息并可在
     *                       UI是将其标记为”已送达“或”已读“以便提升用户体验
     * @see net.openmob.mobileimsdk.server.protocal.Protocal
     */
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
