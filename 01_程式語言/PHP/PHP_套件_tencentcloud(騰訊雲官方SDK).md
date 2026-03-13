# PHP 套件 tencentcloud(騰訊雲官方SDK)

```
tencentcloud/tencentcloud-sdk-php

必須先申請 SecretId / SecretKey
PHP 版本需 ≥ 5.6.33
建議使用 Composer 安裝 SDK
可單獨安裝某個產品 SDK（例如 CVM）或全產品 SDK
在程式碼中透過 vendor/autoload.php 引入 SDK
```

## 目錄

- [PHP 套件 tencentcloud(騰訊雲官方SDK)](#php-套件-tencentcloud騰訊雲官方sdk)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[PHP SDK 3.0 使用说明（官方中文版）](https://www.tencentcloud.com/zh/document/product/583/19695)

# 安裝

```bash
composer require tencentcloud/tencentcloud-sdk-php
```

在程式碼引入

```PHP
require '/path/to/vendor/autoload.php';
```

# 指令

```bash
```

# 用法

```PHP
<?php

require_once 'vendor/autoload.php';

use TencentCloud\Common\Credential;
use TencentCloud\Cvm\V20170312\CvmClient;
use TencentCloud\Cvm\V20170312\Models\DescribeZonesRequest;
use TencentCloud\Common\Exception\TencentCloudSDKException;

try {
    $cred = new Credential("YourSecretId", "YourSecretKey");
    $client = new CvmClient($cred, "ap-guangzhou");

    $req = new DescribeZonesRequest();
    $resp = $client->DescribeZones($req);

    print_r($resp->toJsonString());
} catch (TencentCloudSDKException $e) {
    echo $e;
}
```

```
使用 SecretId / SecretKey 建立憑證
建立 CVM 客戶端
呼叫 DescribeZones API 查詢可用區域
印出結果 JSON
```
