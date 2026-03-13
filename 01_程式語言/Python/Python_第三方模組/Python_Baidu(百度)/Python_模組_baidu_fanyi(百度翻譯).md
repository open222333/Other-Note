# Python 模組 baidu fanyi(百度翻譯)

```
基於百度翻譯API在終端運行的英漢翻譯器
```

## 目錄

- [Python 模組 baidu fanyi(百度翻譯)](#python-模組-baidu-fanyi百度翻譯)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
	- [通用翻譯](#通用翻譯)
	- [偵測語系](#偵測語系)

## 參考資料

[baidu pypi](https://pypi.org/project/baidu/)

[通用翻译API接入文档](https://fanyi-api.baidu.com/doc/21)

[语种识别API接入文档](https://api.fanyi.baidu.com/doc/24)

# 指令

```bash
# 安裝
pip install baidu
```

# 用法

## 通用翻譯

```Python
from urllib.parse import urlencode


def baidu_translate(q, appid, secretKey, print_msg=False, source_code='auto', target_code='zh'):
    '''百度翻譯

    q: 翻譯目標
    appid: 百度翻譯平台提供
    secretKey: 百度翻譯平台提供
    print_msg: 是否印出訊息
    source_code: 來源字詞的語言 代碼
    target_code: 預計翻譯成的語言 代碼
    '''
    result = None
    salt = random.randint(32768, 65536)
    sign = appid+q+str(salt)+secretKey
    m1 = hashlib.md5()
    m1.update(sign.encode(encoding='utf-8'))
    sign = m1.hexdigest()
    params = {
        'appid':appid,
        'q':q,
        'from':source_code,
        'to':target_code,
        'salt':str(salt),
        'sign':sign
    }
    url = f'https://fanyi-api.baidu.com/api/trans/vip/translate?{urlencode(params)}'
    # print('url = '+url)
    http = urllib3.PoolManager()
    try:
        r = http.request('GET',url,timeout=3.0)
        if r.status==200:
            try:
                results = json.loads(r.data.decode("utf-8"))
                if 'error_code' not in results:
                    result = results['trans_result'][0]['dst']
                else:
                    print(results)
            except:
                traceback.print_exc()
    except:
        traceback.print_exc()
    if print_msg:
        print(f'翻譯目標：\n{q}\n')
        print(f'翻譯結果：\n{result}\n')
    return result
```

## 偵測語系

```Python

```