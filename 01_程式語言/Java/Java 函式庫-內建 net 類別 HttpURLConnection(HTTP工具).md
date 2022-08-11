# Java 函式庫-內建 net 類別 HttpURLConnection(HTTP工具)

```
```

## 目錄

- [Java 函式庫-內建 net 類別 HttpURLConnection(HTTP工具)](#java-函式庫-內建-net-類別-httpurlconnectionhttp工具)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[net package summary SE7](https://docs.oracle.com/javase/7/docs/api/java/net/package-summary.html)

[net package summary SE8](https://docs.oracle.com/javase/8/docs/api/java/net/package-summary.html)

[HttpURLConnection SE7](https://docs.oracle.com/javase/7/docs/api/java/net/HttpURLConnection.html)

[HttpURLConnection SE8](https://docs.oracle.com/javase/8/docs/api/java/net/HttpURLConnection.html)

[Online REST & SOAP API Testing Tool - cURL轉換成程式碼](https://reqbin.com/)

# 指令

# 用法

```Java
URL url = new URL("https://reqbin.com/echo/post/form");
HttpURLConnection http = (HttpURLConnection)url.openConnection();
http.setRequestMethod("POST");
http.setDoOutput(true);
http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

String data = "key1=value1&key2=value2";

byte[] out = data.getBytes(StandardCharsets.UTF_8);

OutputStream stream = http.getOutputStream();
stream.write(out);

System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
http.disconnect();
```
