import 'package:flutter/material.dart';
import 'dart:async';
import 'package:mobile_im_plugin/mobile_im_plugin.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _result = '';

  @override
  void initState() {
    super.initState();
    initMobileImPlugin();

    MobileImPlugin.streamController.stream.listen((message) {
      print('收到的消息====$message');
      setState(() {
        _result = message;
      });
    });
  }

  Future<void> initMobileImPlugin() async {
    await MobileImPlugin.getInstance().init(
      serverIP: '192.168.82.138',
      serverPort: 7901,
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
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
          ],
        ),
      ),
    );
  }
}
