# Java 工具 Maven(管理 Java 專案的建構)

```
Maven 是一個建構（Build）工具，用於管理 Java 專案的建構過程。
它可以自動化專案的編譯、打包、測試和部署等工作。
Maven 通過一個名為 POM（Project Object Model）的 XML 文件來描述專案的結構和相依關係，並且可以自動下載所需的依賴庫。
這使得開發人員可以更加專注於代碼的開發，而不必擔心底層的建構細節。
```

## 目錄

- [Java 工具 Maven(管理 Java 專案的建構)](#java-工具-maven管理-java-專案的建構)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
  - [Windows](#windows)
  - [Linux](#linux)
  - [MacOS](#macos)
  - [](#)
- [指令](#指令)
- [依賴文檔](#依賴文檔)

## 參考資料

[官方網站](https://maven.apache.org/)

[官方網站 下載網址](https://maven.apache.org/download.cgi)

# 安裝

## Windows

下載 Maven 壓縮包

前往 Apache Maven 的官方網站，下載最新版本的 Maven 壓縮包（.zip 格式）。

設置環境變數：

    在 Windows 搜尋欄中輸入「環境變數」，點擊「編輯系統環境變數」。

    在系統屬性窗口中，點擊「環境變數」按鈕。

    在系統變數區域中，新增一個名為 MAVEN_HOME 的變數，設置其值為 Maven 的安裝路徑，例如 C:\Program Files\Apache\maven。

    在系統變數中找到 Path 變數，點擊編輯，新增 %MAVEN_HOME%\bin 到變數值中。

## Linux

解壓 Maven 壓縮包

```bash
tar -zxvf apache-maven-$version-bin.tar.gz
```

環境變數

```bash
export MAVEN_HOME=/path/to/apache-maven-<version>
export PATH=$PATH:$MAVEN_HOME/bin
source ~/.bashrc
```

## MacOS

```bash
brew install maven
```

##

# 指令

`Maven 的版本信息`

```bash
mvn -v
```

`創建新的 Java 專案`

```bash
mvn archetype:generate
```

```bash
mvn archetype:generate -DgroupId=com.example -DartifactId=my-project -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

groupId：指定專案的 Group ID，例如 com.example。

artifactId：指定專案的 Artifact ID，例如 my-project。

archetypeArtifactId：指定使用的 archetype，這裡使用的是 maven-archetype-quickstart，它是一個快速生成基本 Java 專案的範本。

interactiveMode=false：這個參數表示不使用交互模式，直接使用默認值生成專案。

編譯專案

```bash
mvn compile
```

測試專案

執行專案中的測試用例並生成測試報告。

```bash
mvn test
```

打包專案

將編譯後的代碼打包成 JAR、WAR 或其他形式的分發包。

```bash
mvn package
```

安裝專案

將專案依賴作為庫供其他專案使用非常有用。

```bash
mvn install
```

清理專案

這將刪除生成的編譯文件和打包文件，讓專案回到初始狀態。

```bash
mvn clean
```

# 依賴文檔

pom.xml

定義了一個名為 my-project 的 Maven 專案，該專案依賴於 JUnit 測試庫（版本 4.12）。
它還配置了 Maven 編譯插件（maven-compiler-plugin）來指定 Java 源代碼的版本為 1.8。

以下是一些常用的 Maven 命令，可以在專案目錄中的終端（Terminal）中執行：

mvn clean：清理專案，刪除生成的編譯文件和打包文件。
mvn compile：編譯專案，將源代碼編譯成類文件。
mvn test：運行單元測試，執行專案中的測試用例。
mvn package：打包專案，將編譯後的代碼打包成 JAR、WAR 或其他形式的分發包。
mvn install：安裝專案到本地 Maven 庫中，對於將專案依賴作為庫供其他專案使用非常有用。
這些命令可以在不同的階段使用，例如在開發過程中可以先執行 mvn compile 和 mvn test，然後再執行 mvn package 來打包分發。

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>my-project</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```
