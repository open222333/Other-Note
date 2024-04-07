# Java 筆記

## 目錄

- [Java 筆記](#java-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
  - [cURL 範本](#curl-範本)
  - [GET](#get)
  - [POST](#post)
  - [Basic Auth](#basic-auth)

## 參考資料

[官網](https://www.java.com/zh-TW/)

[JAVA 基本文法](https://www.runoob.com/java/java-basic-syntax.html)

# 指令

```bash
java -version
```

編譯 Java 程式

```bash
javac YourProgram.java
```

執行已經編譯好的 Java 程式

```bash
java -cp path/to/libs YourProgram
```

-D：設置系統屬性，例如 -Dproperty=value。
-X：設置非標準的選項，例如 -Xms512m 設置初始堆大小為 512 MB。
-jar：執行包含主程序入口的 JAR 文件，例如 java -jar YourApp.jar。

## cURL 範本

[Convert curl commands to JavaScript](https://curlconverter.com/#java)

## GET

```java
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class Main {

	public static void main(String[] args) throws IOException {
		URL url = new URL("http://en.wikipedia.org/");
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod("GET");

		httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
		httpConn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36");
		httpConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpConn.setRequestProperty("Referer", "http://www.wikipedia.org/");
		httpConn.setRequestProperty("Connection", "keep-alive");

		InputStream responseStream = httpConn.getResponseCode() / 100 == 2
				? httpConn.getInputStream()
				: httpConn.getErrorStream();
		if ("gzip".equals(httpConn.getContentEncoding())) {
			responseStream = new GZIPInputStream(responseStream);
		}
		Scanner s = new Scanner(responseStream).useDelimiter("\\A");
		String response = s.hasNext() ? s.next() : "";
		System.out.println(response);
	}
}
```

## POST

```java
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class Main {

	public static void main(String[] args) throws IOException {
		URL url = new URL("http://fiddle.jshell.net/echo/html/");
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod("POST");

		httpConn.setRequestProperty("Origin", "http://fiddle.jshell.net");
		httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		httpConn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36");
		httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		httpConn.setRequestProperty("Accept", "*/*");
		httpConn.setRequestProperty("Referer", "http://fiddle.jshell.net/_display/");
		httpConn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
		httpConn.setRequestProperty("Connection", "keep-alive");

		httpConn.setDoOutput(true);
		OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
		writer.write("msg1=wow&msg2=such&msg3=data");
		writer.flush();
		writer.close();
		httpConn.getOutputStream().close();

		InputStream responseStream = httpConn.getResponseCode() / 100 == 2
				? httpConn.getInputStream()
				: httpConn.getErrorStream();
		if ("gzip".equals(httpConn.getContentEncoding())) {
			responseStream = new GZIPInputStream(responseStream);
		}
		Scanner s = new Scanner(responseStream).useDelimiter("\\A");
		String response = s.hasNext() ? s.next() : "";
		System.out.println(response);
	}
}
```

## Basic Auth

```java
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class Main {

	public static void main(String[] args) throws IOException {
		URL url = new URL("https://api.test.com/");
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod("GET");

		byte[] message = ("some_username:some_password").getBytes("UTF-8");
		String basicAuth = DatatypeConverter.printBase64Binary(message);
		httpConn.setRequestProperty("Authorization", "Basic " + basicAuth);

		InputStream responseStream = httpConn.getResponseCode() / 100 == 2
				? httpConn.getInputStream()
				: httpConn.getErrorStream();
		Scanner s = new Scanner(responseStream).useDelimiter("\\A");
		String response = s.hasNext() ? s.next() : "";
		System.out.println(response);
	}
}
```