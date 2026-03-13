# Retrieval-Augmented Generation (RAG)(檢索增強)

```
先搜尋出相關內容，放進 Prompt 裡面當作 context，再問 LLM
```

## 目錄

- [Retrieval-Augmented Generation (RAG)(檢索增強)](#retrieval-augmented-generation-rag檢索增強)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [](#)

## 參考資料

[]()

#


先將文本資料拆段落 (chunks)
1. 準備工作

每個段落建立 index，放到 Vector Database 向量資料庫 2. 當用戶問問題時

根據問題做語意搜尋，找出 “問題字串” 和 “最相關的內容片段” 共 N 筆

把 context 和問題組出 Prompt 再問 LLM

Prompt 範例:
