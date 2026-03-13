# Tencent(騰訊) 筆記

```
```

## 目錄

- [Tencent(騰訊) 筆記](#tencent騰訊-筆記)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [API 相關](#api-相關)
- [API](#api)
  - [騰訊雲官方的簽名流程](#騰訊雲官方的簽名流程)
  - [取得剩餘金額](#取得剩餘金額)
    - [使用 GuzzleHttp\\Client，這樣不用依賴 Laravel 版本](#使用-guzzlehttpclient這樣不用依賴-laravel-版本)

## 參考資料

### API 相關

[查询已审核客户列表](https://cloud.tencent.com/document/product/563/19184)

# API

## 騰訊雲官方的簽名流程

1. Canonical Request

把請求的 HTTP 方法、路徑、Header、Body 等，組合成一個標準格式字串。

2. String To Sign 待簽名字串 (StringToSign)

把 Canonical Request 的雜湊值，加上演算法、時間戳、認證範圍等，再組合成「待簽名字串」。

3. 計算簽名

用 SecretKey 派生出臨時金鑰，對 String To Sign 做 HMAC-SHA256，得到簽名。

4. Authorization

把簽名和其他資訊放進 HTTP Header。

## 取得剩餘金額

騰訊雲官方有提供 帳單 API：

接口是 DescribeAccountBalance

文檔：https://cloud.tencent.com/document/api/555/19184

回傳範例：

```json
{
    "Balance": 12345,       // 餘額，分為單位
    "Cash": 12345,
    "RequestId": "xxxx"
}
```

```php
use Illuminate\Support\Facades\Http;
use App\Models\QcloudAccounts;

class QcloudAccountsController extends Controller
{
    public function get_balance($bucket)
    {
        $account = QcloudAccounts::find($bucket);

        if (!$account) {
            return response()->json([
                'success' => false,
                'message' => '找不到帳號',
            ], 404);
        }

        if ($account->account_type != 0) {
            return response()->json([
                'success' => true,
                'balance' => '-',
            ]);
        }

        $secretId  = $account->secret_id;
        $secretKey = $account->secret_key;

        $host    = "billing.tencentcloudapi.com";
        $service = "billing";
        $region  = "ap-guangzhou";  // Billing API 可隨便填
        $action  = "DescribeAccountBalance";
        $version = "2018-07-09";

        $timestamp = time();
        $date = gmdate("Y-m-d", $timestamp);

        // Step 1: Canonical Request
        $httpRequestMethod = "POST";
        $canonicalUri = "/";
        $canonicalQueryString = "";
        $canonicalHeaders = "content-type:application/json\nhost:{$host}\n";
        $signedHeaders = "content-type;host";
        $payload = "{}"; // DescribeAccountBalance 不需要參數
        $hashedRequestPayload = hash("SHA256", $payload);
        $canonicalRequest = "$httpRequestMethod\n$canonicalUri\n$canonicalQueryString\n$canonicalHeaders\n$signedHeaders\n$hashedRequestPayload";

        // Step 2: String To Sign
        $algorithm = "TC3-HMAC-SHA256";
        $credentialScope = "$date/$service/tc3_request";
        $hashedCanonicalRequest = hash("SHA256", $canonicalRequest);
        $stringToSign = "$algorithm\n$timestamp\n$credentialScope\n$hashedCanonicalRequest";

        // Step 3: 計算簽名
        $secretDate = hash_hmac("SHA256", $date, "TC3".$secretKey, true);
        $secretService = hash_hmac("SHA256", $service, $secretDate, true);
        $secretSigning = hash_hmac("SHA256", "tc3_request", $secretService, true);
        $signature = hash_hmac("SHA256", $stringToSign, $secretSigning);

        // Step 4: Authorization
        $authorization = "$algorithm Credential=$secretId/$credentialScope, SignedHeaders=$signedHeaders, Signature=$signature";

        try {
            $response = Http::withHeaders([
                "Authorization"   => $authorization,
                "Content-Type"    => "application/json",
                "Host"            => $host,
                "X-TC-Action"     => $action,
                "X-TC-Version"    => $version,
                "X-TC-Timestamp"  => $timestamp,
                "X-TC-Region"     => $region,
            ])->post("https://{$host}", json_decode($payload, true));

            $data = $response->json();

            if (isset($data['Response']['Balance'])) {
                $balance = $data['Response']['Balance'] / 100; // 轉換成元
                return response()->json([
                    'success' => true,
                    'balance' => $balance,
                ]);
            }

            return response()->json([
                'success' => false,
                'message' => $data['Response']['Error']['Message'] ?? '查詢失敗',
            ], 500);

        } catch (\Exception $e) {
            return response()->json([
                'success' => false,
                'message' => 'API 呼叫錯誤: ' . $e->getMessage(),
            ], 500);
        }
    }
}
```

### 使用 GuzzleHttp\Client，這樣不用依賴 Laravel 版本

```php
use GuzzleHttp\Client;

public function get_balance($bucket)
{
    $account = QcloudAccounts::find($bucket);

    if (!$account) {
        return response()->json([
            'success' => false,
            'message' => '找不到帳號',
        ], 404);
    }

    if ($account->account_type != 0) {
        return response()->json([
            'success' => true,
            'balance' => '-',
        ]);
    }

    $secretId  = $account->secret_id;
    $secretKey = $account->secret_key;

    $host    = "billing.tencentcloudapi.com";
    $service = "billing";
    $region  = "ap-guangzhou";  // Billing API 不一定需要
    $action  = "DescribeAccountBalance";
    $version = "2018-07-09";

    $timestamp = time();
    $date = gmdate("Y-m-d", $timestamp);

    // Step 1: Canonical Request
    $httpRequestMethod = "POST";
    $canonicalUri = "/";
    $canonicalQueryString = "";
    $canonicalHeaders = "content-type:application/json\nhost:{$host}\n";
    $signedHeaders = "content-type;host";
    $payload = "{}"; // DescribeAccountBalance 沒有參數
    $hashedRequestPayload = hash("SHA256", $payload);
    $canonicalRequest = "$httpRequestMethod\n$canonicalUri\n$canonicalQueryString\n$canonicalHeaders\n$signedHeaders\n$hashedRequestPayload";

    // Step 2: String To Sign
    $algorithm = "TC3-HMAC-SHA256";
    $credentialScope = "$date/$service/tc3_request";
    $hashedCanonicalRequest = hash("SHA256", $canonicalRequest);
    $stringToSign = "$algorithm\n$timestamp\n$credentialScope\n$hashedCanonicalRequest";

    // Step 3: 簽名
    $secretDate = hash_hmac("SHA256", $date, "TC3".$secretKey, true);
    $secretService = hash_hmac("SHA256", $service, $secretDate, true);
    $secretSigning = hash_hmac("SHA256", "tc3_request", $secretService, true);
    $signature = hash_hmac("SHA256", $stringToSign, $secretSigning);

    // Step 4: Authorization
    $authorization = "$algorithm Credential=$secretId/$credentialScope, SignedHeaders=$signedHeaders, Signature=$signature";

    try {
        $client = new Client();
        $response = $client->post("https://{$host}", [
            'headers' => [
                "Authorization"   => $authorization,
                "Content-Type"    => "application/json",
                "Host"            => $host,
                "X-TC-Action"     => $action,
                "X-TC-Version"    => $version,
                "X-TC-Timestamp"  => $timestamp,
                "X-TC-Region"     => $region,
            ],
            'body' => $payload,
        ]);

        $data = json_decode($response->getBody()->getContents(), true);

        if (isset($data['Response']['Balance'])) {
            $balance = $data['Response']['Balance'] / 100; // 單位轉換
            return response()->json([
                'success' => true,
                'balance' => $balance,
            ]);
        }

        return response()->json([
            'success' => false,
            'message' => $data['Response']['Error']['Message'] ?? '查詢失敗',
        ], 500);

    } catch (\Exception $e) {
        return response()->json([
            'success' => false,
            'message' => 'API 呼叫錯誤: ' . $e->getMessage(),
        ], 500);
    }
}
```
