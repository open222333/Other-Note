# Android 建置與打包

```bash
# 建置 Release APK
./gradlew assembleRelease

# 建置 Release AAB（上架 Google Play 使用）
./gradlew bundleRelease
```

## 目錄

- [Android 建置與打包](#android-建置與打包)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [環境需求](#環境需求)
- [建置類型說明](#建置類型說明)
  - [APK vs AAB](#apk-vs-aab)
  - [Build Variant](#build-variant)
- [Keystore 簽署設定](#keystore-簽署設定)
  - [建立 Keystore 檔](#建立-keystore-檔)
  - [設定 build.gradle 簽署](#設定-buildgradle-簽署)
  - [使用環境變數（CI 推薦）](#使用環境變數ci-推薦)
- [建置方式](#建置方式)
  - [Android Studio GUI 手動建置](#android-studio-gui-手動建置)
  - [Gradle CLI 建置](#gradle-cli-建置)
- [對齊與簽署（手動流程）](#對齊與簽署手動流程)
- [上架 Google Play](#上架-google-play)
  - [使用 Google Play Console 手動上傳](#使用-google-play-console-手動上傳)
  - [使用 Google Play Developer API](#使用-google-play-developer-api)
- [發佈類型對照](#發佈類型對照)

## 參考資料

[Build your app from the command line — Android Developers](https://developer.android.com/build/building-cmdline)

[Sign your app — Android Developers](https://developer.android.com/studio/publish/app-signing)

[Publish your app — Android Developers](https://developer.android.com/studio/publish)

[Android App Bundle — Google Play](https://developer.android.com/guide/app-bundle)

---

# 環境需求

| 工具 | 說明 |
|---|---|
| JDK | 建議 JDK 17（Gradle 8.x 需求） |
| Android Studio | 或只安裝 Android SDK Command-line Tools |
| Gradle | 通常由專案內的 `gradlew` wrapper 管理 |
| Google Play Developer Account | $25 一次性費用 |

```bash
# 確認 Java 版本
java -version

# 確認 Gradle wrapper 版本
./gradlew --version

# 列出所有可用 Task
./gradlew tasks
```

---

# 建置類型說明

## APK vs AAB

| 格式 | 副檔名 | 用途 |
|---|---|---|
| APK | `.apk` | 直接安裝至裝置、Ad Hoc 發佈 |
| AAB | `.aab` | Google Play 上架（官方推薦，2021 年後強制） |

> AAB 由 Google Play 動態最佳化後再產生 APK，體積比直接上傳 APK 小約 15%。

## Build Variant

```
BuildType（debug / release）× ProductFlavor（dev / staging / prod）
→ BuildVariant = productFlavorBuildType
```

```bash
# 列出所有 Variant
./gradlew :app:tasks --all | grep assemble
```

---

# Keystore 簽署設定

## 建立 Keystore 檔

```bash
keytool -genkeypair \
  -v \
  -keystore my-release-key.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias my-key-alias

# 查看 Keystore 資訊
keytool -list -v -keystore my-release-key.jks
```

> ⚠️ **務必備份 Keystore 檔案與密碼**，遺失後無法更新 Google Play 上的同一 App。

## 設定 build.gradle 簽署

```groovy
// app/build.gradle
android {
    signingConfigs {
        release {
            storeFile file("my-release-key.jks")
            storePassword "your_store_password"
            keyAlias "my-key-alias"
            keyPassword "your_key_password"
        }
    }

    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

## 使用環境變數（CI 推薦）

```groovy
// app/build.gradle（從環境變數讀取，避免密碼寫入版本控制）
signingConfigs {
    release {
        storeFile file(System.getenv("KEYSTORE_PATH") ?: "my-release-key.jks")
        storePassword System.getenv("KEYSTORE_PASS")
        keyAlias System.getenv("KEY_ALIAS")
        keyPassword System.getenv("KEY_PASS")
    }
}
```

```bash
# 設定環境變數後執行
export KEYSTORE_PATH=/path/to/my-release-key.jks
export KEYSTORE_PASS=your_store_password
export KEY_ALIAS=my-key-alias
export KEY_PASS=your_key_password

./gradlew bundleRelease
```

---

# 建置方式

## Android Studio GUI 手動建置

**產生 APK：**
1. 選單 **Build → Generate Signed Bundle / APK**
2. 選擇 **APK**
3. 選擇或建立 Keystore
4. 選擇 **release** Build Variant
5. 點擊 **Finish**，輸出至 `app/release/`

**產生 AAB：**
1. 選單 **Build → Generate Signed Bundle / APK**
2. 選擇 **Android App Bundle**
3. 後續步驟同上

## Gradle CLI 建置

```bash
# 建置 Release APK（需已設定 signingConfig）
./gradlew assembleRelease

# 建置 Release AAB
./gradlew bundleRelease

# 建置 Debug APK（不需 Keystore）
./gradlew assembleDebug

# 指定 Flavor + BuildType
./gradlew assembleProdRelease
./gradlew bundleProdRelease

# 清除建置快取
./gradlew clean

# 輸出路徑
# APK: app/build/outputs/apk/release/app-release.apk
# AAB: app/build/outputs/bundle/release/app-release.aab
```

---

# 對齊與簽署（手動流程）

> 通常 `signingConfig` 已包含此步驟，以下為手動操作備用。

```bash
# Step 1: 建置未簽署的 APK
./gradlew assembleRelease  # 產生 app-release-unsigned.apk

# Step 2: 使用 zipalign 對齊（改善執行效能）
zipalign -v 4 \
  app-release-unsigned.apk \
  app-release-aligned.apk

# Step 3: 使用 apksigner 簽署
apksigner sign \
  --ks my-release-key.jks \
  --ks-key-alias my-key-alias \
  --out app-release-signed.apk \
  app-release-aligned.apk

# 驗證簽署
apksigner verify app-release-signed.apk
```

---

# 上架 Google Play

## 使用 Google Play Console 手動上傳

1. 登入 [Google Play Console](https://play.google.com/console)
2. 選擇應用程式 → **發行版本**
3. 選擇發行軌道（內部測試 / 封測 / 公開測試 / 正式版）
4. 點擊 **建立新版本**
5. 上傳 `.aab` 檔案
6. 填寫版本說明 → **儲存並發布**

## 使用 Google Play Developer API

```bash
# 安裝 Google API 工具（Python 範例）
pip install google-api-python-client google-auth

# 使用 fastlane supply（推薦）
# 安裝 fastlane
gem install fastlane

# 初始化
fastlane supply init

# 上傳 AAB
fastlane supply \
  --aab app/build/outputs/bundle/release/app-release.aab \
  --track internal \
  --json_key path/to/service-account.json \
  --package_name com.example.myapp
```

---

# 發佈類型對照

| 類型 | 使用情境 | 裝置限制 | 審核 |
|---|---|---|---|
| Debug APK | 開發測試，直接安裝 | 無限制（需啟用未知來源） | 不需要 |
| Release APK | Ad Hoc 發佈、企業內部 | 無限制（需啟用未知來源） | 不需要 |
| 內部測試軌道 | 開發團隊測試 | 最多 100 名測試員 | 基本審查 |
| 封測軌道 | 封閉 Beta 測試 | 受邀用戶 | 需審核 |
| 公開測試軌道 | 開放 Beta 測試 | 所有用戶（自願加入） | 需審核 |
| 正式版 | 正式上架 | 所有用戶 | 需審核 |
