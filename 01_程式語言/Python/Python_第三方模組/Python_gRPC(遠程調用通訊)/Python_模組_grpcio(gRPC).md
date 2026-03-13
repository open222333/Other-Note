# Python 模組 grpcio(gRPC)

```
grpcio 是 Python 中用於實現 gRPC（gRPC Remote Procedure Call）的庫。
gRPC 是一種高性能、開源的遠程過程調用（RPC）框架，由Google開發並開源，它允許不同的應用程序在不同的計算機之間進行通信，就像調用本地函數一樣。
```

## 目錄

- [Python 模組 grpcio(gRPC)](#python-模組-grpciogrpc)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
		- [工具相關](#工具相關)
		- [教學相關](#教學相關)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[官方網站](https://grpc.io/)

[grpcio pypi](https://pypi.org/project/grpcio/)

[grpc github](https://github.com/grpc/grpc)

### 工具相關

[gRPC 測試工具](https://william-yeh.net/post/2020/04/grpc-testing-tools/)

### 教學相關

[Python實作gRPC通訊實作](https://wenwender.wordpress.com/2022/03/26/python%E5%AF%A6%E4%BD%9Cgrpc%E9%80%9A%E8%A8%8A%E5%AF%A6%E4%BD%9C/)

# 指令

```bash
# 安裝
pip install grpcio

# 安裝 gRPC 和 Protocol Buffers 的 Python 包
pip install grpcio grpcio-tools protobuf
```

# 用法

```proto
// calculator.proto
syntax = "proto3";

package calculator;

service Calculator {
  rpc Add (AddRequest) returns (AddResponse);
}

message AddRequest {
  int32 a = 1;
  int32 b = 2;
}

message AddResponse {
  int32 result = 1;
}
```

```bash
# 生成 calculator_pb2.py 和 calculator_pb2_grpc.py
# 兩個文件，用於在 Python 中使用 gRPC。
python -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. calculator.proto
```

```Python
# 服務器端代碼 (calculator_server.py)
import grpc
from concurrent import futures
import calculator_pb2
import calculator_pb2_grpc

class CalculatorServicer(calculator_pb2_grpc.CalculatorServicer):
    def Add(self, request, context):
        result = request.a + request.b
        return calculator_pb2.AddResponse(result=result)

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    calculator_pb2_grpc.add_CalculatorServicer_to_server(CalculatorServicer(), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    print("Server started on port 50051...")
    server.wait_for_termination()

if __name__ == '__main__':
    serve()
```

```Python
# 客戶端代碼 (calculator_client.py)
import grpc
import calculator_pb2
import calculator_pb2_grpc

def run():
    channel = grpc.insecure_channel('localhost:50051')
    stub = calculator_pb2_grpc.CalculatorStub(channel)

    a = 10
    b = 20
    request = calculator_pb2.AddRequest(a=a, b=b)

    response = stub.Add(request)
    print(f"Result of {a} + {b} is {response.result}")

if __name__ == '__main__':
    run()
```