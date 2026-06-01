# iOS 建置與打包

```bash
# 建置並封存 (Archive)
xcodebuild archive \
  -workspace MyApp.xcworkspace \
  -scheme MyApp \
  -configuration Release \
  -archivePath ./build/MyApp.xcarchive
```

## 目錄

- [iOS 建置與打包](#ios-建置與打包)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [環境需求](#環境需求)
- [憑證與簽署設定](#憑證與簽署設定)
  - [Certificate 類型](#certificate-類型)
  - [Provisioning Profile 類型](#provisioning-profile-類型)
  - [設定方式（Xcode GUI）](#設定方式xcode-gui)
- [建置方式](#建置方式)
  - [Xcode GUI 手動建置](#xcode-gui-手動建置)
  - [xcodebuild CLI 建置](#xcodebuild-cli-建置)
- [打包（Export IPA）](#打包export-ipa)
  - [ExportOptions.plist 設定](#exportoptionsplist-設定)
  - [匯出指令](#匯出指令)
- [上架 App Store](#上架-app-store)
  - [使用 Xcode Organizer 上傳](#使用-xcode-organizer-上傳)
  - [使用 altool CLI 上傳](#使用-altool-cli-上傳)
  - [使用 xcrun notarytool（macOS 12+）](#使用-xcrun-notarytoolmacos-12)
- [發佈類型對照](#發佈類型對照)

## 參考資料

[Xcode Build System — Apple Developer](https://developer.apple.com/documentation/xcode/build-system)

[xcodebuild — Apple Developer Documentation](https://developer.apple.com/documentation/xcode/building-swift-packages-or-apps-that-use-them-in-continuous-integration-workflows)

[Distributing Your App for Beta Testing and Releases — Apple](https://developer.apple.com/documentation/xcode/distributing-your-app-for-beta-testing-and-releases)

---

# 環境需求

| 工具 | 說明 |
|---|---|
| macOS | 必須在 Mac 上建置，建議使用最新 stable 版 |
| Xcode | 從 App Store 安裝，版本需對應目標 iOS SDK |
| Apple Developer Account | 需要付費帳號（$99/年）才能發佈 App Store |
| CocoaPods / SPM | 若有第三方套件依賴 |

```bash
# 確認 Xcode Command Line Tools 已安裝
xcode-select --install

# 確認 Xcode 版本
xcodebuild -version

# 列出可用 SDK
xcodebuild -showsdks
```

---

# 憑證與簽署設定

## Certificate 類型

| 類型 | 用途 |
|---|---|
| Apple Development | 開發測試（Debug） |
| Apple Distribution | App Store 上架、Ad Hoc 發佈 |
| iOS Distribution | 舊版命名，功能同上 |

## Provisioning Profile 類型

| 類型 | 說明 |
|---|---|
| Development | 開發裝置測試，需登記 UDID |
| Ad Hoc | 最多 100 台裝置，不需 App Store 審核 |
| App Store | App Store 正式上架 |
| Enterprise | 企業內部發佈（需 Enterprise 帳號） |

## 設定方式（Xcode GUI）

1. 開啟 Xcode → 選擇 Target → **Signing & Capabilities**
2. 勾選 **Automatically manage signing**（自動管理，適合開發）
3. 選擇正確的 **Team**
4. Bundle Identifier 需與 Apple Developer Portal 一致

---

# 建置方式

## Xcode GUI 手動建置

1. 選擇目標裝置為 **Any iOS Device (arm64)**
2. 選單 **Product → Archive**
3. 等待 Archive 完成後，**Organizer** 視窗自動開啟

## xcodebuild CLI 建置

```bash
# 使用 xcworkspace（有 CocoaPods 時使用）
xcodebuild archive \
  -workspace MyApp.xcworkspace \
  -scheme MyApp \
  -configuration Release \
  -destination "generic/platform=iOS" \
  -archivePath ./build/MyApp.xcarchive \
  CODE_SIGN_STYLE=Manual \
  CODE_SIGN_IDENTITY="Apple Distribution: Your Name (TEAMID)" \
  PROVISIONING_PROFILE_SPECIFIER="MyApp_AppStore_Profile"

# 使用 xcodeproj（無 CocoaPods）
xcodebuild archive \
  -project MyApp.xcodeproj \
  -scheme MyApp \
  -configuration Release \
  -destination "generic/platform=iOS" \
  -archivePath ./build/MyApp.xcarchive

# 清除建置快取
xcodebuild clean \
  -workspace MyApp.xcworkspace \
  -scheme MyApp
```

---

# 打包（Export IPA）

## ExportOptions.plist 設定

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN"
  "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
  <!-- 發佈方式: app-store / ad-hoc / enterprise / development -->
  <key>method</key>
  <string>app-store</string>

  <key>teamID</key>
  <string>XXXXXXXXXX</string>

  <!-- 是否上傳符號檔 (Bitcode 已棄用，設 false) -->
  <key>uploadBitcode</key>
  <false/>

  <key>uploadSymbols</key>
  <true/>

  <!-- app-store 時設為 true 可直接上傳 -->
  <key>destination</key>
  <string>export</string>

  <key>signingStyle</key>
  <string>manual</string>

  <key>provisioningProfiles</key>
  <dict>
    <key>com.example.MyApp</key>
    <string>MyApp_AppStore_Profile</string>
  </dict>
</dict>
</plist>
```

## 匯出指令

```bash
# 從 .xcarchive 匯出 IPA
xcodebuild -exportArchive \
  -archivePath ./build/MyApp.xcarchive \
  -exportPath ./build/output \
  -exportOptionsPlist ./ExportOptions.plist

# 輸出結果：./build/output/MyApp.ipa
```

---

# 上架 App Store

## 使用 Xcode Organizer 上傳

1. **Xcode → Window → Organizer**
2. 選擇剛建立的 Archive
3. 點擊 **Distribute App**
4. 選擇 **App Store Connect** → **Upload**
5. 依據引導完成上傳

## 使用 altool CLI 上傳

```bash
# 上傳 IPA 至 App Store Connect
xcrun altool --upload-app \
  --type ios \
  --file ./build/output/MyApp.ipa \
  --username "your@apple.id" \
  --password "@keychain:AC_PASSWORD"

# 驗證 IPA（不實際上傳）
xcrun altool --validate-app \
  --type ios \
  --file ./build/output/MyApp.ipa \
  --username "your@apple.id" \
  --password "@keychain:AC_PASSWORD"
```

## 使用 xcrun notarytool（macOS 12+）

```bash
# 儲存 API Key 憑證（只需執行一次）
xcrun notarytool store-credentials "AC_PASSWORD" \
  --apple-id "your@apple.id" \
  --team-id "XXXXXXXXXX" \
  --password "xxxx-xxxx-xxxx-xxxx"

# 上傳
xcrun altool --upload-app \
  --type ios \
  --file ./build/output/MyApp.ipa \
  --apiKey YOUR_API_KEY \
  --apiIssuer YOUR_ISSUER_ID
```

---

# 發佈類型對照

| 類型 | 使用情境 | 裝置限制 | 審核 |
|---|---|---|---|
| Development | 開發測試 | 已登記 UDID | 不需要 |
| Ad Hoc | 外部測試、客戶驗收 | 最多 100 台（需 UDID） | 不需要 |
| TestFlight | Beta 測試 | 無限制 | 需 App Store 審核 |
| App Store | 正式上架 | 所有用戶 | 需要 |
| Enterprise | 企業內部發佈 | 無限制（同組織） | 不需要（需 Enterprise 帳號） |
