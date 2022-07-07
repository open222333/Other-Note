# Linux 工具 rpcbind(Remote Procedure Call，RPC, 遠端程序呼叫)

```
分散式計算中，遠端程序呼叫（英語：Remote Procedure Call，RPC）是一個電腦通信協定。
該協定允許執行於一台電腦的程式呼叫另一個位址空間（通常為一個開放網路的一台電腦）的子程式，而程式設計師就像呼叫本地程式一樣，無需額外地為這個互動作用編程（無需關注細節）。
RPC是一種伺服器-客戶端（Client/Server）模式，經典實現是一個通過傳送請求-接受回應進行資訊互動的系統。
```

```
rpcbind工具可以將RPC程序號碼和通用地址互相轉換。要讓某主機能向遠程主機的服務發起RPC調用， 則該主機上的rpcbind必須處於已運行狀態。
當RPC服務啟動後，它會告訴rpcbind它監聽在哪個地址上，還會告訴它為服務準備好提供的PRC程序號碼。當客戶端要向某個給定的程序號碼發起RPC調用時，它首先會聯繫服務端的rpcbind以確定RPC 請求應該發送到哪個地址上。
rpcbind工具應該在所有RPC管理的服務(rpc service)啟動之前啟動。一般來說，標準的rpc服務由端口監視器來啟動，因此rpcbind必須在端口監視器被調用之前已經啟動完成。
當rpcbind工具已經啟動後，它會檢查特定的name-to-address的轉換調用功能是否正確執行。如果失敗，則網絡配置數據庫會被認為過期，由於RPC管理的服務在這種情況下無法正確運行，rpcbind會輸出這些信息並終止。
另外，rpcbind工具只能由super-user啟動
```

```
portmap
當一個RPC服務器啟動時，會選擇一個空閒的端口號並在上面監聽（每次啟動後的端口號各不相同），同時它作為一個可用的服務會在portmap進程註冊。一個RPC服務器對應惟一一個RPC程序號，RPC服務器告訴portmap進程它在哪個端口號上監聽連接請求和為哪個RPC程序號提供服務。經過這個過程，portmap進程就知道了每一個已註冊的RPC服務器所用的Internet端口號，而且還知道哪個程序號在這個端口上是可用的。portmap進程維護著一張RPC程序號到Internet端口號之間的映射表，它的字段包括程序號、版本號、所用協議、端口號和服務名，portmap進程通過這張映射表來提供程序號-端口號之間的轉化功能
如果portmap進程停止了運行或異常終止，那麼該系統上的所有RPC服務器必須重新啟動。首先停止NFS服務器上的所有NFS服務進程，然後啟動portmap進程，再啟動服務器上的NFS進程。
portmap進程一般使用TCP/UDP的111端口。
```

## 參考資料

[遠端程序呼叫](https://zh.wikipedia.org/zh-tw/%E9%81%A0%E7%A8%8B%E9%81%8E%E7%A8%8B%E8%AA%BF%E7%94%A8)

[rpcbind(8) — Linux manual page](https://man7.org/linux/man-pages/man8/rpcbind.8.html)

[Linux rpcinfo command](https://www.computerhope.com/unix/urpcinfo.htm)

# 指令

```bash

# 可以查看目前所有服務
rpcinfo

# 查看 localhost
rpcinfo -s localhost

# klaxon 是機器名稱
rpcinfo klaxon

# 詳細說明
man rpcinfo
```
