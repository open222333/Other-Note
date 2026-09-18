# Android 網路抓包(HTTP攔截除錯)

```
用途：追查 Android app（含只有打包好的 APK、沒有原始碼）實際呼叫的 API 網域與內容。
依有無原始碼分兩種做法：有原始碼用 Android Studio Network Profiler；已打包 APK 用外接 Proxy 攔截（HTTP Toolkit / mitmproxy / Charles 皆可）。
```

## 目錄

- [Android 網路抓包(HTTP攔截除錯)](#android-網路抓包http攔截除錯)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [安裝](#安裝)
	- [HTTP Toolkit](#http-toolkit)
- [配置文檔](#配置文檔)
	- [BlueStacks 開啟 ADB 與 Root access](#bluestacks-開啟-adb-與-root-access)
- [指令](#指令)
	- [方法一：有原始碼 —— Android Studio 開專案 + Network Profiler](#方法一有原始碼-android-studio-開專案--network-profiler)
	- [方法二：已打包 APK —— 通用 Proxy 攔截法](#方法二已打包-apk-通用-proxy-攔截法)
	- [HTTP Toolkit 操作流程](#http-toolkit-操作流程)
	- [BlueStacks 搭配 HTTP Toolkit 連線](#bluestacks-搭配-http-toolkit-連線)
	- [只想確認打到哪個網域（不用解密）](#只想確認打到哪個網域不用解密)
- [例外狀況](#例外狀況)
	- [app 做 certificate pinning，代理抓不到明文內容](#app-做-certificate-pinning代理抓不到明文內容)
	- [adb: command not found（macOS）](#adb-command-not-foundmacos)

## 參考資料

[HTTP Toolkit 官網](https://httptoolkit.com/)

[Chrome DevTools 追蹤 API 呼叫（Web 版做法，原理相同）](../../影像工具(圖片處理)/瀏覽器開發者工具(偵錯工具)/Chrome(Google瀏覽器).md)

[mitmproxy 筆記（另一套可替代 HTTP Toolkit 的攔截工具）](../../../02_作業系統/Unix/Linux/Linux_工具/Linux_工具_mitmproxy(中間人代理工具).md)

# 安裝

## HTTP Toolkit

```bash
# 官方下載（Mac / Windows / Linux 都有），免費版就夠用
# https://httptoolkit.com/download

# macOS 也可用 Homebrew
brew install --cask http-toolkit
```

# 配置文檔

## BlueStacks 開啟 ADB 與 Root access

齒輪 `Settings > Advanced`：

- `Android Debug Bridge`：打開後畫面會顯示連線用的 `IP:Port`（通常是 `127.0.0.1:5555`，多開實例可能不同，以畫面顯示為準）
- `Root access`：切成 `Enabled` 並重啟該實例（僅 BlueStacks 5 + Pie 64-bit 映像才有此選項；沒有就跳過，一般網站/API 抓包通常不需要）
- Root access 打開後，HTTP Toolkit 的 CA 憑證才能裝成「系統層級信任」，不然 Android 7+ 預設不信任使用者憑證，HTTPS 會直接看不到內容

# 指令

## 方法一：有原始碼 —— Android Studio 開專案 + Network Profiler

1. `File > Open` 選專案根目錄（含 `build.gradle`、`settings.gradle`），等 Gradle Sync 跑完
2. `Device Manager` 開模擬器，或 USB 接實機（先開手機開發者選項的 USB 偵錯）
3. 按 `Run`（綠色三角形 / `Shift+F10`）把 app 跑起來
4. 底部分頁選 `App Inspection`（舊版是 `Profiler > Network`），操作 app 觸發功能，即時看到每筆請求的完整網域、method、header
5. 若看不到內容：Network Inspector 只能攔 OkHttp/HttpURLConnection，且遇到 certificate pinning 一樣會失敗，見下方「例外狀況」

## 方法二：已打包 APK —— 通用 Proxy 攔截法

不需要原始碼或 Android Studio 專案，任何 Proxy 攔截工具（mitmproxy / Charles / HTTP Toolkit）通用流程：

1. 用 `adb install app.apk`（或直接拖進模擬器）把 APK 裝到模擬器/實機
2. 電腦上啟動代理工具，記下監聽的 port（例如 `8080`）
3. 裝置 Wi-Fi 設定把 HTTP Proxy 改成手動，填電腦 IP + 該 port
4. 用裝置瀏覽器連到代理工具的憑證下載頁（mitmproxy 是 `mitm.it`）安裝 CA 憑證，不然 HTTPS 會直接連線失敗
5. 打開 app 操作，代理工具畫面即時列出每筆請求的網域、header、body

## HTTP Toolkit 操作流程

比上面手動設代理／裝憑證更省事，靠 ADB 自動處理：

1. 開啟 HTTP Toolkit，首頁選 `Android Device via ADB`（電腦要先裝好 adb，裝置要能被 `adb devices` 抓到）
2. 選定後它會自動用 adb 幫裝置裝代理設定跟 CA 憑證（模擬器/root 過的裝置裝系統層級信任，一般實機裝使用者憑證），連線狀態變綠色即完成
3. 操作 app 觸發功能，主畫面即時列出攔截到的請求
4. 點開單筆請求，右側分頁看 Request/Response 的完整網域、header、body、timing
5. 需要存證據時，右上角可把整個 session 匯出成 HAR 檔

## BlueStacks 搭配 HTTP Toolkit 連線

1. 先依上方「配置文檔」打開 BlueStacks 的 Android Debug Bridge（及 Root access，若有）
2. 電腦終端機執行：

```bash
adb connect 127.0.0.1:5555   # 換成 BlueStacks 畫面顯示的 port
adb devices                   # 確認狀態是 device，不是 offline / unauthorized
```

3. 回 HTTP Toolkit 選 `Android Device via ADB`，這時應該就能抓到這台 BlueStacks 實例
4. 抓不到就先 `adb kill-server` 再 `adb start-server`，重新 `adb connect` 一次（常見是電腦上另一套 adb 佔用了 server）

## 只想確認打到哪個網域（不用解密）

不用裝代理、裝憑證，app 有沒有做 certificate pinning 都不影響：

```bash
# 在模擬器/實機所在網路介面上抓包，看 TLS ClientHello 的 SNI 欄位（明文）
tcpdump -i <介面> -w capture.pcap
# 或直接用 Wireshark 開介面即時看，過濾 tls.handshake.type == 1
```

# 例外狀況

## app 做 certificate pinning，代理抓不到明文內容

- 原因：app 內建只信任特定憑證，不信任你裝的 CA，即使裝了系統層級信任也一樣
- 解法：
  - HTTP Toolkit：裝置卡片開啟 `Android: Override certificate pinning` 攔截器，內建用 Frida 自動繞過多數常見實作
  - 手動：已 root 裝置搭配 Frida + `objection`，或 Magisk 的 SSL 反 pinning 模組
  - 退而求其次：只看 TLS SNI 確認打到哪個網域即可，不需要解密內容（見上方「只想確認打到哪個網域」）

## adb: command not found（macOS）

- 原因：沒安裝 Android platform-tools，或有裝但不在 `PATH`（例如只裝了 BlueStacks，沒裝過 Android Studio / adb）
- 解法：

```bash
# 方法一：只裝 adb / fastboot，不用裝整個 Android Studio
brew install android-platform-tools

# 方法二：已裝過 Android Studio，adb 在內附的 SDK 資料夾裡，補進 PATH
echo 'export PATH="$HOME/Library/Android/sdk/platform-tools:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

- 兩種方法裝完後都要開一個新的終端機視窗（或執行 `source ~/.zshrc`）讓 PATH 生效，跟目前有沒有啟用 conda/venv（例如 `py311`）無關
