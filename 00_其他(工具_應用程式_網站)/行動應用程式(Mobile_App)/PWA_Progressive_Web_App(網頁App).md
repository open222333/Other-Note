# PWA (Progressive Web App)

```
PWA 是可以像原生 App 一樣安裝到手機桌面的網頁應用程式，不需上架 App Store / Google Play。
核心技術：manifest.json（App 基本資訊）+ Service Worker（離線快取與背景同步）。
```

## 目錄

- [PWA (Progressive Web App)](#pwa-progressive-web-app)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [核心概念](#核心概念)
  - [三個必要條件](#三個必要條件)
- [manifest.json](#manifestjson)
- [Service Worker](#service-worker)
  - [註冊 Service Worker](#註冊-service-worker)
  - [Service Worker 生命週期](#service-worker-生命週期)
  - [快取策略](#快取策略)
- [Vue + Vite 整合（vite-plugin-pwa）](#vue--vite-整合vite-plugin-pwa)
- [iOS vs Android 差異](#ios-vs-android-差異)
- [和原生 App 的比較](#和原生-app-的比較)

## 參考資料

[MDN Web App Manifest](https://developer.mozilla.org/en-US/docs/Web/Manifest)

[MDN Service Worker API](https://developer.mozilla.org/en-US/docs/Web/API/Service_Worker_API)

[PWA Builder — Microsoft](https://www.pwabuilder.com/)

[vite-plugin-pwa](https://vite-pwa-org.netlify.app/)

---

# 核心概念

## 三個必要條件

| 條件 | 說明 |
|---|---|
| HTTPS | Service Worker 只在安全來源運作（localhost 例外） |
| manifest.json | 定義 App 名稱、圖示、顯示模式 |
| Service Worker | 背景執行的 JS，負責快取與離線功能 |

---

# manifest.json

```json
{
  "name": "My App",
  "short_name": "App",
  "description": "App 描述",
  "display": "standalone",
  "orientation": "portrait",
  "start_url": "/",
  "background_color": "#ffffff",
  "theme_color": "#000000",
  "icons": [
    { "src": "/icons/icon-192.png", "sizes": "192x192", "type": "image/png" },
    { "src": "/icons/icon-512.png", "sizes": "512x512", "type": "image/png" },
    { "src": "/icons/icon-512.png", "sizes": "512x512", "type": "image/png", "purpose": "maskable" }
  ]
}
```

| `display` 值 | 說明 |
|---|---|
| `standalone` | 全螢幕，隱藏瀏覽器 UI（最常用） |
| `fullscreen` | 隱藏系統狀態列 |
| `minimal-ui` | 保留最少瀏覽器控制項 |
| `browser` | 一般瀏覽器分頁（無安裝效果） |

`HTML head 引入`

```html
<head>
  <link rel="manifest" href="/manifest.json">
  <meta name="theme-color" content="#000000">
  <!-- iOS 額外需要 -->
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="default">
  <meta name="apple-mobile-web-app-title" content="My App">
  <link rel="apple-touch-icon" href="/icons/icon-192.png">
</head>
```

---

# Service Worker

Service Worker 是獨立於網頁的背景執行緒，負責攔截網路請求、管理快取、推播通知。

## 註冊 Service Worker

```js
// main.js 或 index.html 的 <script>
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js')
      .then(reg => console.log('SW registered:', reg.scope))
      .catch(err => console.error('SW register failed:', err));
  });
}
```

## Service Worker 生命週期

```
install → activate → fetch（攔截請求）
```

```js
// public/sw.js
const CACHE_NAME = 'my-app-v1';
const ASSETS = ['/', '/index.html', '/main.js', '/style.css'];

// 安裝：快取靜態資源
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME).then(cache => cache.addAll(ASSETS))
  );
  self.skipWaiting();
});

// 啟用：清除舊快取
self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys().then(keys =>
      Promise.all(keys.filter(k => k !== CACHE_NAME).map(k => caches.delete(k)))
    )
  );
  self.clients.claim();
});

// 攔截請求：Cache First 策略
self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request).then(cached => cached || fetch(event.request))
  );
});
```

## 快取策略

| 策略 | 說明 | 適合情境 |
|---|---|---|
| Cache First | 先查快取，無快取才發請求 | 靜態資源（JS、CSS、圖片） |
| Network First | 先發請求，失敗才用快取 | API 資料（需要最新） |
| Cache Only | 只查快取，沒有就失敗 | 完全離線場景 |
| Network Only | 只發請求，不用快取 | 即時資料 |
| Stale While Revalidate | 先回快取，同時背景更新快取 | 允許略舊資料但需要速度 |

---

# Vue + Vite 整合（vite-plugin-pwa）

Vite 專案可用 `vite-plugin-pwa` 自動產生 Service Worker 與 manifest，不需手動維護 sw.js。

```bash
npm install -D vite-plugin-pwa
```

```ts
// vite.config.ts
import { VitePWA } from 'vite-plugin-pwa';

export default {
  plugins: [
    VitePWA({
      registerType: 'autoUpdate',
      manifest: {
        name: 'My App',
        short_name: 'App',
        theme_color: '#000000',
        icons: [
          { src: '/icons/icon-192.png', sizes: '192x192', type: 'image/png' },
          { src: '/icons/icon-512.png', sizes: '512x512', type: 'image/png', purpose: 'maskable' },
        ],
      },
      workbox: {
        globPatterns: ['**/*.{js,css,html,ico,png,svg,woff2}'],
        runtimeCaching: [
          {
            urlPattern: /^https:\/\/your-api\.com\/.*/i,
            handler: 'NetworkFirst',
            options: {
              cacheName: 'api-cache',
              expiration: { maxEntries: 100, maxAgeSeconds: 60 * 60 * 24 },
            },
          },
        ],
      },
    }),
  ],
};
```

---

# iOS vs Android 差異

| 功能 | iOS (Safari) | Android (Chrome) |
|---|---|---|
| 安裝提示 | 需手動「加入主畫面」，無自動橫幅 | 自動顯示安裝橫幅 |
| 推播通知 | iOS 16.4+（需先加入主畫面才能授權） | 完整支援 |
| 後台同步 | 不支援 Background Sync | 支援 |
| Service Worker 快取上限 | 約 50 MB | 較寬鬆 |
| 更新機制 | Safari 控制，較不穩定 | 較可靠 |

---

# 和原生 App 的比較

| 項目 | PWA | 原生 App（Capacitor / RN） |
|---|---|---|
| 上架流程 | 不需要 | 需 App Store / Google Play |
| 安裝方式 | 瀏覽器直接安裝 | App Store 安裝 |
| 原生硬體存取 | 受限（無藍牙、NFC、背景 GPS） | 完整支援 |
| 離線支援 | Service Worker 快取 | 完整支援 |
| 更新方式 | 自動（伺服器更新即生效） | 需使用者更新 App |
| 開發成本 | 最低（純 Web） | 需額外打包流程 |
| 適合情境 | 不需上架、輕量化需求 | 需原生功能、正式上架 |
