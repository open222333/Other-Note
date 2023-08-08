# Python 模組 flasgger(自動生成和顯示 Swagger API 文件)

```
Flasgger 是一個用於自動生成和顯示 Swagger API 文件的 Python 套件，可以幫助你更方便地記錄和測試你的 API
```

## 目錄

- [Python 模組 flasgger(自動生成和顯示 Swagger API 文件)](#python-模組-flasgger自動生成和顯示-swagger-api-文件)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)
	- [swag\_form用法](#swag_form用法)

## 參考資料

[flasgger pypi](https://pypi.org/project/flasgger/)

# 指令

```bash
# 安裝
pip install flasgger
```

# 用法

```Python
from flasgger import Swagger
app = Flask(__name__)
app.config['SWAGGER'] = {
    "title": "My API",
    "description": "My API",
    "version": "1.0.2",
    "termsOfService": "",
    "hide_top_bar": True
}
CORS(app)
Swagger(app)

@app.route('/v1/node', methods=['GET'])
def node_topo():
  """
    Get All Node List
    Retrieve node list
    ---
    tags:
      - Node APIs
    produces: application/json,
    parameters:
    - name: name
      in: path
      type: string
      required: true
    - name: node_id
      in: path
      type: string
      required: true
    responses:
      401:
        description: Unauthorized error
      200:
        description: Retrieve node list
        examples:
          node-list: [{"id":26},{"id":44}]
  """
  ret = jsonify(ret_list)
  return ret
```

## swag_form用法

```Python
from flask import Flask, request
from flasgger import Swagger, swag_from

app = Flask(__name__)
swagger = Swagger(app)

@app.route('/api/sum', methods=['POST'])
@swag_from('sum.yml')
def sum_numbers():
    a = float(request.form['a'])
    b = float(request.form['b'])
    result = a + b
    return {'result': result}

if __name__ == '__main__':
    app.run(debug=True)
```

```yml
# sum.yml
summary: Sum two numbers
description: This API endpoint calculates the sum of two numbers.
parameters:
  - name: a
    in: formData
    type: number
    required: true
  - name: b
    in: formData
    type: number
    required: true
responses:
  200:
    description: The sum of the two numbers
    schema:
      type: object
      properties:
        result:
          type: number
```