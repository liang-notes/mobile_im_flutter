package com.example.mobile_im_plugin;

import android.content.Context;

import com.example.mobile_im_plugin.events.ChatBaseEventImpl;
import com.example.mobile_im_plugin.events.ChatMessageEventImpl;
import com.example.mobile_im_plugin.events.MessageQoSEventImpl;

import net.x52im.mobileimsdk.android.ClientCoreSDK;
import net.x52im.mobileimsdk.android.conf.ConfigEntity;

import io.flutter.plugin.common.MethodChannel;

public class IMClientManager {
    private static String TAG = IMClientManager.class.getSimpleName();

    private static IMClientManager instance = null;

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


    public static IMClientManager getInstance() {
        if (instance == null)
            instance = new IMClientManager();
        return instance;
    }

    private IMClientManager() {
//        initMobileIMSDK();
    }

    /**
     * MobileIMSDK的初始化方法。正式的APP项目中，建议本方法在Application的子类中调用。
     */
    public void initMobileIMSDK(Context context, MethodChannel channel, String appKey, String serverIP, int serverPort, boolean debug) {
        if (!init) {
            // 设置ConfigEntity
            ConfigEntity.appKey = appKey;
            ConfigEntity.serverIP = serverIP;
            ConfigEntity.serverPort = serverPort;

            // MobileIMSDK核心IM框架的敏感度模式设置
//			ConfigEntity.setSenseMode(SenseMode.MODE_15S);

            // 设置最大TCP帧内容长度（不设置则默认最大是 6 * 1024字节）
//			LocalSocketProvider.TCP_FRAME_MAX_BODY_LENGTH = 60 * 1024;

            // 开启/关闭DEBUG信息输出
            ClientCoreSDK.DEBUG = debug;

            // 【特别注意】请确保首先进行核心库的初始化（这是不同于iOS和Java端的地方)
            ClientCoreSDK.getInstance().init(context);

            // 设置事件回调
            baseEventListener = new ChatBaseEventImpl(channel);
            transDataListener = new ChatMessageEventImpl(channel);
            messageQoSListener = new MessageQoSEventImpl(channel);
            ClientCoreSDK.getInstance().setChatBaseEvent(baseEventListener);
            ClientCoreSDK.getInstance().setChatMessageEvent(transDataListener);
            ClientCoreSDK.getInstance().setMessageQoSEvent(messageQoSListener);
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
