# 程式設計 - MVC架構(Model–view–controller)

```
MVC模式（Model–view–controller）是軟體工程中的一種軟體架構模式，把軟體系統分為三個基本部分：模型（Model）、視圖（View）和控制器（Controller）。

模型（Model） 用於封裝與應用程式的業務邏輯相關的資料以及對資料的處理方法。「 Model 」有對資料直接存取的權力，例如對資料庫的存取。「Model」不依賴「View」和「Controller」，也就是說， Model 不關心它會被如何顯示或是如何被操作。但是 Model 中資料的變化一般會通過一種重新整理機制被公布。為了實現這種機制，那些用於監視此 Model 的 View 必須事先在此 Model 上註冊，從而，View 可以了解在資料 Model 上發生的改變。（比如：觀察者模式）

視圖（View）能夠實現資料有目的的顯示（理論上，這不是必需的）。在 View 中一般沒有程式上的邏輯。為了實現 View 上的重新整理功能，View 需要存取它監視的資料模型（Model），因此應該事先在被它監視的資料那裡註冊。

控制器（Controller）起到不同層面間的組織作用，用於控制應用程式的流程。它處理事件並作出回應。「事件」包括使用者的行為和資料 Model 上的改變。
```

## 目錄

- [程式設計 - MVC架構(Model–view–controller)](#程式設計---mvc架構modelviewcontroller)
	- [目錄](#目錄)
	- [參考資料](#參考資料)

## 參考資料

[MVC模式（Model–view–controller）- wiki](https://zh.wikipedia.org/zh-tw/MVC)
