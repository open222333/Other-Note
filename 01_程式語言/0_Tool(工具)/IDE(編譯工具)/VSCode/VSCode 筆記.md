# VSCode 筆記

## 參考資料

[官方網站](https://code.visualstudio.com/docs)

## 快捷鍵

[Visual Studio Code shortcuts for Windows](http://code.visualstudio.com/shortcuts/keyboard-shortcuts-windows.pdf)

[Visual Studio Code shortcuts for MacOS](https://code.visualstudio.com/shortcuts/keyboard-shortcuts-macos.pdf)

```
在當前光標位置和鼠標點擊（或拖動）位置創建多行塊級選擇區域
shift + option + command + 上/下/左/右 或者shift + option + 單擊/按住鼠標左鍵拖動

添加光標
option + command + 上/下

點擊位置添加光標
option + click

選中全部與當前所選內容相同的文本
shift + command + L

選中下一個與當前所選內容相同的文本
command + D
```

[Visual Studio Code shortcuts for Linux](http://code.visualstudio.com/shortcuts/keyboard-shortcuts-linux.pdf)


# C/C++ 設定 開發工具 常用擴充套件

[C/C++](https://marketplace.visualstudio.com/items?itemName=ms-vscode.cpptools)

```
C/C++ 擴展為 Visual Studio Code 添加了對 C/C++ 的語言支持，包括 IntelliSense 和調試等功能。
```

[C++ Intellisense](https://marketplace.visualstudio.com/items?itemName=austin.code-gnu-global)

```
借助 Visual Studio Code 中的 GNU 全局工具，為 C/C++ 提供 Intellisense。
```

[Code Runner](https://marketplace.visualstudio.com/items?itemName=formulahendry.code-runner)


# Java 設定 開發工具 常用擴充套件

[Getting Started with Java in VS Code](https://code.visualstudio.com/docs/java/java-tutorial)

[下載器 (MacOS)](https://aka.ms/vscode-java-installer-mac)


# Python 設定 開發工具 常用擴充套件

指令 使用anaconda 環境
```bash
source /usr/local/anaconda3/bin/activate
```

[Python](https://marketplace.visualstudio.com/items?itemName=ms-python.python)

[Jupyter](https://marketplace.visualstudio.com/items?itemName=ms-toolsai.jupyter)

[Pylance](https://marketplace.visualstudio.com/items?itemName=ms-python.vscode-pylance)


# 使用者設定的檔案

[VSCode Default settings](https://code.visualstudio.com/docs/getstarted/settings#_default-settings)

使用者設定的檔案儲存在如下目錄：

```
Window
    %APPDATA%\Code\User\settings.json
Mac
    $HOME/Library/Application Support/Code/User/settings.json
Linux
    $HOME/.config/Code/User/settings.json
```

[隱藏檔案 不顯示](https://blog.crazyalu.com/2016/07/21/vscode-file-display/)

```
設定 -> files.exclude
```

[Visual Studio Code 修改tab空格大小，將預設的4格變2格撰寫網頁時更易閱讀](https://www.minwt.com/webdesign-dev/22930.html)

```
設定 -> tabsize
```


# 實用外掛 Code Runner

用途：
[VSCode插件推荐](https://zhuanlan.zhihu.com/p/54861567)

Setting -> Run Code configuration -> Run in Terminal

自定義編輯
Setting -> Executor Map -> setting.json

```
添加 code-runner.executorMap
{
    "code-runner.executorMap": {
        "java": "cd $dir && iavac $fileName && java $fileNameWithoutExt",
    }
}
```

# 實用外掛 Beautify

[官方網站 設定檔參數說明(右上角 搜尋)](https://unibeautify.com/)

[django-html 格式化 設定](https://github.com/vscode-django/vscode-django/issues/23)

用途：

```
在 Visual Studio Code 中美化javascript、JSON、CSS、Sass和HTML。
```

[VSCode 網頁](https://marketplace.visualstudio.com/items?itemName=HookyQR.beautify&ssr=false#overview)

[設定檔](https://github.com/HookyQR/VSCodeBeautify/blob/master/Settings.md)

套用設定方法：
.jsbeautifyrc

用戶或工作區設置中指定的文件路徑或配置對象，例如：

```js
"beautify.config" : "string|Object.<string,string|number|boolean>"
```

[Editorconfig 設置](http://editorconfig.org/)

[.jsbeautifyrc 範例](https://gist.github.com/wzup/fc3254562236c1ec3f69)

內容：

```js
{
    // 插件在與插件相同的目錄中查找 .jsbeautifyrc 文件
    // 你正在美化的源文件（或者上面的任何目錄，如果它不存在，
    // 或在您的主文件夾中，如果其他一切都失敗了）並使用這些選項
    //沿著默認的。

    // 詳情：https://github.com/victorporof/Sublime-HTMLPrettify#using-your-own-jsbeautifyrc-options
    // 文檔：https://github.com/einars/js-beautify/
    “html”：{
        “allowed_file_extensions”：[“htm”、“html”、“xhtml”、“shtml”、“xml”、“svg”、“dust”]、
        "brace_style": "collapse", // [collapse|expand|end-expand|none] 將大括號與控制語句放在同一行（默認），或將大括號放在單獨的行上（Allman / ANSI 樣式），或者直接放在自己的行上結束大括號，或嘗試將它們保持在原位
        "end_with_newline": true, // 以換行符結束輸出
        "indent_char": " ", // 縮進字符
        "indent_handlebars": true, // 例如 {{#foo}}, {{/foo}}
        "indent_inner_html": false, // 縮進 <head> 和 <body> 部分
        "indent_scripts": "keep", // [keep|separate|normal]
        "indent_size": 4, // 縮進大小
        "max_preserve_newlines": 10, // 一個塊中保留的最大換行數（0 禁用）
        "preserve_newlines": true, // 是否應該保留元素之前的現有換行符（僅適用於元素之前，不適用於標籤內或文本）
        “未格式化”：[“a”、“span”、“img”、“code”、“pre”、“sub”、“sup”、“em”、“strong”、“b”、“i”、“ u", "strike", "big", "small", "pre", "h1", "h2", "h3", "h4", "h5", "h6"], // 標籤列表不應重新格式化
        "wrap_line_length": 0 // 行應該在這個字符數之後的下一個機會換行（0 禁用）
    },
    “CSS”：{
        “allowed_file_extensions”：[“css”，“scss”，“sass”，“less”]，
        "end_with_newline": true, // 以換行符結束輸出
        "indent_char": " ", // 縮進字符
        "indent_size": 4, // 縮進大小
        "newline_between_rules": true, // 在每個 css 規則之後添加一個新行
        “selector_separator”：“”，
        "selector_separator_newline": true, // 是否使用換行符分隔選擇器（例如 "a,\nbr" 或 "a, br"）
        “preserve_newlines”：是的，
        “max_preserve_newlines”：10
    },
    “js”：{
        “allowed_file_extensions”：[“js”，“json”，“jshintrc”，“jsbeautifyrc”]，
        "brace_style": "collapse", // [collapse|expand|end-expand|none] 將大括號與控制語句放在同一行（默認），或將大括號放在單獨的行上（Allman / ANSI 樣式），或者直接放在自己的行上結束大括號，或嘗試將它們保持在原位
        "break_chained_methods": false, // 跨後續行中斷鍊接的方法調用
        "e4x": false, // 通過 untouched 傳遞 E4X xml 文字
        "end_with_newline": false, // 以換行符結束輸出
        "indent_char": " ", // 縮進字符
        "indent_level": 0, // 初始縮進級別
        "indent_size": 4, // 縮進大小
        "indent_with_tabs": false, // 使用製表符縮進，覆蓋 `indent_size` 和 `indent_char`
        "jslint_happy": false, // 如果為 true，則強制執行 jslint-stricter 模式
        "keep_array_indentation": false, // 保留數組縮進
        "keep_function_indentation": false, // 保留函數縮進
        "max_preserve_newlines": 0, // 一個塊中保留的最大換行數（0 禁用）
        "preserve_newlines": true, // 是否保留現有的換行符
        "space_after_anon_function": false, // 是否應該在匿名函數的括號前加上空格，"function()" vs "function()"
        "space_before_conditional": true, // 條件語句前是否要加空格，"if(true)" vs "if(true)"
        "space_in_empty_paren": false, // 在空括號內添加填充空格，"f()" vs "f( )"
        "space_in_paren": false, // 在括號內添加填充空格，即。f(a, b)
        "unescape_strings": false, // 以 \xNN 表示法編碼的字符串中的可打印字符是否應該不轉義，"example" vs "\x65\x78\x61\x6d\x70\x6c\x65"
        "wrap_line_length": 0 // 行應該在這個字符數之後的下一個機會換行（0 禁用）
    }
}
```

無註解版本:

```js
{
    "html": {
        "allowed_file_extensions": ["htm", "html", "xhtml", "shtml", "xml", "svg", "dust"],
        "brace_style": "collapse",
        "end_with_newline": true,
        "indent_char": " ",
        "indent_handlebars": true,
        "indent_inner_html": false,
        "indent_scripts": "keep",
        "indent_size": 4,
        "max_preserve_newlines": 10,
        "preserve_newlines": true,
        "unformatted": ["a", "span", "img", "code", "pre", "sub", "sup", "em", "strong", "b", "i", "u", "strike", "big", "small", "pre", "h1",
            "h2", "h3", "h4", "h5", "h6"
        ],
        "wrap_line_length": 0
    },
    "css": {
        "allowed_file_extensions": ["css", "scss", "sass", "less"],
        "end_with_newline": true,
        "indent_char": " ",
        "indent_size": 4,
        "newline_between_rules": true,
        "selector_separator": " ",
        "selector_separator_newline": true,
        "preserve_newlines": true,
        "max_preserve_newlines": 10
    },
    "js": {
        "allowed_file_extensions": ["js", "json", "jshintrc", "jsbeautifyrc"],
        "brace_style": "collapse",
        "break_chained_methods": false,
        "e4x": false,
        "end_with_newline": false,
        "indent_char": " ",
        "indent_level": 0,
        "indent_size": 4,
        "indent_with_tabs": false,
        "jslint_happy": false,
        "keep_array_indentation": false,
        "keep_function_indentation": false,
        "max_preserve_newlines": 0,
        "preserve_newlines": true,
        "space_after_anon_function": false,
        "space_before_conditional": true,
        "space_in_empty_paren": false,
        "space_in_paren": false,
        "unescape_strings": false,
        "wrap_line_length": 0
    }
}
```

# 設定檔 備份 setting.json

[vsintellicode.modify.editor.suggestSelection](https://github.com/MicrosoftDocs/intellicode/issues/78)

"vsintellicode.modify.editor.suggestSelection": "automaticallyOverrodeDefaultValue",

## 20220210
```json
{
    // 編輯區 設定
    "editor.tabSize": 4,
    "editor.fontSize": 16,
    "editor.insertSpaces": true,
    "editor.detectIndentation": true,
    "editor.suggestSelection": "first",
    "editor.wordWrap": "on",
    "diffEditor.wordWrap": "on",
    "notebook.cellToolbarLocation": {
        "default": "right",
        "jupyter-notebook": "left"
    },
    "workbench.editorAssociations": {
        "*.ipynb": "jupyter-notebook"
    },
    // 終端機設定
    "terminal.integrated.inheritEnv": false,
    "terminal.integrated.fontSize": 16,
    "terminal.integrated.defaultProfile.osx": "bash",
    // 除錯設定
    "debug.console.fontSize": 16,
    // 檔案異動確認
    "explorer.confirmDragAndDrop": false,
    "explorer.confirmDelete": false,
    // 自動儲存
    "files.autoSave": "afterDelay",
    // Python
    "python.condaPath": "anaconda3",
    "python.formatting.autopep8Args": [
        "--max-line-length=180"
    ],
    // Code Runner 設定
    "code-runner.runInTerminal": true,
    // 不顯示檔案
    "files.exclude": {
        ".tmp.drivedownload": true,
        ".tmp.driveupload": true,
        ".vscode": true,
        "**/.classpath": true,
        "**/.project": true,
        "**/.settings": true,
        "**/.factorypath": true
    },
    // https://github.com/MicrosoftDocs/intellicode/issues/78
    "vsintellicode.modify.editor.suggestSelection": "automaticallyOverrodeDefaultValue",
    "jupyter.askForKernelRestart": false,
    // HookyQR.beautify 設定
    "beautify.config": {
        "html": {
            "allowed_file_extensions": [
                "htm",
                "html",
                "xhtml",
                "shtml",
                "xml",
                "svg",
                "dust"
            ],
            "brace_style": "collapse,preserve-inline",
            "end_with_newline": true,
            "indent_char": " ",
            "indent_handlebars": true,
            "indent_inner_html": false,
            "indent_scripts": "normal",
            "indent_size": 2,
            "max_preserve_newlines": 10,
            "preserve_newlines": true,
            "unformatted": [
                "a",
                "span",
                "img",
                "code",
                "pre",
                "sub",
                "sup",
                "em",
                "strong",
                "b",
                "i",
                "u",
                "strike",
                "big",
                "small",
                "pre",
                "h1",
                "h2",
                "h3",
                "h4",
                "h5",
                "h6"
            ],
            "wrap_line_length": 0
        },
        "css": {
            "allowed_file_extensions": [
                "css",
                "scss",
                "sass",
                "less"
            ],
            "end_with_newline": true,
            "indent_char": " ",
            "indent_size": 4,
            "newline_between_rules": true,
            "selector_separator": " ",
            "selector_separator_newline": true,
            "preserve_newlines": true,
            "max_preserve_newlines": 10
        },
        "js": {
            "allowed_file_extensions": [
                "js",
                "json",
                "jshintrc",
                "jsbeautifyrc"
            ],
            "brace_style": "collapse",
            "break_chained_methods": false,
            "e4x": false,
            "end_with_newline": false,
            "indent_char": " ",
            "indent_level": 0,
            "indent_size": 2,
            "indent_with_tabs": false,
            "jslint_happy": false,
            "keep_array_indentation": false,
            "keep_function_indentation": false,
            "max_preserve_newlines": 0,
            "preserve_newlines": true,
            "space_after_anon_function": false,
            "space_before_conditional": true,
            "space_in_empty_paren": false,
            "space_in_paren": false,
            "unescape_strings": false,
            "wrap_line_length": 0
        }
    },
    "beautify.language": {
        "js": {
            "type": [
                "javascript",
                "json"
            ],
            "filename": [
                ".jshintrc",
                ".jsbeautifyrc"
            ]
        },
        "css": [
            "css",
            "scss"
        ],
        "html": [
            "htm",
            "html",
            "django-html"
        ]
    },
    "files.associations": {
        "**/*.html": "html",
        "**/templates/*/*.html": "django-html",
        "**/templates/*": "django-txt",
        "**/templates/*.html": "django-html",
        "**/requirements{/**,*}.{txt,in}": "pip-requirements"
    },
    // 各個檔案設定
    "[html]": {
        "editor.defaultFormatter": "HookyQR.beautify"
    },
    "[django-html]": {
        "editor.tabSize": 2,
    },
    "emmet.includeLanguages": {
        "django-html": "html"
    },
    "[javascript]": {
        "editor.defaultFormatter": "HookyQR.beautify"
    },
}
```

## 20220211
```json
{
    // 編輯區 設定
    "editor.tabSize": 4,
    "editor.fontSize": 16,
    "editor.insertSpaces": false,
    "editor.detectIndentation": true,
    "editor.suggestSelection": "first",
    "editor.wordWrap": "on",
    "diffEditor.wordWrap": "on",
    "notebook.cellToolbarLocation": {
        "default": "right",
        "jupyter-notebook": "left"
    },
    "workbench.editorAssociations": {
        "*.ipynb": "jupyter-notebook"
    },
    // 終端機設定
    "terminal.integrated.inheritEnv": false,
    "terminal.integrated.fontSize": 16,
    "terminal.integrated.defaultProfile.osx": "bash",
    // 除錯設定
    "debug.console.fontSize": 16,
    // 檔案異動確認
    "explorer.confirmDragAndDrop": false,
    "explorer.confirmDelete": false,
    // 自動儲存
    "files.autoSave": "afterDelay",
    // Python
    "python.condaPath": "anaconda3",
    "python.formatting.autopep8Args": [
        "--max-line-length=180"
    ],
    // Code Runner 設定
    "code-runner.runInTerminal": true,
    // 不顯示檔案
    "files.exclude": {
        ".tmp.drivedownload": true,
        ".tmp.driveupload": true,
        ".vscode": true,
        "**/.classpath": true,
        "**/.project": true,
        "**/.settings": true,
        "**/.factorypath": true
    },
    // https://github.com/MicrosoftDocs/intellicode/issues/78
    "vsintellicode.modify.editor.suggestSelection": "automaticallyOverrodeDefaultValue",
    "jupyter.askForKernelRestart": false,
    // HookyQR.beautify 設定
    "beautify.config": {
        "html": {
            "allowed_file_extensions": [
                "htm",
                "html",
                "xhtml",
                "shtml",
                "xml",
                "svg",
                "dust"
            ],
            "brace_style": "collapse,preserve-inline",
            "end_with_newline": true,
            "indent_char": " ",
            "indent_handlebars": true,
            "indent_inner_html": false,
            "indent_scripts": "normal",
            "indent_size": 2,
            "max_preserve_newlines": 10,
            "preserve_newlines": true,
            "unformatted": [
                "a",
                "span",
                "img",
                "code",
                "pre",
                "sub",
                "sup",
                "em",
                "strong",
                "b",
                "i",
                "u",
                "strike",
                "big",
                "small",
                "pre",
                "h1",
                "h2",
                "h3",
                "h4",
                "h5",
                "h6"
            ],
            "wrap_line_length": 0
        },
        "css": {
            "allowed_file_extensions": [
                "css",
                "scss",
                "sass",
                "less"
            ],
            "end_with_newline": true,
            "indent_char": " ",
            "indent_size": 4,
            "newline_between_rules": true,
            "selector_separator": " ",
            "selector_separator_newline": true,
            "preserve_newlines": true,
            "max_preserve_newlines": 10
        },
        "js": {
            "allowed_file_extensions": [
                "js",
                "json",
                "jshintrc",
                "jsbeautifyrc"
            ],
            "brace_style": "end-expand",
            "break_chained_methods": false,
            "e4x": false,
            "end_with_newline": false,
            "indent_level": 0,
            "indent_size": 2,
            "indent_with_tabs": true,
            "jslint_happy": false,
            "keep_array_indentation": false,
            "keep_function_indentation": true,
            "max_preserve_newlines": 0,
            "preserve_newlines": true,
            "space_after_anon_function": true,
            "space_before_conditional": true,
            "space_in_empty_paren": false,
            "space_in_paren": false,
            "unescape_strings": false,
            "wrap_line_length": 0
        },
    },
    "beautify.language": {
        "js": {
            "type": [
                "javascript",
                "json"
            ],
            "filename": [
                ".jshintrc",
                ".jsbeautifyrc"
            ]
        },
        "css": [
            "css",
            "scss"
        ],
        "html": [
            "htm",
            "html",
            "django-html"
        ]
    },
    "files.associations": {
        "**/*.html": "html",
        "**/templates/*/*.html": "django-html",
        "**/templates/*": "django-txt",
        "**/templates/*.html": "django-html",
        "**/requirements{/**,*}.{txt,in}": "pip-requirements"
    },
    // 各個檔案設定
    "[html]": {
        "editor.defaultFormatter": "HookyQR.beautify"
    },
    "[django-html]": {
        "editor.tabSize": 2,
    },
    "emmet.includeLanguages": {
        "django-html": "html"
    },
    "[javascript]": {
        "editor.defaultFormatter": "HookyQR.beautify"
    },
}
```

## 20220314
```json
{
    // 編輯區 設定
    // "editor.tabSize": 4,
    "editor.fontSize": 16,
    "editor.insertSpaces": false,
    "editor.detectIndentation": false,
    "editor.suggestSelection": "first",
    "editor.wordWrap": "on",
    "editor.foldingImportsByDefault": true,
    "editor.showFoldingControls": "always",
	// 去除行尾空白
	"files.trimTrailingWhitespace":true,

    "diffEditor.wordWrap": "on",

    "notebook.showFoldingControls": "always",
    "notebook.cellToolbarLocation": {
        "default": "right",
        "jupyter-notebook": "left"
    },
    "workbench.editorAssociations": {
        "*.ipynb": "jupyter-notebook"
    },
    // 工作區
    "terminal.integrated.inheritEnv": false,
    "terminal.integrated.fontSize": 16,
    "terminal.integrated.defaultProfile.osx": "bash",
    // 除錯設定
    "debug.console.fontSize": 16,
    // 檔案異動確認
    "explorer.confirmDragAndDrop": false,
    "explorer.confirmDelete": false,
    // 自動儲存
    "files.autoSave": "afterDelay",
    // Python
    "python.condaPath": "anaconda3",
    "python.diagnostics.sourceMapsEnabled": true,
    "python.terminal.activateEnvironment": true,
    "python.defaultInterpreterPath": "/usr/local/anaconda3/bin/",
    "python.formatting.autopep8Args": [
        "--max-line-length=180"
    ],
    // Code Runner 設定
    "code-runner.runInTerminal": true,
    // 不顯示檔案
    "files.exclude": {
        ".tmp.drivedownload": true,
        ".tmp.driveupload": true,
        ".vscode": true,
        "**/.classpath": true,
        "**/.project": true,
        "**/.settings": true,
        "**/.factorypath": true
    },
    // https://github.com/MicrosoftDocs/intellicode/issues/78
    "vsintellicode.modify.editor.suggestSelection": "automaticallyOverrodeDefaultValue",
    "jupyter.askForKernelRestart": false,
    // HookyQR.beautify 設定
    "beautify.config": {
        "html": {
            "allowed_file_extensions": [
                "htm",
                "html",
                "xhtml",
                "shtml",
                "xml",
                "svg",
                "dust"
            ],
            "brace_style": "collapse,preserve-inline",
            "end_with_newline": true,
            "indent_char": " ",
            "indent_handlebars": true,
            "indent_inner_html": false,
            "indent_scripts": "normal",
            "indent_size": 2,
            "max_preserve_newlines": 10,
            "preserve_newlines": true,
            "unformatted": [
                "a",
                "span",
                "img",
                "code",
                "pre",
                "sub",
                "sup",
                "em",
                "strong",
                "b",
                "i",
                "u",
                "strike",
                "big",
                "small",
                "pre",
                "h1",
                "h2",
                "h3",
                "h4",
                "h5",
                "h6"
            ],
            "wrap_line_length": 0
        },
        "css": {
            "allowed_file_extensions": [
                "css",
                "scss",
                "sass",
                "less"
            ],
            "end_with_newline": true,
            "indent_char": " ",
            "indent_size": 2,
            "newline_between_rules": true,
            "selector_separator": " ",
            "selector_separator_newline": true,
            "preserve_newlines": true,
            "max_preserve_newlines": 10
        },
        "js": {
            "allowed_file_extensions": [
                "js",
                "json",
                "jshintrc",
                "jsbeautifyrc"
            ],
            "brace_style": "end-expand",
            "break_chained_methods": false,
            "e4x": false,
            "end_with_newline": false,
            "indent_level": 0,
            "indent_size": 2,
            "jslint_happy": false,
            "keep_array_indentation": false,
            "keep_function_indentation": false,
            "max_preserve_newlines": 0,
            "preserve_newlines": true,
            "space_after_anon_function": true,
            "space_before_conditional": true,
            "space_in_empty_paren": false,
            "space_in_paren": false,
            "unescape_strings": false,
            "wrap_line_length": 0
        },
    },
    "beautify.language": {
        "js": {
            "type": [
                "javascript",
                "json"
            ],
            "filename": [
                ".jshintrc",
                ".jsbeautifyrc"
            ]
        },
        "css": [
            "css",
            "scss"
        ],
        "html": [
            "htm",
            "html",
            "django-html"
        ]
    },
    "files.associations": {
        "**/*.html": "html",
        "**/templates/*/*.html": "django-html",
        "**/templates/*": "django-txt",
        "**/templates/*.html": "django-html",
        "**/requirements{/**,*}.{txt,in}": "pip-requirements",
        "*.yml": "dockercompose"
    },
    "emmet.includeLanguages": {
        "django-html": "html"
    },

    // 各個檔案設定
    "[html]": {
        "editor.defaultFormatter": "HookyQR.beautify"
    },
    "[django-html]": {
        "editor.tabSize": 2,
    },
    "[javascript]": {
		"editor.tabSize": 2,
        "editor.defaultFormatter": "HookyQR.beautify",
    },
    "[python]": {
        "editor.insertSpaces": true,
    },
    "security.workspace.trust.untrustedFiles": "open",
    "[json]": {
        "editor.defaultFormatter": "HookyQR.beautify"
    },
    "[markdown]": {
        "editor.tabSize": 4,
	}
}
```

## 20220325
```json
{
    // 編輯區 設定
    // "editor.tabSize": 4,
    "editor.fontSize": 16,
    "editor.insertSpaces": false,
    "editor.detectIndentation": false,
    "editor.suggestSelection": "first",
    "editor.wordWrap": "on",
    "editor.foldingImportsByDefault": true,
    "editor.showFoldingControls": "always",
	// 去除行尾空白
	"files.trimTrailingWhitespace":true,

	// 安全性
	"security.workspace.trust.untrustedFiles": "open",

    "diffEditor.wordWrap": "on",

    "notebook.showFoldingControls": "always",
    "notebook.cellToolbarLocation": {
        "default": "right",
        "jupyter-notebook": "left"
    },
    "workbench.editorAssociations": {
        "*.ipynb": "jupyter-notebook"
    },
    // 工作區
	"terminal.integrated.enableMultiLinePasteWarning": false,
    "terminal.integrated.inheritEnv": false,
    "terminal.integrated.fontSize": 16,
    "terminal.integrated.defaultProfile.osx": "bash",
    // 除錯設定
    "debug.console.fontSize": 16,
    // 檔案異動確認
    "explorer.confirmDragAndDrop": false,
    "explorer.confirmDelete": false,
    // 自動儲存
    "files.autoSave": "afterDelay",
    // Python
    "python.condaPath": "anaconda3",
    "python.diagnostics.sourceMapsEnabled": true,
    "python.terminal.activateEnvironment": true,
    "python.defaultInterpreterPath": "/usr/local/anaconda3/bin/",
    "python.formatting.autopep8Args": [
        "--max-line-length=180"
    ],
    // Code Runner 設定
    "code-runner.runInTerminal": true,
    // 不顯示檔案
    "files.exclude": {
        ".tmp.drivedownload": true,
        ".tmp.driveupload": true,
        ".vscode": true,
        "**/.classpath": true,
        "**/.project": true,
        "**/.settings": true,
        "**/.factorypath": true
    },
    // https://github.com/MicrosoftDocs/intellicode/issues/78
    "vsintellicode.modify.editor.suggestSelection": "automaticallyOverrodeDefaultValue",
    "jupyter.askForKernelRestart": false,
    // HookyQR.beautify 設定
    "beautify.config": {
        "html": {
            "allowed_file_extensions": [
                "htm",
                "html",
                "xhtml",
                "shtml",
                "xml",
                "svg",
                "dust"
            ],
            "brace_style": "collapse,preserve-inline",
            "end_with_newline": true,
            "indent_char": " ",
            "indent_handlebars": true,
            "indent_inner_html": false,
            "indent_scripts": "normal",
            "indent_size": 2,
            "max_preserve_newlines": 10,
            "preserve_newlines": true,
            "unformatted": [
                "a",
                "span",
                "img",
                "code",
                "pre",
                "sub",
                "sup",
                "em",
                "strong",
                "b",
                "i",
                "u",
                "strike",
                "big",
                "small",
                "pre",
                "h1",
                "h2",
                "h3",
                "h4",
                "h5",
                "h6"
            ],
            "wrap_line_length": 0
        },
        "css": {
            "allowed_file_extensions": [
                "css",
                "scss",
                "sass",
                "less"
            ],
            "end_with_newline": true,
            "indent_char": " ",
            "indent_size": 2,
            "newline_between_rules": true,
            "selector_separator": " ",
            "selector_separator_newline": true,
            "preserve_newlines": true,
            "max_preserve_newlines": 10
        },
        "js": {
            "allowed_file_extensions": [
                "js",
                "json",
                "jshintrc",
                "jsbeautifyrc"
            ],
            "brace_style": "end-expand",
            "break_chained_methods": false,
            "e4x": false,
            "end_with_newline": false,
            "indent_level": 0,
            "indent_size": 2,
            "jslint_happy": false,
            "keep_array_indentation": false,
            "keep_function_indentation": false,
            "max_preserve_newlines": 0,
            "preserve_newlines": true,
            "space_after_anon_function": true,
            "space_before_conditional": true,
            "space_in_empty_paren": false,
            "space_in_paren": false,
            "unescape_strings": false,
            "wrap_line_length": 0
        },
    },
    "beautify.language": {
        "js": {
            "type": [
                "javascript",
                "json"
            ],
            "filename": [
                ".jshintrc",
                ".jsbeautifyrc"
            ]
        },
        "css": [
            "css",
            "scss"
        ],
        "html": [
            "htm",
            "html",
            "django-html"
        ]
    },
    "files.associations": {
        "**/*.html": "html",
        "**/templates/*/*.html": "django-html",
        "**/templates/*": "django-txt",
        "**/templates/*.html": "django-html",
        "**/requirements{/**,*}.{txt,in}": "pip-requirements",
        "*.yml": "dockercompose"
    },
    "emmet.includeLanguages": {
        "django-html": "html"
    },

    // 各個檔案設定
    "[html]": {
        "editor.defaultFormatter": "HookyQR.beautify"
    },
    "[django-html]": {
        "editor.tabSize": 2,
    },
    "[javascript]": {
		"editor.tabSize": 2,
        "editor.defaultFormatter": "HookyQR.beautify",
    },
    "[python]": {
        "editor.insertSpaces": true,
    },
    "[json]": {
		"editor.tabSize": 2,
        "editor.defaultFormatter": "HookyQR.beautify"
    },
    "[markdown]": {
        "editor.tabSize": 4,
	},
}
```

## 20220408
```json
{
    // 編輯區 設定
    // "editor.tabSize": 4,
    "editor.fontSize": 16,
    "editor.insertSpaces": false,
    "editor.detectIndentation": false,
    "editor.suggestSelection": "first",
    "editor.wordWrap": "on",
    "editor.foldingImportsByDefault": true,
    "editor.showFoldingControls": "always",
	// 去除行尾空白
	"files.trimTrailingWhitespace":true,

	// 安全性
	"security.workspace.trust.untrustedFiles": "open",

    "diffEditor.wordWrap": "on",

    "notebook.showFoldingControls": "always",
    "notebook.cellToolbarLocation": {
        "default": "right",
        "jupyter-notebook": "left"
    },
    "workbench.editorAssociations": {
        "*.ipynb": "jupyter-notebook"
    },
    // 工作區
	"terminal.integrated.enableMultiLinePasteWarning": false,
    "terminal.integrated.inheritEnv": false,
    "terminal.integrated.fontSize": 16,
    "terminal.integrated.defaultProfile.osx": "bash",
    // 除錯設定
    "debug.console.fontSize": 16,
    // 檔案異動確認
    "explorer.confirmDragAndDrop": false,
    "explorer.confirmDelete": false,
    // 自動儲存
    "files.autoSave": "afterDelay",
    // Python
    "python.condaPath": "anaconda3",
    "python.diagnostics.sourceMapsEnabled": true,
    "python.terminal.activateEnvironment": true,
    "python.defaultInterpreterPath": "/usr/local/anaconda3/bin/",
    "python.formatting.autopep8Args": [
        "--max-line-length=180"
    ],
    // Code Runner 設定
    "code-runner.runInTerminal": true,
    // 不顯示檔案
    "files.exclude": {
        ".tmp.drivedownload": true,
        ".tmp.driveupload": true,
        ".vscode": true,
        "**/.classpath": true,
        "**/.project": true,
        "**/.settings": true,
        "**/.factorypath": true
    },
    // https://github.com/MicrosoftDocs/intellicode/issues/78
    "vsintellicode.modify.editor.suggestSelection": "automaticallyOverrodeDefaultValue",
    "jupyter.askForKernelRestart": false,
    // HookyQR.beautify 設定
    "beautify.config": {
        "html": {
            "allowed_file_extensions": [
                "htm",
                "html",
                "xhtml",
                "shtml",
                "xml",
                "svg",
                "dust"
            ],
            "brace_style": "collapse,preserve-inline",
            "end_with_newline": true,
            "indent_char": " ",
            "indent_handlebars": true,
            "indent_inner_html": false,
            "indent_scripts": "normal",
            "indent_size": 2,
            "max_preserve_newlines": 10,
            "preserve_newlines": true,
            "unformatted": [
                "a",
                "span",
                "img",
                "code",
                "pre",
                "sub",
                "sup",
                "em",
                "strong",
                "b",
                "i",
                "u",
                "strike",
                "big",
                "small",
                "pre",
                "h1",
                "h2",
                "h3",
                "h4",
                "h5",
                "h6"
            ],
            "wrap_line_length": 0
        },
        "css": {
            "allowed_file_extensions": [
                "css",
                "scss",
                "sass",
                "less"
            ],
            "end_with_newline": true,
            "indent_char": " ",
            "indent_size": 2,
            "newline_between_rules": true,
            "selector_separator": " ",
            "selector_separator_newline": true,
            "preserve_newlines": true,
            "max_preserve_newlines": 10
        },
        "js": {
            "allowed_file_extensions": [
                "js",
                "json",
                "jshintrc",
                "jsbeautifyrc"
            ],
            "brace_style": "end-expand",
            "break_chained_methods": false,
            "e4x": false,
            "end_with_newline": false,
            "indent_level": 0,
            "indent_size": 2,
            "jslint_happy": false,
            "keep_array_indentation": false,
            "keep_function_indentation": false,
            "max_preserve_newlines": 0,
            "preserve_newlines": true,
            "space_after_anon_function": true,
            "space_before_conditional": true,
            "space_in_empty_paren": false,
            "space_in_paren": false,
            "unescape_strings": false,
            "wrap_line_length": 0
        },
    },
    "beautify.language": {
        "js": {
            "type": [
                "javascript",
                "json"
            ],
            "filename": [
                ".jshintrc",
                ".jsbeautifyrc"
            ]
        },
        "css": [
            "css",
            "scss"
        ],
        "html": [
            "htm",
            "html",
            "django-html"
        ]
    },
    "files.associations": {
        "**/*.html": "html",
        "**/templates/*/*.html": "django-html",
        "**/templates/*": "django-txt",
        "**/templates/*.html": "django-html",
        "**/requirements{/**,*}.{txt,in}": "pip-requirements",
        "*.yml": "dockercompose"
    },
    "emmet.includeLanguages": {
        "django-html": "html"
    },

    // 各個檔案設定
    "[html]": {
        "editor.defaultFormatter": "HookyQR.beautify"
    },
    "[django-html]": {
        "editor.tabSize": 2,
		"editor.insertSpaces": true,
    },
    "[javascript]": {
		"editor.tabSize": 2,
		// 使用tab鍵 根據tabSize加入空白
		"editor.insertSpaces": true,
        "editor.defaultFormatter": "HookyQR.beautify",
    },
    "[python]": {
        "editor.insertSpaces": true,
    },
    "[json]": {
		"editor.tabSize": 2,
		"editor.insertSpaces": true,
        "editor.defaultFormatter": "HookyQR.beautify"
    },
    "[markdown]": {
        "editor.tabSize": 4,
	},
}
```