# Linux 工具 chmod(權限)

```
```

## 目錄

- [Linux 工具 chmod(權限)](#linux-工具-chmod權限)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)

## 參考資料

[chmod(2) — Linux manual page](https://man7.org/linux/man-pages/man2/chmod.2.html)

# 指令

更改文件所有者

```bash
chown user:group file.txt
```

遞歸更改目錄及其內容的所有者 -R

```bash
chown -R user:group directory/
```

僅更改文件的所有者，不更改組

```bash
chown user file.txt
```

變更文件的所有者但不改變組

```bash
chown :group file.txt
```

使用數字表示的權限（例如，UID 和 GID）

```bash
chown 1000:1000 file.txt
```

```bash
# 新增x可執行權限
chmod +x officialsite/

# 有效與支援群組的觀察
groups
```