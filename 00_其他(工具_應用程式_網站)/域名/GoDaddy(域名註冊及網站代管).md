# GoDaddy(域名註冊及網站代管)

```
```

## 目錄

- [GoDaddy(域名註冊及網站代管)](#godaddy域名註冊及網站代管)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [範例](#範例)
  - [Python](#python)

## 參考資料

[The GoDaddy API](https://developer.godaddy.com/)

# 範例

## Python

```ini
[GODADDY]
API_KEY=key
API_SECRET=sercret
```

```Python
import os
import requests
from configparser import ConfigParser


conf = ConfigParser()
conf.read(f'{os.path.dirname(__file__)}/config.ini', encoding='utf-8')


def get_domain_info(domain, shopper_id, is_ote=False):
    '''
    is_ote: 使用ote環境的api
    '''

    API_KEY = conf.get('GODADDY', 'API_KEY')
    API_SECRET = conf.get('GODADDY', 'API_SECRET')

    if is_ote:
        # ote 環境
        api_url = 'api.ote-godaddy.com'
    else:
        # product 環境
        api_url = 'api.godaddy.com'

    header_content = {
        'X-Shopper-Id': f'{shopper_id}',
        'Authorization': f'sso-key {API_KEY}:{API_SECRET}',
        'accept': 'application/json'
    }

    result = requests.get(
        f'https://{api_url}/v1/domains/{domain}',
        headers=header_content
    ).json()

    return result
```