# JavaScript Node 模組 vuex(官方狀態管理庫)

```
Vue.js的官方狀態管理庫，它用於在Vue應用程序中管理全局狀態。

在復雜的應用程序中，組件之間可能需要共享數據或進行狀態同步，而Vuex提供了一種集中式存儲管理解決方案。

核心包含以下幾個概念:

State（狀態）: 單一狀態樹（Single State Tree），用一個對象包含所有應用層級的狀態。它類似於組件的data，但是是全局共享的。

Getters（獲取器）: 用於從state中派生出一些狀態，類似於計算屬性（computed）。

Mutations（變更）: 用於修改state中的狀態，必須是同步函數。

Actions（動作）: 用於處理異步操作，可以包含任意異步操作。

Modules（模塊）: 用於將大型的Vuex狀態樹拆分為多個模塊，每個模塊有自己的state、getters、mutations和actions。
```

## 目錄

- [JavaScript Node 模組 vuex(官方狀態管理庫)](#javascript-node-模組-vuex官方狀態管理庫)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[vuex npm 頁面](https://www.npmjs.com/package/vuex)

# 指令

```bash
# 安裝
npm install vuex

yarn add vuex
```

# 用法

```JavaScript
// store.js（Vuex配置文件）
import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    count: 0,
  },
  mutations: {
    increment(state) {
      state.count++;
    },
    decrement(state) {
      state.count--;
    },
  },
  actions: {
    asyncIncrement({ commit }) {
      // 模擬異步操作
      setTimeout(() => {
        commit('increment');
      }, 1000);
    },
  },
  getters: {
    doubleCount(state) {
      return state.count * 2;
    },
  },
});

export default store;
```

```JavaScript
// main.js
// 在根Vue實例中，我們將上面定義的store實例掛載到Vue實例上，使其可以在整個應用程序中共享狀態。
import Vue from 'vue';
import App from './App.vue';
import store from './store'; // 導入上面定義的store

new Vue({
  store, // 將store實例掛載到Vue實例上
  render: h => h(App),
}).$mount('#app');
```

```vue
<!-- App.vue -->
<template>
  <div>
    <p>Count: {{ $store.state.count }}</p>
    <p>Double Count: {{ $store.getters.doubleCount }}</p>

    <button @click="$store.commit('increment')">Increment</button>
    <button @click="$store.commit('decrement')">Decrement</button>
    <button @click="$store.dispatch('asyncIncrement')">Async Increment</button>
  </div>
</template>
```