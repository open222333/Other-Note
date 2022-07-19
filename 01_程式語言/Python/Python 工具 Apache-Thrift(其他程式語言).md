# Python 工具 Apache-Thrift(其他程式語言)

## 目錄

- [Python 工具 Apache-Thrift(其他程式語言)](#python-工具-apache-thrift其他程式語言)
	- [目錄](#目錄)

```
Apache Thrift 是 Facebook 實現的一種高效的、支援多種程式語言的遠端服務呼叫的框架。
```

```
Thrift network stack
    Transport
        Transport網路讀寫（socket，http等）抽象，用於和其他thrift元件解耦。
        Transport的介面包括：open, close, read, write, flush, isOpen, readAll。
        Server端需要ServerTransport（對監聽socket的一種抽象），用於接收客戶端連線，介面包括：listen, accept, close。
        python中Transport的實現包括：TSocket, THttpServer, TSSLSocket, TTwisted, TZlibTransport，都是對某種協議或框架的實現。還有兩個裝飾器，用於為已有的Transport新增功能，TBufferedTransport（增加緩衝）和TFramedTransport（新增幀）。
        在建立server時，傳入的時Tranport的工廠，這些Factory包括：TTransportFactoryBase（沒有任何修飾，直接返回），TBufferedTransportFactory（返回帶緩衝的Transport）和TFramedTransportFactory（返回幀定位的Transport）。
    Protocol
        Protocol用於對資料格式抽象，在rpc呼叫時序列化請求和響應。
        TProtocol的實現包括：TJSONProtocol，TSimpleJSONProtocol，TBinaryProtocol，TBinaryPotocolAccelerated，TCompactProtocol。
    Processor
        Processor對stream讀寫抽象，最終會呼叫使用者編寫的handler已響應對應的service。具體的Processor有compiler生成，使用者需要實現service的實現類。
    Server
        Server建立Transport，輸入、輸出的Protocol，以及響應service的handler，監聽到client的請求然後委託給processor處理。
        TServer是基類，建構函式的引數包括：
            1) processor, serverTransport
            2) processor, serverTransport, transportFactory, protocolFactory
            3) processor, serverTransport, inputTransportFactory, outputTransportFactory, inputProtocolFactory, outputProtocolFactory
        TServer內部實際上需要3）所列的引數，1）和2）會導致對應的引數使用預設值。
        TServer的子類包括：TSimpleServer, TThreadedServer, TThreadPoolServer, TForkingServer, THttpServer, TNonblockingServer, TProcessPoolServer
        TServer的serve方法用於開始服務，接收client的請求。
    Code generated
        constants.py: 包含宣告的所有常量
        ttypes.py: 宣告的struct，實現了具體的序列化和反序列化
        SERVICE_NAME.py: 對應service的描述檔案，包含了：
        Iface: service介面定義
        Client: client的rpc呼叫樁
```

用法：

```
Thrift的用法實際上很簡單，定義好IDL，然後實現service對應的handler（方法名、引數列表與介面定義一致介面），最後就是選擇各個元件。
需要選擇的包括：Transport（一般都是socket，只是十分需要選擇buffed和framed裝飾器factory），Protocol，Server。
```

範例：

```
簡單記錄下在mac下使用python thrift的過程
```

```
1. 安裝 Thrift 的 python 庫有兩種方案(1. pip安裝 2. 原始碼安裝)具體參見文末連結
    1）pip安裝： pip install thrift(最好在venv中使用)
2. 安裝 Thrift 的 IDL 編譯工具（windows/linux安裝見文末連結）
    1）mac下安裝： brew install thrift
    $ thrift -version，如果打印出來：Thrift version x.x.x 表明 complier 安裝成功
3. 建立專案目錄(thrift_demo)並開始編碼
    1）目錄結構
       <1> client目錄下的 client.py 實現了客戶端用於傳送資料並列印接收到 server 端處理後的資料
       <2> server 目錄下的 server.py 實現了服務端用於接收客戶端傳送的資料，並對資料進行大寫處理後返回給客戶端
       <3> thrift_file 用於存放 thrift 的 IDL 檔案： *.thrift
    2) 定義 Thrift RPC 介面IDL檔案 example.thrift:
            namespace py example

            struct Data {
                1: string text
                2: i32 id
            }

            service format_data {
                Data do_format(1:Data data),
            }
        進入 thrift_file 目錄執行：$ thrift -out .. --gen py example.thrift，就會在 thrift_file 的同級目錄下生成 python 的包：example
    3) 實現 server 端server.py:
        #! /usr/bin/env python
        # -*- coding: utf-8 -*-

        import os
        import sys
        cur_path =os.path.abspath(os.path.join(os.path.dirname('__file__'), os.path.pardir))
        sys.path.append(cur_path)

        from example import format_data
        from example import ttypes
        from thrift.transport import TSocket
        from thrift.transport import TTransport
        from thrift.protocol import TBinaryProtocol
        from thrift.server import TServer

        __HOST = 'localhost'
        __PORT = 9000


        class FormatDataHandler(object):
            def do_format(self, data):
                print(data.text, data.id)
                # can do something
                return ttypes.Data(data.text.upper(), data.id)


        if __name__ == '__main__':
            handler = FormatDataHandler()

            processor = format_data.Processor(handler)
            transport = TSocket.TServerSocket(__HOST, __PORT)
            tfactory = TTransport.TBufferedTransportFactory()
            pfactory = TBinaryProtocol.TBinaryProtocolFactory()

            rpcServer = TServer.TSimpleServer(processor,transport, tfactory, pfactory)

            print('Starting the rpc server at', __HOST,':', __PORT)
            rpcServer.serve()
            print('done')
    4) 實現 client 端client.py:
        #! /usr/bin/env python
        # -*- coding: utf-8 -*-

        import os
        import sys
        sys.path.append(os.path.abspath(os.path.join(os.path.dirname('__file__'), os.path.pardir)))

        from thrift.transport import TSocket
        from thrift.transport import TTransport
        from thrift.protocol import TBinaryProtocol
        from example.format_data import Client
        from example.format_data import Data


        __HOST = 'localhost'
        __PORT = 9000


        try:
            tsocket = TSocket.TSocket(__HOST, __PORT)
            transport = TTransport.TBufferedTransport(tsocket)
            protocol = TBinaryProtocol.TBinaryProtocol(transport)
            client = Client(protocol)

            data = Data('hello,world!', 123)
            transport.open()
            print('client-requets')
            res = client.do_format(data)
            # print(client.do_format(data).text)
            print('server-answer', res)

            transport.close()
        except Thrift.TException as ex:
            print(ex.message)
4. 執行驗證結果
    1) 先啟動 server(進入server目錄，執行python server.py)，之後再另一個視窗執行 client（進入client目前，執行python client.py）:
```
