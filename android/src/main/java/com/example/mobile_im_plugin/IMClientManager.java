package com.example.mobile_im_plugin;

import android.content.Context;
import android.util.Log;

import com.example.mobile_im_plugin.events.ChatBaseEventImpl;
import com.example.mobile_im_plugin.events.ChatMessageEventImpl;
import com.example.mobile_im_plugin.events.MessageQoSEventImpl;

import net.x52im.mobileimsdk.android.ClientCoreSDK;
import net.x52im.mobileimsdk.android.conf.ConfigEntity;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class IMClientManager  {
    private static String TAG = IMClientManager.class.getSimpleName();

    private static IMClientManager instance = null;

    private MethodChannel channel;

    /**
     * MobileIMSDK是否已被初始化. true表示已初化完成，否则未初始化.
     */
    private boolean init = false;

    /**
     * 基本连接状态事件监听器
     */
    private ChatBaseEventImpl baseEventListener = null;
    /**
     * 数据接收事件监听器
     */
    private ChatMessageEventImpl transDataListener = null;
    /**
     * 消息送达保证事件监听器
     */
    private MessageQoSEventImpl messageQoSListener = null;

    private Context context = null;

    public static IMClientManager getInstance(Context context,  MethodChannel channel) {
        if (instance == null)
            instance = new IMClientManager(context,channel);
        return instance;
    }

    private IMClientManager(Context context, MethodChannel channel) {
        this.context = context;
        this.channel = channel;
        initMobileIMSDK();
    }

    /**
     * MobileIMSDK的初始化方法。正式的APP项目中，建议本方法在Application的子类中调用。
     */
    public void initMobileIMSDK() {
        if (!init) {
            // 设置AppKey
            ConfigEntity.appKey = "5418023dfd98c579b6001741";
            Log.d("xxx-context", "initMobileIMSDK "+ context);

            // 设置服务器ip和服务器端口
//			ConfigEntity.serverIP = "192.168.82.138";
//			ConfigEntity.serverPort = 8901;

			ConfigEntity.serverIP = "rbcore.52im.net";
			ConfigEntity.serverPort = 8901;

            // MobileIMSDK核心IM框架的敏感度模式设置
//			ConfigEntity.setSenseMode(SenseMode.MODE_15S);

            // 设置最大TCP帧内容长度（不设置则默认最大是 6 * 1024字节）
//			LocalSocketProvider.TCP_FRAME_MAX_BODY_LENGTH = 60 * 1024;

            // 开启/关闭DEBUG信息输出
//	    	ClientCoreSDK.DEBUG = false;

            // 【特别注意】请确保首先进行核心库的初始化（这是不同于iOS和Java端的地方)
            ClientCoreSDK.getInstance().init(this.context);

            // 设置事件回调
            baseEventListener = new ChatBaseEventImpl(this.channel);
            transDataListener = new ChatMessageEventImpl(this.channel);
            messageQoSListener = new MessageQoSEventImpl();
            ClientCoreSDK.getInstance().setChatBaseEvent(baseEventListener);
            ClientCoreSDK.getInstance().setChatMessageEvent(transDataListener);
            ClientCoreSDK.getInstance().setMessageQoSEvent(messageQoSListener);
            Log.d("xxx", "initMobileIMSDK");
            init = true;
        }
    }

    public void release() {
        ClientCoreSDK.getInstance().release();
        resetInitFlag();
    }



    /**
     * 重置init标识。
     * <p>
     * <b>重要说明：</b>不退出APP的情况下，重新登陆时记得调用一下本方法，不然再
     * 次调用 {@link #initMobileIMSDK()} 时也不会重新初始化MobileIMSDK（
     * 详见 {@link #initMobileIMSDK()}代码）而报 code=203错误！
     */
    public void resetInitFlag() {
        init = false;
    }

    public ChatMessageEventImpl getTransDataListener() {
        return transDataListener;
    }

    public ChatBaseEventImpl getBaseEventListener() {
        return baseEventListener;
    }

    public MessageQoSEventImpl getMessageQoSListener() {
        return messageQoSListener;
    }

}
