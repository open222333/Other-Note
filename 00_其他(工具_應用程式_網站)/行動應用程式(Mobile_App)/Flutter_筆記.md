# Flutter 筆記

```
Flutter 是 Google 開發的跨平台 UI 框架，使用 Dart 語言，透過自繪引擎（Impeller）渲染 UI，不依賴原生元件。
可同時部署至 iOS、Android、Web、桌面（Windows / macOS / Linux）。
```

## 目錄

- [Flutter 筆記](#flutter-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [和其他方案的差別](#和其他方案的差別)
- [安裝](#安裝)
  - [Debian (Ubuntu)](#debian-ubuntu)
  - [Homebrew (MacOS)](#homebrew-macos)
  - [確認環境](#確認環境)
- [指令](#指令)
- [核心概念](#核心概念)
  - [一切皆 Widget](#一切皆-widget)
  - [常用 Layout Widget](#常用-layout-widget)
  - [StatefulWidget 狀態管理](#statefulwidget-狀態管理)
- [呼叫 API（對接後端）](#呼叫-api對接後端)
- [常見 Package](#常見-package)

## 參考資料

[Flutter 官方](https://flutter.dev/)

[Dart 官方](https://dart.dev/)

[pub.dev — Flutter 套件庫](https://pub.dev/)

[Flutter Widget Catalog](https://docs.flutter.dev/ui/widgets)

---

# 和其他方案的差別

| 方案 | 原理 | 適合情境 |
|---|---|---|
| Capacitor | WebView 包 Web | 已有 Web App，想快速上架 |
| React Native | JS → 原生元件渲染 | 需要更接近原生 UI 效能 |
| Flutter | Dart → 自繪 UI | 追求最高效能 / 跨平台一致 |
| PWA | 純瀏覽器 | 不需上架，手機加入主畫面即可 |

---

# 安裝

## Debian (Ubuntu)

```bash
sudo snap install flutter --classic
flutter sdk-path
```

## Homebrew (MacOS)

```bash
brew install --cask flutter
```

## 確認環境

```bash
# 列出所有環境問題
flutter doctor

# flutter doctor 會檢查：
# ✓ Flutter SDK
# ✓ Android toolchain（Android Studio / SDK）
# ✓ Xcode（macOS）
# ✓ CocoaPods（iOS 需要）
# ✓ VS Code / Android Studio 插件
```

---

# 指令

```bash
# 建立專案
flutter create my_app

cd my_app

# 列出可用裝置
flutter devices

# 執行（互動選擇裝置）
flutter run

# 執行到指定平台
flutter run -d ios
flutter run -d android
flutter run -d chrome        # Web
flutter run -d macos         # 桌面

# 建置
flutter build apk            # Android APK
flutter build appbundle      # Android AAB（上架 Google Play）
flutter build ios            # iOS（需 macOS + Xcode）

# 清除建置快取
flutter clean

# 取得 / 更新套件
flutter pub get
flutter pub upgrade
```

---

# 核心概念

## 一切皆 Widget

Flutter UI 完全由 Widget 組成，Widget 是不可變的描述，State 改變時重建 Widget。

| 類型 | 說明 |
|---|---|
| `StatelessWidget` | 無狀態，純 UI，不會因外部輸入改變 |
| `StatefulWidget` | 有狀態，`setState()` 觸發重新渲染 |

```dart
import 'package:flutter/material.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'My App',
      home: Scaffold(
        appBar: AppBar(title: const Text('Hello Flutter')),
        body: const Center(
          child: Text('Hello World', style: TextStyle(fontSize: 24)),
        ),
      ),
    );
  }
}
```

## 常用 Layout Widget

| Widget | 功能 | Web 類比 |
|---|---|---|
| `Column` | 垂直排列子元件 | `flex-direction: column` |
| `Row` | 水平排列子元件 | `flex-direction: row` |
| `Stack` | 重疊排列 | `position: absolute` |
| `Container` | 有 padding / margin / decoration 的容器 | `<div>` |
| `Expanded` | 填滿剩餘空間 | `flex: 1` |
| `Padding` | 只加 padding | `padding` |
| `SizedBox` | 固定尺寸 / 間距 | `width` / `height` |
| `ListView` | 可捲動列表 | `overflow-y: scroll` |
| `GridView` | 格狀佈局 | CSS Grid |

## StatefulWidget 狀態管理

```dart
class CounterPage extends StatefulWidget {
  const CounterPage({super.key});

  @override
  State<CounterPage> createState() => _CounterPageState();
}

class _CounterPageState extends State<CounterPage> {
  int _count = 0;

  void _increment() {
    setState(() {
      _count++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(child: Text('Count: $_count', style: const TextStyle(fontSize: 32))),
      floatingActionButton: FloatingActionButton(
        onPressed: _increment,
        child: const Icon(Icons.add),
      ),
    );
  }
}
```

---

# 呼叫 API（對接後端）

```yaml
# pubspec.yaml
dependencies:
  http: ^1.2.0
```

```bash
flutter pub get
```

```dart
import 'package:http/http.dart' as http;
import 'dart:convert';

const String baseUrl = 'https://your-server.com';

// GET 請求
Future<List<dynamic>> fetchItems() async {
  final response = await http.get(Uri.parse('$baseUrl/api/items'));

  if (response.statusCode == 200) {
    return jsonDecode(response.body) as List<dynamic>;
  }
  throw Exception('Failed to load: ${response.statusCode}');
}

// POST 請求
Future<void> createItem(Map<String, dynamic> data) async {
  final response = await http.post(
    Uri.parse('$baseUrl/api/items'),
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode(data),
  );

  if (response.statusCode != 201) {
    throw Exception('Failed to create');
  }
}
```

`在 Widget 中使用`

```dart
class ItemListPage extends StatefulWidget {
  const ItemListPage({super.key});

  @override
  State<ItemListPage> createState() => _ItemListPageState();
}

class _ItemListPageState extends State<ItemListPage> {
  late Future<List<dynamic>> _items;

  @override
  void initState() {
    super.initState();
    _items = fetchItems();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<dynamic>>(
      future: _items,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const CircularProgressIndicator();
        }
        if (snapshot.hasError) return Text('Error: ${snapshot.error}');
        final items = snapshot.data!;
        return ListView.builder(
          itemCount: items.length,
          itemBuilder: (context, index) => ListTile(title: Text(items[index]['name'])),
        );
      },
    );
  }
}
```

---

# 常見 Package

| 用途 | Package |
|---|---|
| HTTP 請求 | `http` / `dio` |
| 狀態管理 | `provider` / `riverpod` / `bloc` |
| 路由 | `go_router` |
| 本地儲存 | `shared_preferences` |
| JSON 序列化 | `json_serializable` |
| 圖示 | `flutter_svg` / `font_awesome_flutter` |
| 相機 | `camera` |
| 地圖 | `google_maps_flutter` |
| 推播通知 | `firebase_messaging` |
