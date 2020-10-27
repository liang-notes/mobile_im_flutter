import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:mobile_im_plugin/mobile_im_plugin.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _result = '';

  @override
  void initState() {
    super.initState();
    initPlatformState();

    MobileImPlugin.streamController.stream.listen((message) {
      print('收到的消息====$message');
      setState(() {
        _result = message;
      });
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    await MobileImPlugin.getInstance().init(
      serverIP: '192.168.82.138',
      serverPort: 7901,
    );
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await MobileImPlugin.getInstance().platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(children: [
          Center(
            child: Text('Running on: $_platformVersion\n hhh'),
          ),
          FlatButton(
            onPressed: () async {
              final status =
                  await MobileImPlugin.getInstance().login('1', '123');
              print('xxx===$status');
              setState(() {});
            },
            child: Text('login'),
          ),
          FlatButton(
            onPressed: () async {
              final status = await MobileImPlugin.getInstance()
                  .send('hello flutter plugin', '2');
              print('xxx===$status');
            },
            child: Text('send message'),
          ),
          Center(
            child: Text('result: $_result\n'),
          ),
        ]),
      ),
    );
  }
}
