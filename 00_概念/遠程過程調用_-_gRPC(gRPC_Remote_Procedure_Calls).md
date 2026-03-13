# 遠程過程調用 - gRPC(gRPC Remote Procedure Calls)

```
gRPC (gRPC Remote Procedure Calls)
介面描述語言（Interface description language，縮寫IDL）

gRPC（gRPC 遠程過程調用）是一種高性能、開源的遠程過程調用（RPC）框架，由 Google 開發並開源。
它允許不同的應用程序在不同的計算機之間通過定義和發佈服務來進行通信，就像調用本地函數一樣。

以下是有關 gRPC 的一些重要概念和特點：

IDL（介面定義語言）: gRPC 使用 Protocol Buffers（ProtoBuf）作為其 IDL。ProtoBuf 是一種輕量級且高效的序列化格式，用於定義服務介面和消息結構。IDL 用於定義 gRPC 服務的方法、輸入參數和返回類型。

多語言支持: gRPC 支持多種編程語言，包括但不限於 Python、Java、C++、Go、C#、Node.js、Ruby 等。這意味著您可以使用 gRPC 來構建多語言的分佈式應用程序。

HTTP/2 協議: gRPC 使用 HTTP/2 作為其傳輸協議，這帶來了性能上的改進。HTTP/2 支持雙向流、頭部壓縮、多路復用等功能，使得 gRPC 在處理大量請求時更加高效。

雙向通信: gRPC 支持雙向流式通信，這意味著客戶端和伺服器可以同時發送和接收數據流。這對於實時應用程序和流式數據處理非常有用。

自動代碼生成: 基於服務定義和消息結構，gRPC 提供了自動代碼生成工具，使開發人員能夠輕鬆生成客戶端和伺服器端的代碼。

支持安全性: gRPC 支持多種安全性選項，包括基於 TLS/SSL 的加密通信以及認證和授權機制，以確保通信的安全性。

高性能: gRPC 被設計為高性能的遠程過程調用框架，它使用二進制協議和連接復用來減少開銷，並提供了異步支持，以實現更高的並發性能。

攔截器和中間件: gRPC 允許您使用攔截器和中間件來添加功能，例如身份驗證、日誌記錄、錯誤處理等。

生態系統: gRPC 有一個豐富的生態系統，包括各種插件、工具和擴展，用於幫助開發人員更輕鬆地構建和部署 gRPC 服務。

總之，gRPC 是一個強大的遠程過程調用框架，適用於構建分佈式系統中的各種服務。它具有高性能、多語言支持、雙向通信、安全性等許多優點，適用於各種應用場景，特別是需要高性能和實時通信的應用程序。
```

## 目錄

- [遠程過程調用 - gRPC(gRPC Remote Procedure Calls)](#遠程過程調用---grpcgrpc-remote-procedure-calls)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [教學相關](#教學相關)

## 參考資料

[官方網站](https://grpc.io/)

[grpc - 原始碼庫 github](https://github.com/grpc/grpc)

[gRPC - wiki](https://zh.wikipedia.org/zh-tw/GRPC)

### 教學相關

[三種好用的 gRPC 測試工具](https://blog.wu-boy.com/2022/08/three-grpc-testing-tool/)

[什麼是 RPC ？](https://www.readfog.com/a/1676771469356535808)

[比較 gRPC 服務與 HTTP API](https://learn.microsoft.com/zh-tw/aspnet/core/grpc/comparison?view=aspnetcore-7.0)