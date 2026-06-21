# Capacitor 筆記

```
Capacitor 是 Ionic 團隊開發的跨平台工具，將現有 Web App（Vue / React）打包成原生 iOS / Android App。
核心原理：用 WKWebView（iOS）或 WebView（Android）顯示 Web 內容，透過 JS Bridge 呼叫原生 API。
```

## 目錄

- [Capacitor 筆記](#capacitor-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [解決的核心問題](#解決的核心問題)
- [和其他方案的差別](#和其他方案的差別)
- [安裝](#安裝)
  - [環境需求](#環境需求)
- [指令](#指令)
  - [初始化](#初始化)
  - [常用指令](#常用指令)
- [配置文檔](#配置文檔)
  - [本機模式 vs 遠端模式](#本機模式-vs-遠端模式)
- [Plugin 系統](#plugin-系統)
  - [常用官方 Plugin](#常用官方-plugin)
- [更新流程](#更新流程)
- [Vue + Flask 架構注意事項](#vue--flask-架構注意事項)

## 參考資料

[Capacitor 官方](https://capacitorjs.com/)

[Capacitor Plugins](https://capacitorjs.com/docs/plugins)

[Capacitor Config](https://capacitorjs.com/docs/config)

---

# 解決的核心問題

| 問題 | 解決方式 |
|---|---|
| 想上架但不想重寫原生 Swift / Kotlin | 用現有 Vue 程式碼直接包 |
| 需要呼叫手機硬體（相機、GPS） | 透過 Capacitor Plugin（JS 橋接到原生 API） |
| Web 無法安裝到桌面 | 打包後走正常 App 安裝流程 |

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

## 環境需求

| 平台 | 需求 |
|---|---|
| iOS | macOS + Xcode |
| Android | Android Studio（Windows / macOS / Linux 皆可） |

```bash
# 安裝核心套件
npm install @capacitor/core @capacitor/cli

# iOS 平台
npm install @capacitor/ios

# Android 平台
npm install @capacitor/android
```

---

# 指令

## 初始化

```bash
# 在前端專案根目錄執行
npx cap init "App Name" "com.example.appname"

# 加入平台（擇一或兩者都加）
npx cap add ios
npx cap add android
```

## 常用指令

```bash
# 建置前端後同步（最常用）
npm run build
npx cap sync

# 只複製 web 資源（不更新原生依賴）
npx cap copy

# 用原生 IDE 開啟
npx cap open ios
npx cap open android

# 查看已安裝 Plugin
npx cap ls
```

| 指令 | 說明 |
|---|---|
| `cap sync` | `cap copy` + 更新原生 Plugin 依賴（新增 plugin 後用） |
| `cap copy` | 只複製 web build 到原生專案（只改前端時用，較快） |
| `cap open` | 用原生 IDE 開啟，後續在 IDE 執行 build / run |

---

# 配置文檔

```ts
// capacitor.config.ts
import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.example.appname',
  appName: 'My App',
  webDir: 'dist',    // Vue build 輸出目錄（npm run build 的輸出位置）
  server: {
    // 本機模式：不設 url，讀取 dist/ 靜態檔案（離線可用）
    // url: 'https://your-server.com',  // 遠端模式
  },
};

export default config;
```

## 本機模式 vs 遠端模式

| 模式 | 設定 | 特點 |
|---|---|---|
| 本機模式 | 不設 `server.url` | 離線可用，前端更新需重新 `cap sync` + rebuild |
| 遠端模式 | 設 `server.url` | 伺服器更新即時生效，不需重打包 App |

---

# Plugin 系統

Capacitor Plugin 讓 JS 橋接到原生 API：

```bash
# 安裝 Plugin 後必須執行 cap sync
npm install @capacitor/camera
npx cap sync
```

```ts
import { Camera, CameraResultType } from '@capacitor/camera';

const photo = await Camera.getPhoto({
  resultType: CameraResultType.Uri,
  quality: 90,
});
console.log(photo.webPath);
```

## 常用官方 Plugin

| Plugin | 功能 |
|---|---|
| `@capacitor/camera` | 相機 / 相簿 |
| `@capacitor/geolocation` | GPS 定位 |
| `@capacitor/push-notifications` | 推播通知 |
| `@capacitor/preferences` | 本地鍵值儲存 |
| `@capacitor/network` | 網路狀態偵測 |
| `@capacitor/filesystem` | 檔案系統存取 |
| `@capacitor/app` | App 狀態（前景 / 背景） |

---

# 更新流程

| 變動類型 | 需要 | 不需要 |
|---|---|---|
| 前端 HTML / JS / CSS（本機模式） | `npm run build` + `cap sync` | 重裝 App |
| 前端（遠端模式，`server.url`） | 直接部署伺服器 | build / sync / 重裝 |
| 新增 Capacitor Plugin | `cap sync` + IDE rebuild + 重裝 | - |
| `Info.plist` / `AndroidManifest.xml` 變動 | IDE rebuild + 重裝 | - |

---

# Vue + Flask 架構注意事項

詳見各平台打包筆記的「Capacitor → Vue + 後端分離架構」章節：

- [iOS 建置與打包](./iOS_建置與打包.md)
- [Android 建置與打包](./Android_建置與打包.md)

重點摘要：

1. **API base URL**：開發用 Vite proxy，生產改絕對 HTTPS
2. **iOS ATS**：正式環境需 HTTPS，開發測試才允許 HTTP
3. **Android cleartext**：同上，`usesCleartextTraffic="true"` 僅開發用
4. **CORS**：後端需允許 `capacitor://localhost`（iOS）與 `http://localhost`（Android）
