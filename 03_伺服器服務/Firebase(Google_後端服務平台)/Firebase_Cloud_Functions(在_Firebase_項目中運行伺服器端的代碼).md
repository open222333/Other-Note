# Firebase Cloud Functions(在 Firebase 項目中運行伺服器端的代碼)

```
Firebase Cloud Functions 是 Firebase 提供的一項服務，它允許您在 Firebase 項目中運行伺服器端的代碼。Firebase Cloud Functions 的主要目的是讓您能夠執行特定事件發生時觸發的代碼，這些事件可以包括 Firebase Realtime Database 上的數據更改、新的文件上傳到 Cloud Storage、新用戶註冊等。

以下是 Firebase Cloud Functions 的一些主要特點和使用場景：

事件驅動的代碼執行： 您可以定義 Cloud Functions，使其在某些特定的事件發生時自動執行。這樣的事件可能包括數據庫更改、文件上傳、新用戶註冊等。

無伺服器運行： Firebase Cloud Functions 是無伺服器（serverless）的，這意味著您無需操心伺服器的管理和維護，Firebase 會自動處理伺服器的配置和擴展。

輕量級和靈活： Cloud Functions 是小型、獨立的代碼塊，您可以根據需要編寫和部署它們。這讓您能夠將代碼針對特定事件進行分割，並維護它們。

支援多種語言： Firebase Cloud Functions 支援多種語言，包括 Node.js、Python、Go、Java 等。您可以使用您最熟悉的語言來編寫 Cloud Functions。

整合 Firebase 產品： Cloud Functions 可以輕鬆與其他 Firebase 產品進行整合，例如 Realtime Database、Firestore、Cloud Storage、Authentication 等。

使用 Firebase Cloud Functions 可以實現一系列自動化的後端邏輯，使應用程序更強大且具有更高的靈活性。您可以在 Firebase 控制台上設置和部署 Cloud Functions，也可以使用命令行工具進行操作。
```

```
Firebase Cloud Functions 的收費是基於使用的資源和執行的次數。
Firebase 提供有限的免費額度，超出免費額度的使用可能會產生費用。

以下是 Firebase Cloud Functions 的主要計費因素：

運行時間： 您將根據 Cloud Functions 的運行時間進行計費，以毫秒為單位。
計算從函數啟動到結束所花費的時間。

資源使用： 這包括函數使用的記憶體和網絡資源。
Firebase Cloud Functions 會基於這些資源的使用情況向您收費。

觸發事件的次數： Firebase Cloud Functions 是事件驅動的，當特定事件發生時（例如數據庫更改、文件上傳等），相應的 Cloud Function 將被觸發。這也可能影響收費。

網絡傳輸： 如果 Cloud Functions 與其他 Firebase 服務通信，可能會產生額外的網絡傳輸費用。

Firebase Cloud Functions 提供了一些免費額度，這些額度可以滿足一般應用的需求。具體的計費詳情和定價策略可能會隨時間而有所變化，因此建議查看 Firebase 官方網站上的最新計費信息，以確保您擁有最準確的資訊。
```

## 目錄

- [Firebase Cloud Functions(在 Firebase 項目中運行伺服器端的代碼)](#firebase-cloud-functions在-firebase-項目中運行伺服器端的代碼)
	- [目錄](#目錄)
	- [參考資料](#參考資料)

## 參考資料

[使用 Cloud Functions for Firebase 建立 LINE Chatbot x Gemini（Google 最好的 AI Gen）](https://techblog.lycorp.co.jp/zh-hant/linebot-x-gemini-firebase?fbclid=IwAR2urERfCH0e3j3RISufjz4qhf3Em9vm8NinFNON06TzBlMYQ9YEOv5Qd-4)
