# Windows 遠端連線

## 參考資料

[Windows 內建的遠端桌面連線工具設定與使用教學](https://www.pcsetting.com/windows/31)

# Windows10 設定 遠端連線

1. windows鍵+R -> 輸入`systempropertiesremote`進行遠端設定
2. 遠端設定視窗 -> 選擇`允許遠端連線到此電腦`
3. 取消勾選`僅允許來自執行含有網路層級驗證之遠端桌面的電腦進行連線`，完成之後點選`確定`
4. windows鍵+R -> 輸入`firewall.cpl`進行防火牆設定
5. 點選`允許應用程式或功能通過Windows防火牆`->`變更設定`
6. 在應用程式與功能列表中，找到`遠端桌面`功能

# 例外狀況

## 身份驗證錯誤，要求的函數不受支持

[Win10遠程桌面 出現 身份驗證錯誤，要求的函數不受支持，這可能是由於CredSSP加密Oracle修正](https://www.cnblogs.com/raswin/p/9018388.html)

解決方法

```
計算機配置>管理模板>系統>憑據分配>加密Oracle修正
```
