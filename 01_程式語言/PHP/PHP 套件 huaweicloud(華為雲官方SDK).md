# PHP 套件 huaweicloud(華為雲官方SDK)

```
huaweicloud/huaweicloud-sdk-php
```

## 目錄

- [PHP 套件 huaweicloud(華為雲官方SDK)](#php-套件-huaweicloud華為雲官方sdk)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [指令](#指令)
- [用法](#用法)
  - [Huawei class 自製類別](#huawei-class-自製類別)
  - [建立 → 再更新](#建立--再更新)
  - [建立新的 CDN 加速域名](#建立新的-cdn-加速域名)

## 參考資料

[Huawei Cloud SDK 開發者指南（包含 PHP 章節）](https://support.huaweicloud.com/intl/en-us/devg-sdk/en-us_topic_0070637169.html)

[Huawei Cloud SDK V3 開發指南（中文）](https://support.huaweicloud.com/devg-sdk/zh-cn_topic_0070637169.html)

[PHP SDK 文字識別 (OCR) 示例](https://support.huaweicloud.com/intl/zh-cn/sdkreference-ocr/ocr_04_0009.html)

[PHP SDK 用於 IoT 服務](https://support.huaweicloud.com/intl/zh-cn/sdkreference-iothub/iot_10_10007.html)

[PHP SDK 用於 Live 直播](https://support.huaweicloud.com/intl/zh-cn/ssdk-live/live_18_0005.html)

[CDN 使用者指南 裡關於回源請求頭（Origin Request Headers）的官方說明](https://support.huaweicloud.com/intl/zh-cn/usermanual-cdn/cdn_01_0126.html)

[修改域名全量配置接口（UpdateDomainFullConfig）官方中文文檔](https://support.huaweicloud.com/api-cdn/UpdateDomainFullConfig.html)

[建立加速網域（CreateDomain） 官方中文文檔](https://support.huaweicloud.com/intl/zh-cn/api-cdn/CreateDomain.html)


# 安裝

```bash
composer require huaweicloud/huaweicloud-sdk-php
```

# 指令

```bash
```

# 用法

```PHP
use HuaweiCloud\SDK\Core\Auth\BasicCredentials;
use HuaweiCloud\SDK\Ecs\V2\EcsClient;
use HuaweiCloud\SDK\Ecs\V2\Model\ListServersDetailsRequest;

$credentials = new BasicCredentials()
    ->withAk('your-ak')
    ->withSk('your-sk')
    ->withProjectId('your-project-id');

$client = EcsClient::newBuilder()
    ->withCredentials($credentials)
    ->withRegion('cn-north-4')
    ->build();

$request = new ListServersDetailsRequest();
$response = $client->listServersDetails($request);
```

## Huawei class 自製類別

```PHP
<?php

namespace App;

// 引入 HuaweiCloud PHP SDK 的各種 class
use HuaweiCloud\SDK\Core\Auth\GlobalCredentials;
use HuaweiCloud\SDK\Core\Auth\BasicCredentials;
use HuaweiCloud\SDK\Core\Http\HttpConfig;
use HuaweiCloud\SDK\Iam\V3\IamClient;
use HuaweiCloud\SDK\Iam\V3\Model\KeystoneListProjectsRequest;
use HuaweiCloud\SDK\Cdn\V2\CdnClient;
use HuaweiCloud\SDK\Cdn\V2\Model\CreateDomainRequest;
use HuaweiCloud\SDK\Cdn\V2\Model\CreateDomainRequestBody;
use HuaweiCloud\SDK\Cdn\V2\Model\DomainsWithPort;
use HuaweiCloud\SDK\Cdn\V2\Model\ListDomainsRequest;
use HuaweiCloud\SDK\Cdn\V2\Model\DomainOriginHost;
use HuaweiCloud\SDK\Cdn\V2\Model\BatchCopyDomainRequest;
use HuaweiCloud\SDK\Cdn\V2\Model\BatchCopyDRequestBody;
use HuaweiCloud\SDK\Cdn\V2\Model\BatchCopyConfigs;
use HuaweiCloud\SDK\Cdn\V2\Model\UpdateDomainMultiCertificatesRequest;
use HuaweiCloud\SDK\Cdn\V2\Model\UpdateDomainMultiCertificatesRequestBody;
use HuaweiCloud\SDK\Cdn\V2\Model\UpdateDomainMultiCertificatesRequestBodyContent;
use HuaweiCloud\SDK\Cdn\V2\Model\ForceRedirectConfig;
use HuaweiCloud\SDK\Core\Exceptions\ConnectionException;
use HuaweiCloud\SDK\Core\Exceptions\RequestTimeoutException;
use HuaweiCloud\SDK\Core\Exceptions\ServiceResponseException;
use HuaweiCloud\SDK\Cdn\V2\Model\ShowVerifyDomainOwnerInfoRequest;


/**
 * Huawei 封裝類
 * 這個類別用來操作 HuaweiCloud 的 IAM 和 CDN 功能
 */
class Huawei
{
    // HuaweiCloud 認證資訊
    protected string $ak;
    protected string $sk;
    protected string $region;

    // 建構子，初始化 SDK client
    public function __construct($HuaweiCloudAccounts)
    {
        // 從傳入的物件取得 AK/SK/Region
        $this->ak = $HuaweiCloudAccounts->access_key;
        $this->sk = $HuaweiCloudAccounts->secret_key;
        $this->region = $HuaweiCloudAccounts->region;

        // 建立 HTTP 配置，忽略 SSL 驗證
        $this->config = HttpConfig::getDefaultConfig();
        $this->config->setIgnoreSslVerification(true);

        // IAM client
        $this->cred = new GlobalCredentials($this->ak, $this->sk);
        $this->iamClient = IamClient::newBuilder()
            ->withHttpConfig($this->config)
            ->withCredentials($this->cred)
            ->withEndpoint("https://iam.myhuaweicloud.com") // IAM 服務入口
            ->build();

        // 取得 Project ID（根據 region）
        $this->projectId = $this->getProjectIdByRegion($this->region);

        // CDN client
        $this->cred = new GlobalCredentials($this->ak, $this->sk, $this->projectId);
        $this->cdnClient = CdnClient::newBuilder()
            ->withHttpConfig($this->config)
            ->withEndpoint('https://cdn.myhuaweicloud.com') // CDN 服務入口
            ->withCredentials($this->cred)
            ->build();
    }

    /**
     * 取得全部 Projects
     * 用 IAM client 取得所有 Project 列表
     */
    public function getAllProjects(): array
    {
        $response = $this->iamClient->keystoneListProjects(new KeystoneListProjectsRequest());

        // 用 collect() 轉成簡單陣列：name/id
        return collect($response->getProjects())->map(fn ($p) => [
            'name' => $p->getName(),
            'id' => $p->getId(),
        ])->toArray();
    }

    /**
     * 取得指定 region 的 Project ID
     */
    public function getProjectIdByRegion(string $region): ?string
    {
        $projects = $this->getAllProjects();
        $project = collect($projects)->firstWhere('name', $region);
        return $project['id'] ?? null;
    }

    /**
     * 根據域名取得 CDN 設定
     */
    public function getDomainByName(string $domain)
    {
        $req = new ListDomainsRequest();
        $req->setDomainName($domain);
        $resp = $this->cdnClient->listDomains($req);

        // 回傳第一個域名（如果有的話）
        return $resp->getDomains()[0] ?? null;
    }

    /**
     * 取得參照域名的 CDN 設定
     */
    public function getReferenceCdnConfig(string $referenceDomain)
    {
        $domain = $this->getDomainByName($referenceDomain);
        if (!$domain) {
            throw new \Exception("Reference domain not found: {$referenceDomain}");
        }

        $originHost = $domain->getDomainOriginHost();
        $customOriginHost = ($originHost && $originHost->getOriginHostType() === 'customize')
            ? $originHost->getCustomizeDomain()
            : null;

        return [
            'id' => $domain->getId(),
            'sources' => $domain->getSources(),
            'business_type' => $domain->getBusinessType(),
            'custom_origin_host' => $customOriginHost,
        ];
    }

    /**
     * 建立新 CDN 域名，依照參照域名設定
     */
    public function createCdnDomainFromReference(string $subdomain, string $referenceDomain)
    {
        $response = null;

        try {
            $cdnConfig = $this->getReferenceCdnConfig($referenceDomain);

            // 建立新域名設定
            $domainBody = (new DomainsWithPort())
                ->setDomainName($subdomain)
                ->setBusinessType($cdnConfig['business_type'])
                ->setSources($cdnConfig['sources'])
                ->setDomainOriginHost(
                    (new DomainOriginHost())
                        ->setOriginHostType('customize')
                        ->setCustomizeDomain($cdnConfig['custom_origin_host'])
                );

            $body = (new CreateDomainRequestBody())->setDomain($domainBody);
            $request = (new CreateDomainRequest())->setBody($body);
            $response = $this->cdnClient->createDomain($request);

            // 單獨修改特定配置 設定回源Header X-Host
            $updateDomainBody = (new DomainsWithPort())
                ->setDomainOriginHost(
                    (new DomainOriginHost())
                        ->setOriginHostType('customize')
                        ->setCustomizeDomain($subdomain)
                )
                ->setOriginRequestHeader([
                    [
                        'header_name'  => 'X-Host',
                        'header_value' => $subdomain,
                        'operation'    => 'set',
                    ],
                ]);

            $updateBody = (new CreateDomainRequestBody())->setDomain($updateDomainBody);
            $updateRequest = (new CreateDomainRequest())->setBody($updateBody);

            return [
                'success' => true,
                'data' => $response,
            ];
        } catch (ConnectionException | RequestTimeoutException $e) {
            // 連線或請求逾時例外
            return [
                'success' => false,
                'type' => get_class($e),
                'message' => $e->getMessage(),
            ];
        } catch (ServiceResponseException $e) {
            // HuaweiCloud API 回傳錯誤
            return [
                'success' => false,
                'type' => get_class($e),
                'http_status' => $e->getHttpStatusCode(),
                'request_id' => $e->getRequestId(),
                'error_code' => $e->getErrorCode(),
                'message' => $e->getErrorMsg(),
            ];
        } catch (\Exception $e) {
            // 其他例外
            return [
                'success' => false,
                'type' => get_class($e),
                'message' => $e->getMessage(),
            ];
        }
    }

    /**
     * 複製 CDN 設定從 sourceDomain 到 targetDomain
     */
    public function copyCdnConfig(string $sourceDomain, string $targetDomain)
    {
        try {
            $source = $this->getDomainByName($sourceDomain);
            $target = $this->getDomainByName($targetDomain);
            if (!$source || !$target) {
                throw new \Exception("CDN 找不到 {$sourceDomain} or {$targetDomain}");
            }

            // 設定要複製的項目
            $configs = new BatchCopyConfigs([
                'sourceDomain' => $sourceDomain,
                'targetDomain' => $targetDomain,
                'configList' => [
                    'originRequestHeader',
                    'httpResponseHeader',
                    'urlAuth',
                    'userAgentBlackAndWhiteList',
                    'ipv6Accelerate',
                    'rangeStatus',
                    'cacheRules',
                    'follow302Status',
                    'sources',
                    'compress',
                    'referer',
                    'ipBlackAndWhiteList',
                    'browserCacheRules',
                    'cacheValidErrorCode'
                ]
            ]);

            $body = new BatchCopyDRequestBody(['configs' => $configs]);
            $request = new BatchCopyDomainRequest(['body' => $body]);

            $response = $this->cdnClient->batchCopyDomain($request);

            return [
                'success' => true,
                'response' => $response,
            ];
        } catch (ConnectionException | RequestTimeoutException $e) {
            return [
                'success' => false,
                'type' => get_class($e),
                'message' => $e->getMessage(),
            ];
        } catch (ServiceResponseException $e) {
            return [
                'success' => false,
                'type' => get_class($e),
                'http_status' => $e->getHttpStatusCode(),
                'request_id' => $e->getRequestId(),
                'error_code' => $e->getErrorCode(),
                'message' => $e->getErrorMsg(),
            ];
        } catch (\Exception $e) {
            return [
                'success' => false,
                'type' => get_class($e),
                'message' => $e->getMessage(),
            ];
        }
    }

    /**
     * 啟用 CDN HTTPS
     * 讀取本地 letsencrypt 憑證，並上傳到 Huawei CDN
     */
    function enableCdnHttps(string $domainName)
    {
        $root_domain = $this->getRootDomain($domainName);
        $domainPath = rtrim('/etc/letsencrypt/live', '/') . '/' . $root_domain;

        if (!is_dir($domainPath)) {
            throw new \Exception("找不到目錄：{$domainPath}");
        }

        $fullchainLink = readlink("{$domainPath}/fullchain.pem");
        if (!$fullchainLink) {
            throw new \Exception("無法讀取 fullchain.pem");
        }

        // 取得憑證版本號
        if (preg_match('/fullchain(\d*)\.pem$/', $fullchainLink, $matches)) {
            $version = $matches[1] ?: '1';
        } else {
            $version = '1';
        }

        $certName = "{$root_domain}-{$version}";
        $fullchainPath = "{$domainPath}/fullchain.pem";
        $privkeyPath   = "{$domainPath}/privkey.pem";

        if (!file_exists($fullchainPath) || !file_exists($privkeyPath)) {
            throw new \Exception("找不到憑證 {$domainPath}");
        }

        $certContent = file_get_contents($fullchainPath);
        $privateKey  = file_get_contents($privkeyPath);

        $content = new UpdateDomainMultiCertificatesRequestBodyContent([
            'certificateSource' => 0, // 上傳憑證
            'certName' => $certName,
            'certificate' => $certContent,
            'privateKey' => $privateKey,
            'httpsStatus' => 'on',
            'forceRedirectHttps' => 0,
            'httpsSwitch' => 1,
            'domainName' => $domainName,
        ]);

        $body = new UpdateDomainMultiCertificatesRequestBody([
            'https' => $content,
        ]);

        $request = new UpdateDomainMultiCertificatesRequest([
            'body' => $body,
        ]);

        try {
            $response = $this->cdnClient->updateDomainMultiCertificates($request);
            return [
                'success' => true,
                'response' => $response,
            ];
        } catch (ConnectionException | RequestTimeoutException $e) {
            return [
                'success' => false,
                'type' => get_class($e),
                'message' => $e->getMessage(),
            ];
        } catch (ServiceResponseException $e) {
            return [
                'success' => false,
                'type' => get_class($e),
                'http_status' => $e->getHttpStatusCode(),
                'request_id' => $e->getRequestId(),
                'error_code' => $e->getErrorCode(),
                'message' => $e->getErrorMsg(),
            ];
        } catch (\Exception $e) {
            return [
                'success' => false,
                'type' => get_class($e),
                'message' => $e->getMessage(),
            ];
        }
    }

    /**
     * 取得主域名（root domain）
     * 例如：sub.example.com -> example.com
     */
    public function getRootDomain($host)
    {
        $parts = explode('.', $host);
        $numParts = count($parts);
        if ($numParts >= 2) {
            $rootDomain = $parts[$numParts - 2] . '.' . $parts[$numParts - 1];
            return $rootDomain;
        }
        return $host;
    }

    /**
     * 查詢已添加的加速域名歸屬校驗資訊
     */
    public function domainVerifies($domain)
    {
        $request = new ShowVerifyDomainOwnerInfoRequest();
        $request->setDomainName($domain);

        try {
            $response = $this->cdnClient->showVerifyDomainOwnerInfo($request);
            $dnsType = $response->getDnsVerifyType();
            $dnsName = $response->getDnsVerifyName();
            $dnsValue = $response->getVerifyContent();

            $fileUrl = $response->getFileVerifyUrl();
            $fileName = $response->getFileVerifyFilename();
            $fileContent = $response->getVerifyContent();

            $domainName = $response->getDomainName();
            $requestId = $response->getXRequestId();
            return [
                'success' => true,
                'dns' => [
                    'type' => $dnsType,
                    'name' => $dnsName,
                    'value' => $dnsValue,
                ],
                'file' => [
                    'url' => $fileUrl,
                    'filename' => $fileName,
                    'content' => $fileContent,
                ],
                'domain' => $domainName
            ];
        } catch (ConnectionException | RequestTimeoutException $e) {
            return [
                'success' => false,
                'type' => get_class($e),
                'message' => $e->getMessage(),
            ];
        } catch (ServiceResponseException $e) {
            return [
                'success' => false,
                'type' => get_class($e),
                'http_status' => $e->getHttpStatusCode(),
                'request_id' => $e->getRequestId(),
                'error_code' => $e->getErrorCode(),
                'message' => $e->getErrorMsg(),
            ];
        } catch (\Exception $e) {
            return [
                'success' => false,
                'type' => get_class($e),
                'message' => $e->getMessage(),
            ];
        }
    }
}
```

## 建立 → 再更新

[修改域名全量配置接口（UpdateDomainFullConfig）官方中文文檔](https://support.huaweicloud.com/api-cdn/UpdateDomainFullConfig.html)

| 動作                     |  Model                                                    |
| ---------------------- | ----------------------------------------------------------- |
| createDomain           | CreateDomainRequest / Body                                  |
| updateDomainFullConfig | **UpdateDomainFullConfigRequest + UpdateDomainRequestBody** |


```php
// 建立 domain（保留參照域名設定）
$response = $this->cdnClient->createDomain($request);

// ===== 建立完成後，單獨修改設定 =====

// 要更新的內容
$updateDomainBody = (new DomainsWithPort())
    ->setDomainOriginHost(
        (new DomainOriginHost())
            ->setOriginHostType('customize')
            ->setCustomizeDomain($subdomain) // ⭐ 回源 Host 改為新域名
    )
    ->setOriginRequestHeader([
        [
            'header_name'  => 'X-Host',
            'header_value' => $subdomain,
            'operation'    => 'set',
        ],
    ]);

// Update 專用 Body
$updateBody = (new UpdateDomainRequestBody())
    ->setDomain($updateDomainBody);

// Update 專用 Request（一定要指定 domainName）
$updateRequest = (new UpdateDomainFullConfigRequest())
    ->setDomainName($subdomain)
    ->setBody($updateBody);

// 呼叫 update API
$this->cdnClient->updateDomainFullConfig($updateRequest);
```

## 建立新的 CDN 加速域名

[建立加速網域（CreateDomain） 官方中文文檔（Huawei Cloud CDN API）](https://support.huaweicloud.com/intl/zh-cn/api-cdn/CreateDomain.html)

```php
$domainBody = (new DomainsWithPort())
    ->setDomainName('cdn.example.com')
    ->setBusinessType('web')
    ->setSources([
        [
            'ip_or_domain' => '1.1.1.1',
            'origin_type'  => 'ipaddr',
            'active_standby' => 1,
        ]
    ]);

$body = (new CreateDomainRequestBody())
    ->setDomain($domainBody);

$request = (new CreateDomainRequest())
    ->setBody($body);

$response = $this->cdnClient->createDomain($request);
```
