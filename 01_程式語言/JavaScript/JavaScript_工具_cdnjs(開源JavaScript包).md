# JavaScript 工具 cdnjs(開源JavaScript包)

```
cdnjs（Content Delivery Network for JavaScript libraries）是另一個為前端開發者提供開源JavaScript庫的全球性CDN。
類似於 UNPKG 和 JSdelivr，cdnjs 使得開發者能夠通過 CDN 引用和使用各種 JavaScript 库，而無需將這些库文件下载到本地。
```

## 目錄

- [JavaScript 工具 cdnjs(開源JavaScript包)](#javascript-工具-cdnjs開源javascript包)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
    - [資安相關](#資安相關)
- [用法](#用法)
- [資安](#資安)
  - [Remote code execution(RCE)](#remote-code-executionrce)

## 參考資料

[官方網站](https://cdnjs.com/)

### 資安相關

[請儘速遠離 cdn.polyfill.io 之惡意程式碼淺析](https://blog.huli.tw/2024/06/25/stop-using-polyfill-io/)

[從 cdnjs 的漏洞來看前端的供應鏈攻擊與防禦](https://blog.huli.tw/2021/08/22/cdnjs-and-supply-chain-attack/)

RCE 漏洞, 讓攻擊者執行任意程式碼，是風險等級很高的漏洞。

[Remote code execution in cdnjs of Cloudflare](https://blog.ryotak.net/post/cdnjs-remote-code-execution-en/)

RCE 漏洞 修正

[Cloudflare's Handling of an RCE Vulnerability in cdnjs](https://blog.cloudflare.com/cloudflares-handling-of-an-rce-vulnerability-in-cdnjs/)

# 用法

通過以下格式引用特定庫的特定版本

```
https://cdnjs.cloudflare.com/ajax/libs/library/version/file
```

例如 引用 jQuery 的3.6.0版本

```
https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js
```

# 資安

## Remote code execution(RCE)

```
讓攻擊者執行任意程式碼，是風險等級很高的漏洞。
```

一名資安研究員 @ryotkak 在他的部落格上發布了一篇文章，名為：Remote code execution in cdnjs of Cloudflare（以下用「作者」來稱呼）。

首先呢，Cloudflare 有把 cdnjs 相關的程式碼開源在 GitHub 上面，而其中有一個自動更新的功能引起了作者的注意。這個功能會自動去抓 npm 上打包好的 package 檔案，格式是壓縮檔 .tgz，解壓縮之後把檔案做一些處理，複製到合適的位置。

假設今天你有一段程式碼是複製檔案，然後做了類似底下的操作：

用目的地 + 檔名拼湊出目標位置，建立新檔案
讀取原本檔案，寫入新檔案
如果目的地是 /packages/test，檔名是 abc.js，那最後就會在 /packages/test/abc.js 產生新的檔案。

這時候若是目的地一樣，檔名是 ../../../tmp/abc.js，就會在 /package/test/../../../tmp/abc.js 也就是 /tmp/abc.js 底下寫入檔案。

因此透過這樣的手法，可以寫入檔案到任何有權限的地方！而 cdnjs 的程式碼就有類似的漏洞，能夠寫入檔案到任意位置。如果能利用這漏洞，去覆蓋掉原本就會定時自動執行的檔案的話，就可以達成 RCE 了。

當作者正想要做個 POC 來驗證的時候，突然很好奇針對 Git 自動更新的功能是怎麼做的（上面講的關於壓縮檔的是針對 npm 的）

而研究過後，作者發現關於 Git repo 的自動更新，有一段複製檔案的程式碼，長這個樣子：

```Javascript
func MoveFile(sourcePath, destPath string) error {
    inputFile, err := os.Open(sourcePath)
    if err != nil {
        return fmt.Errorf("Couldn't open source file: %s", err)
    }
    outputFile, err := os.Create(destPath)
    if err != nil {
        inputFile.Close()
        return fmt.Errorf("Couldn't open dest file: %s", err)
    }
    defer outputFile.Close()
    _, err = io.Copy(outputFile, inputFile)
    inputFile.Close()
    if err != nil {
        return fmt.Errorf("Writing to output file failed: %s", err)
    }
    // The copy was successful, so now delete the original file
    err = os.Remove(sourcePath)
    if err != nil {
        return fmt.Errorf("Failed removing original file: %s", err)
    }
    return nil
}
```

看起來沒什麼，就是複製檔案而已，開啟一個新檔案，把舊檔案的內容複製進去。

但如果這個原始檔案是個 symbolic link 的話，就不一樣了。在繼續往下之前，先簡單介紹一下什麼是 symbolic link。

Symbolic link 的概念有點像是以前在 Windows 上看到的「捷徑」，這個捷徑本身只是一個連結，連到真正的目標去。

在類 Unix 系統裡面可以用 ln -s 目標檔案 捷徑名稱 去建立一個 symbolic link，這邊直接舉一個例子會更好懂。

我先建立一個檔案，內容是 hello，位置是 /tmp/hello。接著我在當前目錄底下建立一個 symbolic link，指到剛剛建立好的 hello 檔案：ln -s /tmp/hello link_file

接著我如果印出 link_file 的內容，會出現 hello，因為其實就是在印出 /tmp/hello 的內容。如果我對 link_file 寫入資料，實際上也是對 /tmp/hello 寫入。

再來我們試試看用 Node.js 寫一段複製檔案的程式碼，看看會發生什麼事：

```bash
node -e 'require("fs").copyFileSync("link_file", "test.txt")'
```

執行完成之後，我們發現目錄底下多了一個 test.txt 的檔案，內容是 /tmp/hello 的檔案內容。

所以用程式在執行複製檔案時，並不是「複製一個 symbolic link」，而是「複製指向的檔案內容」。

因此呢，我們剛剛提到的 Go 複製檔案的程式碼，如果有個檔案是指向 /etc/passwd 的 symbolic link，複製完以後就會產生出一個內容是 /etc/passwd 的檔案。

我們可以在 Git 的檔案裡面加一個 symbolic link 名稱叫做 test.js，讓它指向 /etc/passwd，這樣被 cdnjs 複製過後，就會產生一個 test.js 的檔案，而且裡面是 /etc/passwd 的內容！

如此一來，就得到了一個任意檔案讀取（Arbitrary File Read）的漏洞。

講到這邊稍微做個總結，作者一共找到兩個漏洞，一個可以寫檔案一個可以讀檔案，寫檔案如果不小心覆蓋重要檔案會讓系統掛掉，因此作者決定從讀檔案開始做 POC，自己建了一個 Git 倉庫然後發佈新版本，等 cdnjs 去自動更新，最後觸發檔案讀取的漏洞，在 cdnjs 發布的 JS 上面就可以看到讀到的檔案內容。

而作者讀的檔案是 /proc/self/environ（他本來是想讀另一個 /proc/self/maps），這裡面有著環境變數，而且有一把 GitHub 的 api key 也在裡面，這把 key 對 cdnjs 底下的 repo 有寫入權限，所以利用這把 key，可以直接去改 cdnjs 或是 cdnjs 網站的程式碼，進而控制整個服務。

以上就是關於 cdnjs 漏洞的解釋，想看更多技術細節或是詳細發展的話，可以去看原作者的部落格文章，裡面記錄了許多細節。總之呢，就算是大公司在維護的服務，也是有被入侵的風險存在。

而 Cloudflare 也在一週後發佈了事件處理報告：Cloudflare’s Handling of an RCE Vulnerability in cdnjs，記錄了事情發生的始末以及事後的修補措施，他們把整個架構都重寫了，把原本解壓縮的部分放到 Docker sandbox 裡面，增加了整體的安全性。
