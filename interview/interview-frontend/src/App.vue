<template>
  <div id="app">
    <nav class="navbar">
      <div class="container">
        <h1>Interview UAA 系统</h1>
      </div>
    </nav>

    <!-- 路由出口 -->
    <router-view />
  </div>
</template>

<script>
export default {
  methods: {
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
#app {
  font-family: Arial, sans-serif;
}

.navbar {
  background-color: #1976d2;
  color: white;
  padding: 10px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: center; /* 修改为居中 */
  align-items: center; /* 垂直居中 */
  text-align: center; /* 确保文本居中 */
}
</style>
