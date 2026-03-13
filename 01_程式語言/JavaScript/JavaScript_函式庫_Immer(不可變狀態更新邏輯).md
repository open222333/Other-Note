# JavaScript 函式庫 Immer(不可變狀態更新邏輯)

```
Immer 是一個用來幫助開發者編寫不可變狀態更新邏輯的 JavaScript 函式庫。
其名稱源自德語單詞「immer」，意思是「永遠」或「總是」，強調了不可變數據結構的持久性。
Immer 使得在 JavaScript 中處理不可變數據變得簡單和直觀，特別適用於狀態管理（如 Redux）。

Immer 的核心概念

不可變性（Immutability）：
不可變性指的是一旦創建，數據結構就不能被修改。每次對數據的更改都會返回一個新的數據結構。不可變性可以帶來更簡單的調試、更好的時間旅行調試和更少的副作用。

草稿（Drafts）：
Immer 通過代理機制，允許你在一個「草稿」狀態下自由地修改數據結構，而不會實際改變原始數據。修改完草稿後，Immer 會自動生成一個新的不可變狀態。
```

## 目錄

- [JavaScript 函式庫 Immer(不可變狀態更新邏輯)](#javascript-函式庫-immer不可變狀態更新邏輯)
  - [目錄](#目錄)
  - [參考資料](#參考資料)
- [安裝](#安裝)
- [用法](#用法)
  - [與 Redux 一起使用](#與-redux-一起使用)

## 參考資料

[]()

# 安裝

```bash
npm install immer
# 或者
yarn add immer
```

# 用法

```JavaScript
import produce from "immer";

const baseState = [
  {
    todo: "Learn Immer",
    done: true
  },
  {
    todo: "Use it in a project",
    done: false
  }
];

const nextState = produce(baseState, draftState => {
  draftState[1].done = true;
  draftState.push({ todo: "Tweet about it" });
});

console.log(baseState); // 原始狀態未改變
console.log(nextState); // 新狀態
```

## 與 Redux 一起使用

Immer 非常適合用於 Redux reducer，因為它可以簡化狀態更新的邏輯：

```Javascript
import produce from "immer";

const initialState = {
  todos: [
    { id: 1, text: "Learn Immer", done: true },
    { id: 2, text: "Integrate with Redux", done: false }
  ]
};

const todosReducer = (state = initialState, action) => {
  switch (action.type) {
    case "ADD_TODO":
      return produce(state, draftState => {
        draftState.todos.push({ id: action.id, text: action.text, done: false });
      });
    case "TOGGLE_TODO":
      return produce(state, draftState => {
        const todo = draftState.todos.find(todo => todo.id === action.id);
        if (todo) {
          todo.done = !todo.done;
        }
      });
    default:
      return state;
  }
};

export default todosReducer;
```
