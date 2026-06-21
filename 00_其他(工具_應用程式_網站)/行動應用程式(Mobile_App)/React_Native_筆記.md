# React Native 筆記

```
React Native 是 Meta 開發的跨平台框架，將 JavaScript / TypeScript 元件轉換為真正的原生 iOS / Android UI 元件。
和 WebView 方案不同，React Native 渲染的是原生元件（非 HTML），效能接近原生 App。
```

## 目錄

- [React Native 筆記](#react-native-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [和其他方案的差別](#和其他方案的差別)
- [安裝](#安裝)
  - [React Native CLI（完整控制）](#react-native-cli完整控制)
  - [Expo（簡化開發）](#expo簡化開發)
  - [CLI vs Expo 比較](#cli-vs-expo-比較)
- [指令](#指令)
- [核心概念](#核心概念)
  - [元件對照（Web vs React Native）](#元件對照web-vs-react-native)
  - [StyleSheet](#stylesheet)
  - [Flexbox 差異](#flexbox-差異)
- [呼叫 API（對接後端）](#呼叫-api對接後端)
- [常見 Library](#常見-library)

## 參考資料

[React Native 官方](https://reactnative.dev/)

[Expo 官方](https://expo.dev/)

[React Navigation](https://reactnavigation.org/)

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

## React Native CLI（完整控制）

```bash
# 環境需求：Node 18+、Watchman（macOS）、JDK 17+
# iOS 需要：macOS + Xcode + CocoaPods
# Android 需要：Android Studio + Android SDK

# 建立專案
npx @react-native-community/cli@latest init MyApp

# 進入專案目錄
cd MyApp

# iOS（僅 macOS）
npx react-native run-ios

# Android
npx react-native run-android
```

## Expo（簡化開發）

```bash
# 建立專案
npx create-expo-app@latest MyApp

cd MyApp

# 啟動開發伺服器
npx expo start

# 手機安裝 Expo Go App，掃 QR code 即可預覽
# 不需要連接 Xcode / Android Studio
```

## CLI vs Expo 比較

| | React Native CLI | Expo |
|---|---|---|
| 環境設定複雜度 | 高（需裝 Xcode / Android Studio） | 低（Expo Go 手機預覽） |
| 原生模組控制 | 完整 | Expo SDK 範圍內（可 eject） |
| Build 方式 | 本地 build | 可用 EAS Build（雲端 build） |
| 推薦情境 | 需要客製化原生功能 | 快速開發、原型驗證 |

---

# 指令

```bash
# 啟動 Metro（JS bundler）
npx react-native start

# 清除快取重啟
npx react-native start --reset-cache

# 執行到 iOS 模擬器
npx react-native run-ios

# 執行到指定 iOS 裝置
npx react-native run-ios --device "iPhone 15 Pro"

# 執行到 Android
npx react-native run-android

# 建置 Release APK（Android）
cd android && ./gradlew assembleRelease
```

---

# 核心概念

## 元件對照（Web vs React Native）

React Native 沒有 HTML 標籤，使用對應的原生元件：

| Web | React Native | 說明 |
|---|---|---|
| `<div>` | `<View>` | 容器，相當於 div |
| `<p>`, `<span>` | `<Text>` | 所有文字必須包在 Text 內 |
| `<img>` | `<Image>` | 圖片（需指定尺寸） |
| `<input>` | `<TextInput>` | 輸入框 |
| `<button>` | `<TouchableOpacity>` / `<Pressable>` | 可點擊區域 |
| `<ul>` / `<li>` | `<FlatList>` | 高效能長列表 |
| `<a>` | `Linking.openURL()` | 外部連結 |
| CSS class | `StyleSheet.create()` | 樣式定義 |

## StyleSheet

```tsx
import { View, Text, StyleSheet } from 'react-native';

export default function App() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>Hello React Native</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#333',
  },
});
```

## Flexbox 差異

React Native Flexbox 與 CSS 大致相同，但預設值不同：

| 屬性 | CSS 預設 | React Native 預設 |
|---|---|---|
| `flexDirection` | `row` | `column` |
| `alignItems` | `stretch` | `stretch` |

---

# 呼叫 API（對接後端）

React Native 使用標準 `fetch` 或 `axios`：

```ts
// 各環境 API 位址差異
const BASE_URL = __DEV__
  ? 'http://10.0.2.2:5000'    // Android 模擬器（10.0.2.2 = 電腦的 localhost）
  : 'https://your-server.com'; // 生產環境

// iOS 模擬器：直接用 http://localhost:5000
// 實體裝置（iOS / Android）：用電腦的區域網路 IP（如 http://192.168.1.100:5000）
```

```ts
// 範例：fetch API 資料
const fetchData = async () => {
  try {
    const response = await fetch(`${BASE_URL}/api/data`);
    const json = await response.json();
    return json;
  } catch (error) {
    console.error('API error:', error);
  }
};
```

---

# 常見 Library

| 用途 | Library |
|---|---|
| 路由 / 導覽 | `@react-navigation/native` |
| HTTP 請求 | `axios` |
| 狀態管理 | `zustand` / `redux` |
| UI 元件庫 | `react-native-paper` / `NativeBase` |
| 圖示 | `@expo/vector-icons` / `react-native-vector-icons` |
| 相機 | `react-native-vision-camera` |
| 地圖 | `react-native-maps` |
| 推播通知 | `@notifee/react-native` |
