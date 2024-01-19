# Python 模組 PyYAML(解析和生成 YAML)

```
PyYAML 是一個 Python 库，用於解析和生成 YAML（YAML Ain't Markup Language）格式的數據。

YAML 是一種人類可讀且容易寫的數據序列化格式，通常用於配置文件、數據交換等場景。
```

## 目錄

- [Python 模組 PyYAML(解析和生成 YAML)](#python-模組-pyyaml解析和生成-yaml)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [資安相關](#資安相關)
- [指令](#指令)
- [用法](#用法)
	- [yaml.load vs. yaml.safe\_load(資安)](#yamlload-vs-yamlsafe_load資安)

## 參考資料

[PyYAML pypi](https://pypi.org/project/PyYAML/)

### 資安相關

[資安宣導 — 為什麼使用 PyYaml 解析 YAML 時要使用 safe_load 才對](https://www.myapollo.com.tw/blog/python-pyyaml-safe-load/?fbclid=IwAR3GOQVfWzR2YA-fQ-hV4XXDXgEQUVrPhUnt1d3eRHCo5J9T9Ni8XMKS0TU)

[PyYAML Documentation](https://pyyaml.org/wiki/PyYAMLDocumentation)

# 指令

```bash
# 安裝
pip install PyYAML
```

# 用法

`將 Python 數據轉換為 YAML`

```Python
import yaml

data = {
    'name': 'John Doe',
    'age': 30,
    'city': 'New York'
}

# 將 Python 數據轉換為 YAML 字符串
yaml_str = yaml.dump(data, default_flow_style=False)

print(yaml_str)
```

`將 YAML 轉換為 Python 數據`

```Python
import yaml

yaml_str = """
name: John Doe
age: 30
city: New York
"""

# 將 YAML 字符串轉換為 Python 數據
data = yaml.load(yaml_str, Loader=yaml.FullLoader)

print(data)
```

`讀取 YAML 文件`

```Python
import yaml

# 讀取 YAML 文件
with open('example.yaml', 'r') as file:
    data = yaml.load(file, Loader=yaml.FullLoader)

print(data)
```

## yaml.load vs. yaml.safe_load(資安)

yaml_data 中含有 !!python/object/apply:eval 的字串，就能夠讓 PyYMAL 在解析字串時，順便執行 Python 程式碼列印出 Hello 字串。

```Python
# pip install PyYAML==6.0.1
import yaml
from yaml import Loader

yaml_data = """
!!python/object/apply:eval
  args: ['print("Hello")']
"""

try:
    parsed_data = yaml.load(yaml_data, Loader)
except yaml.YAMLError as e:
    print(f"Error: {e}")
```

而這種攻擊手法之所以能夠成功，是因為 PyYAML 支援將 Python objects 在 dump 時轉為特製的 YAML tags,

例如 !!python/object , !!python/bytes 等等，因此也支援在 load 時轉回 Python objects, 這也造成前述攻擊手法成為 1 種可能。

所以在讀取不受信任來源的 YAML 檔時，請使用 safe_load() , 該方法只會讀取 YAML 標準定義的 tags, 就能夠防範前述攻擊手法，例如：

```Python
import yaml

yaml_data = """
!!python/object/apply:eval
  args: ['print("Hello")']
"""

try:
    parsed_data = yaml.safe_load(yaml_data)
except yaml.YAMLError as e:
    print(f"Error: {e}")
```
