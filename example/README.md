# mobile_im_plugin_example

Demonstrates how to use the mobile_im_plugin plugin.

## Getting Started

In the pubspec.yaml of your flutter project, add the following dependency:
```
dependencies:
  mobile_im_plugin: 0.0.1
```

In your library add the following import:
```
import 'package:mobile_im_plugin/mobile_im_plugin.dart';

```

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs)

## Example

```
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
```