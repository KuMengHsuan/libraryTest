<template>
  <div id="app">
    <h1>Hello Vue Router App!</h1>
    <p>
      <!-- router-link 在經過編譯後會變成 <a> 標籤 -->
      <!-- 使用 to 屬性來處理目標 URL -->
      <router-link to="/">Go to Home</router-link>
      <router-link to="/login">Go to About</router-link>
    </p>
    <!-- 渲染 route 的位置 -->
    <router-view></router-view>
  </div>
</template>
<style scoped>
#app {
  margin: 1rem;
}

p > a {
  margin-right: 1rem;
}

section {
  border: 1px solid #000;
  padding: 10px;
}
</style>