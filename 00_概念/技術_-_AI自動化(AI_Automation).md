# 技術 - AI自動化(AI_Automation)

```
AI 自動化不只有 BDD，還涵蓋流程自動化、開發自動化、知識自動化、Agent 自動化與企業 AI 平台
用途：釐清各技術定位、規劃導入順序與學習路徑
```

## 目錄

- [技術 - AI自動化(AI\_Automation)](#技術---ai自動化ai_automation)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
  - [AI 自動化分類](#ai-自動化分類)
  - [AI 自動化成熟度](#ai-自動化成熟度)
  - [建議學習順序](#建議學習順序)
  - [BDD 在整體中的位置](#bdd-在整體中的位置)
  - [企業常見的 AI 自動化組合](#企業常見的-ai-自動化組合)
  - [導入重點](#導入重點)

## 參考資料

[Retrieval-Augmented Generation (RAG)(檢索增強)](../00_其他(工具_應用程式_網站)/LLM(大型語言模型)/Retrieval-Augmented_Generation_(RAG)(檢索增強).md)

[提示工程 CoT(Chain-of-Thought)(思維鏈)](../00_其他(工具_應用程式_網站)/LLM(大型語言模型)/提示工程（Prompt_Engineering）_CoT(Chain-of-Thought)(思維鏈).md)

[提示工程 ToT(Tree-of-Thought)(思維樹)](../00_其他(工具_應用程式_網站)/LLM(大型語言模型)/提示工程（Prompt_Engineering）_ToT(Tree-of-Thought)(思維樹).md)

[AI 多人協作開發架構(團隊AI整合)](../01_程式語言/AI/AI_多人協作開發架構(團隊AI整合).md)

## AI 自動化分類

| 類別 | 用途 | 常見工具 / 技術 |
|---|---|---|
| **BDD**（Behavior-Driven Development） | 用自然語言描述需求，自動產生測試 | Cucumber、SpecFlow、Playwright + Gherkin |
| **TDD** | 先寫測試，再讓 AI 協助產生程式 | GitHub Copilot、Claude Code、ChatGPT |
| **RPA** | 模擬人工操作系統 | UiPath、Automation Anywhere、Power Automate |
| **Workflow Automation** | 自動串接不同服務 | n8n、Make、Zapier |
| **AI Agent** | AI 自主完成多步驟工作 | OpenAI Agents、LangGraph、CrewAI、AutoGen |
| **MCP** | AI 連接外部工具與資料 | MCP Server、生態系工具 |
| **A2A** | 多個 AI Agent 協同合作 | Google A2A Protocol |
| **Prompt Engineering** | 設計高品質提示詞 | Chain of Thought、Few-shot、ReAct |
| **RAG** | AI 查詢企業知識庫 | LangChain、LlamaIndex、向量資料庫 |
| **Function Calling** | AI 呼叫 API、資料庫、工具 | OpenAI Function Calling、MCP |
| **CI/CD** | 自動建置、測試、部署 | GitHub Actions、GitLab CI、Jenkins |
| **Observability** | 監控 AI 成本、品質、效能 | Langfuse、Helicone、OpenTelemetry |

## AI 自動化成熟度

| Level | 階段 | 涵蓋技術 |
|---|---|---|
| 1 | 流程自動化 | RPA、n8n、Zapier |
| 2 | 開發自動化 | BDD、TDD、AI Coding、CI/CD |
| 3 | 知識自動化 | RAG、向量資料庫、文件搜尋 |
| 4 | Agent 自動化 | MCP、AI Agent、Multi-Agent、A2A |
| 5 | 企業 AI 平台 | 整合 Agent + Workflow + RAG + MCP + Observability，形成完整的企業 AI 工作平台 |

## 建議學習順序

1. Python
2. API（REST）
3. Git
4. Prompt Engineering
5. n8n（工作流程）
6. MCP
7. AI Agent
8. RAG
9. BDD（測試自動化）
10. CI/CD（部署）
11. Multi-Agent / A2A

## BDD 在整體中的位置

BDD 主要負責需求描述、驗收測試、自動化測試，屬於**開發自動化（Level 2）的一部分**，並不是 AI 自動化的全部。

## 企業常見的 AI 自動化組合

```text
使用者
   ↓
AI Agent
   ↓
MCP（工具連接）
   ↓
Workflow（n8n）
   ↓
RAG（企業知識庫）
   ↓
API / 資料庫 / SaaS
```

此架構能讓 AI 回答問題、查詢文件、呼叫 API、更新資料庫、發送通知，並執行完整工作流程。

## 導入重點

BDD 很重要，但只是其中一個環節。要讓 AI 從「聊天」進化成能執行工作的自動化助手，重點投入：

- **MCP**
- **AI Agent**
- **Workflow Automation**
- **RAG**
- **CI/CD**
- **Observability**
