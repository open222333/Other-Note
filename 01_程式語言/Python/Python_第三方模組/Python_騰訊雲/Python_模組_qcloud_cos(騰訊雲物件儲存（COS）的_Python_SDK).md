# Python 模組 qcloud_cos(騰訊雲物件儲存（COS）的 Python SDK)

```
qcloud_cos 是騰訊雲物件儲存（COS）的 Python SDK，用於在 Python 中存取和操作騰訊雲 COS 服務
```

## 目錄

- [Python 模組 qcloud\_cos(騰訊雲物件儲存（COS）的 Python SDK)](#python-模組-qcloud_cos騰訊雲物件儲存cos的-python-sdk)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[qcloud_cos pypi](https://pypi.org/project/qcloud_cos/)

# 指令

```bash
# 安裝
pip install qcloud_cos
```

# 用法

```Python
from qcloud_cos import CosConfig
from qcloud_cos import CosS3Client

# 配置
secret_id = 'your_secret_id'
secret_key = 'your_secret_key'
region = 'your_region'
token = None  # 如果使用临时密钥，需要设置 token
cos_config = CosConfig(
    Secret_id=secret_id,
    Secret_key=secret_key,
    Region=region,
    Token=token
)

# 透過 cos_client 物件可以進行各種 COS 操作，例如上傳檔案、下載檔案、列舉文件
cos_client = CosS3Client(cos_config)
response = cos_client.upload_file(
    Bucket='your_bucket_name',
    LocalFilePath='local_file_path',
    Key='remote_file_key'
)
```
