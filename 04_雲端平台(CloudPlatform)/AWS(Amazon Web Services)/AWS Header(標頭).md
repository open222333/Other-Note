# AWS Header(標頭)


## 目錄

- [AWS Header(標頭)](#aws-header標頭)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
	- [緩存配置-刪除Cloudfront Header](#緩存配置-刪除cloudfront-header)

## 參考資料

[為自訂原始伺服器之請求和回應行為](https://docs.aws.amazon.com/zh_tw/AmazonCloudFront/latest/DeveloperGuide/RequestAndResponseBehaviorCustomOrigin.html)


## 緩存配置-刪除Cloudfront Header

`X-Amz-Request-Id`
`X-Amz-Id-2`

```
使用 HTTP 取得要求 ID
可以在 HTTP 要求傳抵目標應用程式之前，先記錄 HTTP 要求的位元數，從而取得自己的要求 ID
```

```http
x-amz-request-id: 79104EXAMPLEB723
x-amz-id-2: IOWQ4fDEXAMPLEQM+ey7N9WgVhSnQ6JEXAMPLEZb7hSQDASK+Jd1vEXAMPLEa3Km
```

`X-Amz-Cf-Pop`

```
AWS 邊緣基礎設施的標頭值(猜測)
https://www.reddit.com/r/aws/comments/iz9tzj/what_is_mean_that_cloudfront_xamzcfpop_header/

告知由哪裡提供服務

會提供收到回應的時間
```

`Via`

```
CloudFront 轉送標頭至原始伺服器。
```

`X-Amz-Cf-Id`

```
CloudFront 在轉送請求至原始伺服器之前，將標頭加入檢視器。
此標頭值包含可唯一識別請求的加密字串。
```
