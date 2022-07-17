# MacOS 工具 launchd(工作排程)

```
macOS1 自 10.4 開始使用 launchd 這一個啟動守護行程 (Launch Daemon) 來管理系統中的程序 (Processes)、應用程式 (Applications) 及腳本 (Scripts)，以及進行工作排程。


屬性列表 (Property List) 檔案簡介

MacOS的服務可以分為系統層級 (System-Wide) 以及使用者層級 (Per-User) 兩種。
系統層級的服務被稱為是守護行程 (Daemon)，會在開機時載入；使用者層級的服務則被稱為任務項 (Agent)，會在使用者登入時才載入。

負責儲存服務工作定義 (Job Definition) 的檔案被稱為屬性列表 (Property List) 檔案 (簡稱為 plist 檔)，分別存放在下列的檔案夾中：
/System/Library/LaunchDaemons
Apple 提供的系統服務程式

/System/Library/LaunchAgents
Apple 提供的代理程式，適用於以使用者為基礎的所有使用者

/Library/LaunchDaemons
第三方系統服務程式

/Library/LaunchAgents
第三方代理程式，適用於以使用者為基礎的所有使用者

~/Library/LaunchAgents
第三方代理程式，僅適用於已登入的使用者
```

## 參考資料

[在 Mac 上登入時自動打開項目 - 使用介面做設定](https://support.apple.com/zh-tw/guide/mac-help/mh15189/mac)

[man launchd.plist(5)](https://www.manpagez.com/man/5/launchd.plist/)

[macOS 的啟動守護行程 (Launch Daemon)：launchd](http://mt116.blogspot.com/2017/11/launch-daemonslaunchd.html)

[在 Mac 上的「終端機」中使用 launchd 進行工序指令管理](https://support.apple.com/zh-tw/guide/terminal/apdc6c1077b-5d5d-4d35-9c19-60f2397b2369/mac)

# 指令

```bash
```

# 配置文檔

```xml
<!-- 存放在 /System/Library/LaunchDaemons 檔案夾中的  tftp.plist 檔的設定內容 -->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple Computer//DTD PLIST 1.0//EN" 
    "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
 <key>Disabled</key>
 <true/>
 <key>Label</key>
 <string>com.apple.tftpd</string>
 <key>ProgramArguments</key>
 <array>
  <string>/usr/libexec/tftpd</string>
  <string>-i</string>
  <string>/private/tftpboot</string>
 </array>
 <key>inetdCompatibility</key>
 <dict>
  <key>Wait</key>
  <true/>
 </dict>
 <key>InitGroups</key>
 <true/>
 <key>Sockets</key>
 <dict>
  <key>Listeners</key>
  <dict>
   <key>SockServiceName</key>
   <string>tftp</string>
   <key>SockType</key>
   <string>dgram</string>
  </dict>
 </dict>
</dict>
</plist>
```

```
<key>Disabled</key> 設定項目用來決定工作定義內容是否會被載入 (此項設定的 PCDATA 為 Disabled)；
其後緊跟著屬於數值原姶的 <true/> 設定用標籤，代表設定內容將不會被載入。

<key>Label</key> 設定項目用來識別工作，對於 launchd 而言，它必需是唯一的；
其後緊跟著 <string> 原始型別類型標籤來設定守護行程或是任務項的名稱 。

<key>ProgramArguments</key> 設定項目指定要執行的程式的路徑以及參數，因為需要設定較多的內容，因此使用 <array> 集合類型標籤來設定多個參數，而每個參數則會使用 <string> 標籤。
```
