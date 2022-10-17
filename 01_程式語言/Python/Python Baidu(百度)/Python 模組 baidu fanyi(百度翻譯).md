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
	- [API](#api)

## 參考資料

[baidu pypi](https://pypi.org/project/baidu/)

[通用翻译API接入文档](https://fanyi-api.baidu.com/doc/21)

# 指令

```bash
# 安裝
pip install baidu
```

# 用法

## API

```Python
def translate(q , print_msg=False, source_code='auto', target_code='zh'):
	'''
	q: 翻譯目標
	'''
    appid = '11111111111111111'
    secretKey = 'esfsfesfsfsfsfsfsfsfsffsf'
    result = None
    salt = random.randint(32768, 65536)
    sign = f'{appid}{q}{str(salt)}{secretKey}'
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
    url = 'https://fanyi-api.baidu.com/api/trans/vip/translate?' + urllib.parse.urlencode(params)
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
