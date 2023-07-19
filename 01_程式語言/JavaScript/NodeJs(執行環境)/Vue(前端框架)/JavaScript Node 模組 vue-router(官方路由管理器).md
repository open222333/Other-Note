# JavaScript Node 模組 vue-router(官方路由管理器)

```
Vue.js框架中的官方路由管理器。
它允許在Vue應用程序中實現客戶端路由，使能夠在不重新加載整個頁面的情況下切換不同的視圖。
這在構建單頁面應用程序（SPA）時非常有用，因為它提供了更流暢的用戶體驗。
```

## 目錄

- [JavaScript Node 模組 vue-router(官方路由管理器)](#javascript-node-模組-vue-router官方路由管理器)
	- [目錄](#目錄)
	- [參考資料](#參考資料)
- [指令](#指令)
- [用法](#用法)

## 參考資料

[vue-router npm 頁面](https://www.npmjs.com/package/vue-router)

# 指令

```bash
# 安裝
npm install vue-router

yarn add vue-router
```

# 用法

```JavaScript
// router.js
import Vue from 'vue';
import VueRouter from 'vue-router';

// 導入組件
import Home from './components/Home.vue';
import About from './components/About.vue';
import Contact from './components/Contact.vue';

Vue.use(VueRouter);

const routes = [
  { path: '/', component: Home },
  { path: '/about', component: About },
  { path: '/contact', component: Contact }
];

const router = new VueRouter({
  routes
});

export default router;
```

```JavaScript
// 將路由器實例掛載到Vue實例上，以便它可以處理路由導航。
// main.js
import Vue from 'vue';
import App from './App.vue';
import router from './router'; // 導入我們的路由器實例

new Vue({
  router, // 將路由器掛載到Vue實例上
  render: h => h(App)
}).$mount('#app');
```

```vue
<!--
Vue應用程序已經啟用了vue-router。
可以在組件中使用<router-link>標籤來導航到不同的路徑，並使用<router-view>標籤來顯示與當前路徑匹配的組件內容。 -->
<!-- App.vue -->
<template>
  <div>
    <router-link to="/">Home</router-link>
    <router-link to="/about">About</router-link>
    <router-link to="/contact">Contact</router-link>

    <router-view></router-view>
  </div>
</template>
```