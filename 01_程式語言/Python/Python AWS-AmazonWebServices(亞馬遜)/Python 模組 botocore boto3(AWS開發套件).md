# Python 模組 botocore boto3(AWS開發套件)

## 目錄

- [Python 模組 botocore boto3(AWS開發套件)](#python-模組-botocore-boto3aws開發套件)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [boto3相關](#boto3相關)
		- [botocore相關](#botocore相關)
- [用法](#用法)
	- [S3範例](#s3範例)

## 參考資料

### boto3相關

[適用於 Python 的 AWS 開發套件 (Boto3)](https://aws.amazon.com/tw/sdk-for-python/)

[boto3 文檔](https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/index.html)

### botocore相關

[botocore 文檔](https://botocore.amazonaws.com/v1/documentation/api/latest/index.html)

# 用法

## S3範例

```Python
# AWS S3
import boto3
import botocore


class AmazonS3():
    def __init__(self, aws_access_key_id: str, aws_secret_access_key: str, region_name: str):
        """_summary_

        Args:
            aws_access_key_id (str): 需從AWS平台 取得 aws_access_key_id
            aws_secret_access_key (str): 需從AWS平台 取得 aws_secret_access_key
            region_name (str): 區域,ex: ap-northeast-1
        """
        self.s3 = boto3.resource(
            's3',
            aws_access_key_id=aws_access_key_id,
            aws_secret_access_key=aws_secret_access_key,
            region_name=region_name
        )

    def upload_file(self, file_path_in_local, file_path_in_S3, bucket='ngs-avdata-upload'):
        self.s3.Bucket(bucket).upload_file(
            file_path_in_local, file_path_in_S3)

    def upload_fileobj(self, file_obj, file_path_in_S3, bucket='ngs-avdata-upload'):
        self.s3.Bucket(bucket).upload_fileobj(
            file_obj, file_path_in_S3)

    def check_file_exists(self, file_path_in_s3, bucket='ngs-avdata-upload'):
        try:
            self.s3.Object(bucket, file_path_in_s3).load()
        except botocore.exceptions.ClientError as e:
            if e.response['Error']['Code'] == '404':
                print('file not found')
            else:
                print(
                    f"Something went wrong. Http error code is {e.response['Error']['Code']}")
            return False
        return True

    def copy(self, source_bucket, source_key, destination_bucket, destination_key):
        copy_source = {
            'Bucket': source_bucket,
            'Key': source_key
        }
        self.s3.meta.client.copy(
            copy_source, destination_bucket, destination_key)
```