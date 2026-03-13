# reCAPTCHA(人機驗證)

```
reCAPTCHA 是由 Google 開發的一種免費防止自動化程式濫用網站的技術。
它通過要求用戶完成一些小任務來區分人類和自動化程式（如機器人）。

reCAPTCHA v1:

    這是最早的版本，通常要求用戶閱讀扭曲的文本並輸入正確的字符。
    這種方式對於大部分機器人來說很難破解，但對於用戶來說可能有點麻煩。

reCAPTCHA v2:

    這個版本更友好，用戶只需點擊「我不是機器人」的勾選框（checkbox）。
    如果系統認為這個動作可疑，會出現一系列圖片拼圖（如選擇包含交通燈的所有圖片）來進一步驗證用戶身份。

reCAPTCHA v3:

    這是一個更加無縫的版本，不需要用戶進行任何操作。
    它在背景中運行，根據用戶的行為分析風險分數來判斷是否為機器人。
    網站開發者可以根據這個風險分數來決定是否進一步驗證用戶（如發送郵件驗證）。

reCAPTCHA Enterprise:

    針對企業級用戶，提供更高級的風險分析和自定義選項。
    適用於需要高度安全性的應用和網站。

reCAPTCHA 的工作原理
reCAPTCHA 的基本工作原理是通過分析用戶的行為模式來區分人類和機器人。
這些行為模式包括但不限於：
滑鼠移動和點擊的方式
鍵盤輸入的速度和模式
瀏覽器和設備信息
過去的行為歷史和互動記錄

使用 reCAPTCHA 的好處
提高安全性：有效防止垃圾留言、賬戶盜取和其他形式的自動化攻擊。
提高用戶體驗：reCAPTCHA v3 幾乎不打擾用戶，v2 也只需要簡單的操作。
免費使用：對於大多數網站來說，reCAPTCHA 是免費的。
```

## 目錄

- [reCAPTCHA(人機驗證)](#recaptcha人機驗證)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [使用相關](#使用相關)
    - [破解相關](#破解相關)
- [範例](#範例)
  - [reCAPTCHA v2 的簡單整合示例](#recaptcha-v2-的簡單整合示例)

## 參考資料

[reCAPTCHA 文檔](https://developers.google.com/recaptcha/docs/v3?hl=zh-tw)

[reCAPTCHA wiki](https://zh.wikipedia.org/zh-tw/ReCAPTCHA)

### 使用相關

[Google reCAPTCHA(v3) 我不是機器人再進化，完整簡易教學版](https://www.e-show.tw/module/pageinfo/106.html)

### 破解相關

[使用python selenium解决谷歌验证码(reCAPTCHA)](https://www.cnblogs.com/mswei/p/15568461.html)

[如何有效地繞過驗證碼](https://ithelp.ithome.com.tw/articles/10256692)

[PCHome 登入爬蟲時recaptcha V3 的解決方式](https://ithelp.ithome.com.tw/questions/10212643)

[最佳的驗證碼破解服務](https://yanwei-liu.medium.com/best-captcha-service-10b76cf2ca5f)

[2captcha 收費 api](https://2captcha.com/zh/)

[Image Typerz](https://www.imagetyperz.com/)

[Deathbycaptcha](https://deathbycaptcha.com/)

[EndCaptcha](https://endcaptcha.com/)

[Bypass Captcha](https://book.hacktricks.xyz/pentesting-web/captcha-bypass)

# 範例

## reCAPTCHA v2 的簡單整合示例

獲取 API 金鑰：
    註冊 Google reCAPTCHA，並獲取網站密鑰（site key）和秘密密鑰（secret key）。

在網站中添加 reCAPTCHA：
    在需要的頁面中嵌入 reCAPTCHA 的 JavaScript 代碼。
    在表單提交時驗證用戶的響應，將其發送到後端進行驗證。

HTML 部分：

```html
<form action="submit.php" method="post">
  <div class="g-recaptcha" data-sitekey="你的網站密鑰"></div>
  <input type="submit" value="提交">
</form>
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
```

後端驗證部分（以 PHP 為例）：

```php
<?php
$secret = '你的秘密密鑰';
$response = $_POST['g-recaptcha-response'];
$remoteip = $_SERVER['REMOTE_ADDR'];

$recaptcha_url = 'https://www.google.com/recaptcha/api/siteverify';
$recaptcha_response = file_get_contents($recaptcha_url . '?secret=' . $secret . '&response=' . $response . '&remoteip=' . $remoteip);
$recaptcha_data = json_decode($recaptcha_response);

if ($recaptcha_data->success) {
    echo '驗證成功';
} else {
    echo '驗證失敗';
}
?>
```
