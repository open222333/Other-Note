# Golang 腳本規範

```
撰寫 Go 小工具與 CLI 程式的統一約束：專案結構、命名、錯誤處理、檢查工具。
以官方 gofmt / go vet 為基礎。
```

## 目錄

- [Golang 腳本規範](#golang-腳本規範)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [基本結構](#基本結構)
- [命名](#命名)
- [相依管理](#相依管理)
- [參數與設定](#參數與設定)
- [錯誤處理與日誌](#錯誤處理與日誌)
- [檢查工具](#檢查工具)
- [建置與發佈](#建置與發佈)

## 參考資料

[Effective Go](https://go.dev/doc/effective_go)

[Go 官方文檔](https://go.dev/doc/)

[Go(Golang) 筆記](Go(Golang)_筆記.md)

# 基本結構

- 單檔小工具：`package main` + `main()`，邏輯拆函式，`main()` 只做參數解析與呼叫。
- 錯誤統一在 `run()` 回傳，`main()` 收尾退出，避免到處 `os.Exit`：

```go
package main

// 用途：同步圖片到 CDN（一行說明）
// 用法：go run . <tag> [-cleanup]

func main() {
	if err := run(); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(1)
	}
}

func run() error {
	// ...
	return nil
}
```

# 命名

- 套件名：全小寫單字；匯出識別字 `PascalCase`、未匯出 `camelCase`。
- 縮寫維持大小寫一致：`ServeHTTP`、`userID`（非 `Http`、`Id`）。
- 檔名小寫底線：`sync_images.go`。

# 相依管理

- 一律 Go Modules：`go mod init` / `go mod tidy`，提交 `go.mod` 與 `go.sum`。
- `go.mod` 指定 Go 版本。

# 參數與設定

- 旗標用標準庫 `flag`；子命令多時再考慮 `cobra`。
- 環境相關值集中常數區或讀環境變數 `os.Getenv`，密碼、token 不寫死。

# 錯誤處理與日誌

- 錯誤逐層回傳並包語境：`fmt.Errorf("讀取設定: %w", err)`；禁止 `_` 丟棄錯誤。
- 判斷特定錯誤用 `errors.Is` / `errors.As`。
- 日誌用 `log`（簡單）或 `log/slog`（結構化），狀態輸出走 stderr、資料輸出走 stdout。
- 長時間任務定期輸出進度。

# 檢查工具

- 提交前必跑：

```bash
gofmt -w .          # 格式化（或 goimports -w .）
go vet ./...        # 官方靜態檢查
go test ./...       # 有測試就跑

# 進階（可選）
golangci-lint run
```

# 建置與發佈

- 建置產物不進版控；跨平台用環境變數：

```bash
GOOS=linux GOARCH=amd64 go build -o bin/tool_linux .
```

- 版本資訊用 `-ldflags "-X main.version=..."` 注入，不寫死。
