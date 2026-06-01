# Python 模組 Flask-即時推送(SSE_WebSocket_輪詢)

```
三種讓 Flask 主動或即時推送資料給前端的方式：
SSE（Server-Sent Events）、WebSocket、縮短輪詢間隔
```

## 目錄

- [Python 模組 Flask-即時推送(SSE\_WebSocket\_輪詢)](#python-模組-flask-即時推送sse_websocket_輪詢)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [比較](#比較)
- [SSE（Server-Sent Events）](#sseserver-sent-events)
  - [安裝](#安裝)
  - [用法](#用法)
    - [Server（Flask）](#serverflask)
    - [Client（JavaScript）](#clientjavascript)
    - [注意事項](#注意事項)
- [WebSocket（Flask-SocketIO）](#websocketflask-socketio)
  - [安裝](#安裝-1)
  - [用法](#用法-1)
    - [Server（Flask）](#serverflask-1)
    - [Client（JavaScript）](#clientjavascript-1)
    - [注意事項](#注意事項-1)
- [縮短輪詢間隔（Short Polling）](#縮短輪詢間隔short-polling)
  - [用法](#用法-2)
    - [Server（Flask）](#serverflask-2)
    - [Client（JavaScript）](#clientjavascript-2)
    - [注意事項](#注意事項-2)

## 參考資料

[SSE — MDN Web Docs](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events)

[Flask stream_with_context — 官方文檔](https://flask.palletsprojects.com/en/latest/patterns/streaming/)

[Flask-SocketIO — 官方文檔](https://flask-socketio.readthedocs.io/en/latest/)

[Flask-SocketIO — PyPI](https://pypi.org/project/Flask-SocketIO/)

---

# 比較

| 技術 | 方向 | 延遲 | 複雜度 | 適用情境 |
|---|---|---|---|---|
| **SSE** | Server → Client（單向） | 低 | 低（Flask 原生支援） | 即時通知、進度更新、日誌串流 |
| **WebSocket** | 雙向 | 最低 | 高（需 Flask-SocketIO） | 聊天室、即時協作、遊戲 |
| **縮短輪詢** | Client 主動拉取 | 中（視間隔） | 最低（改一行） | 低頻更新、快速驗證可行性 |

---

# SSE（Server-Sent Events）

Flask 原生支援，透過 `Response` 持續推送資料流，前端用 `EventSource` 接收。  
連線中斷時瀏覽器會**自動重連**。

## 安裝

```bash
# 不需額外安裝，Flask 內建支援
```

## 用法

### Server（Flask）

```python
import time
from flask import Flask, Response, stream_with_context

app = Flask(__name__)


def event_stream():
    """產生 SSE 格式的資料流"""
    count = 0
    while True:
        count += 1
        # SSE 格式：data: <內容>\n\n
        yield f"data: count={count}\n\n"
        time.sleep(1)


@app.route("/stream")
def stream():
    return Response(
        stream_with_context(event_stream()),
        mimetype="text/event-stream",
        headers={
            "Cache-Control": "no-cache",
            "X-Accel-Buffering": "no",   # 停用 Nginx 緩衝
        },
    )
```

```python
# 推送自訂 event 名稱（前端用 addEventListener 區分）
def event_stream():
    yield "event: status\ndata: connected\n\n"
    while True:
        data = get_latest_data()         # 取得最新資料
        yield f"event: update\ndata: {data}\n\n"
        time.sleep(2)
```

### Client（JavaScript）

```javascript
const source = new EventSource("/stream");

// 接收預設 message 事件
source.onmessage = (e) => {
  console.log("收到:", e.data);
};

// 接收自訂 event 名稱
source.addEventListener("update", (e) => {
  document.getElementById("output").textContent = e.data;
});

source.onerror = () => {
  console.warn("SSE 連線中斷，瀏覽器將自動重連");
};

// 手動關閉
// source.close();
```

### 注意事項

```
- Nginx 反向代理需關閉緩衝：proxy_buffering off;
- Gunicorn 需用 gevent 或 eventlet worker，避免執行緒阻塞
- SSE 只支援 GET 請求，無法帶 POST body
- 同一瀏覽器對同一 origin 的 SSE 連線數有上限（HTTP/1.1 為 6 條）
```

```python
# Gunicorn 啟動範例（gevent）
# gunicorn -k gevent -w 4 app:app
```

---

# WebSocket（Flask-SocketIO）

雙向即時通訊，client 與 server 皆可主動發送訊息。  
底層自動降級：WebSocket → Long Polling。

## 安裝

```bash
pip install flask-socketio

# 需搭配 async worker（擇一安裝）
pip install eventlet
# 或
pip install gevent gevent-websocket
```

## 用法

### Server（Flask）

```python
from flask import Flask, render_template
from flask_socketio import SocketIO, emit

app = Flask(__name__)
app.config["SECRET_KEY"] = "your-secret"
socketio = SocketIO(app)                  # 預設使用 eventlet/gevent


# 客戶端連線時
@socketio.on("connect")
def on_connect():
    print(f"客戶端連線：{request.sid}")
    emit("status", {"msg": "connected"})


# 客戶端斷線時
@socketio.on("disconnect")
def on_disconnect():
    print(f"客戶端斷線：{request.sid}")


# 接收客戶端訊息
@socketio.on("message")
def handle_message(data):
    print("收到訊息:", data)
    emit("response", {"echo": data})      # 回覆給同一個 client


# 廣播給所有已連線的 client
@socketio.on("broadcast")
def handle_broadcast(data):
    emit("message", data, broadcast=True)


# Server 主動推送（在其他地方呼叫）
def push_update(data):
    socketio.emit("update", {"value": data})


if __name__ == "__main__":
    socketio.run(app, debug=True)
```

```python
# 使用 Room 分組推送
from flask_socketio import join_room, leave_room

@socketio.on("join")
def on_join(data):
    room = data["room"]
    join_room(room)
    emit("status", {"msg": f"已加入 {room}"}, to=room)

@socketio.on("send_to_room")
def on_send(data):
    emit("message", data["msg"], to=data["room"])
```

### Client（JavaScript）

```html
<!-- 引入 socket.io client -->
<script src="https://cdn.socket.io/4.7.5/socket.io.min.js"></script>
```

```javascript
const socket = io();                      // 自動連線到同 origin

socket.on("connect", () => {
  console.log("已連線，ID:", socket.id);
});

socket.on("status", (data) => {
  console.log(data.msg);
});

socket.on("update", (data) => {
  document.getElementById("output").textContent = data.value;
});

// 向 server 發送訊息
document.getElementById("btn").addEventListener("click", () => {
  socket.emit("message", { text: "Hello" });
});

socket.on("disconnect", () => {
  console.warn("連線中斷");
});
```

### 注意事項

```
- Nginx 反向代理需加 WebSocket upgrade header
- 不可用 Flask 內建的 app.run()，必須用 socketio.run(app)
- Gunicorn 需搭配 eventlet 或 gevent worker
- 有狀態連線，水平擴展需搭配 Redis 做 message queue
```

```nginx
# Nginx WebSocket 設定
location /socket.io/ {
    proxy_pass http://127.0.0.1:5000;
    proxy_http_version 1.1;
    proxy_set_header Upgrade $http_upgrade;
    proxy_set_header Connection "upgrade";
    proxy_set_header Host $host;
}
```

```python
# Gunicorn 啟動範例（eventlet）
# gunicorn -k eventlet -w 1 app:app
# WebSocket 有狀態，worker 數建議設為 1，搭配 Redis 擴展
```

---

# 縮短輪詢間隔（Short Polling）

前端定時向 server 發送請求，server 每次回傳當前資料。  
實作最簡單，適合資料更新頻率不高、快速驗證需求的情境。

## 用法

### Server（Flask）

```python
from flask import Flask, jsonify

app = Flask(__name__)


@app.route("/api/status")
def get_status():
    data = fetch_latest_data()            # 取得最新資料
    return jsonify({"value": data})
```

### Client（JavaScript）

```javascript
// 每 2 秒輪詢一次
const INTERVAL_MS = 2000;

async function poll() {
  try {
    const res = await fetch("/api/status");
    const data = await res.json();
    document.getElementById("output").textContent = data.value;
  } catch (err) {
    console.error("輪詢失敗:", err);
  }
}

const timerId = setInterval(poll, INTERVAL_MS);

// 停止輪詢
// clearInterval(timerId);
```

### 注意事項

```
- 間隔越短，請求越頻繁，伺服器負載越高
- 資料沒有變更時仍會發送請求（浪費資源）
- 適合 update 頻率低（> 5 秒）或臨時測試用途
- 正式環境建議改用 SSE（單向）或 WebSocket（雙向）
```
