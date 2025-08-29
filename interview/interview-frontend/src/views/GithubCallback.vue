<template>
  <div class="callback-container">
    <h2>GitHub 登录中...</h2>
    <p v-if="loading" style="color: #1976d2;">正在验证身份，请稍候</p>
    <p v-if="error" style="color: red;">{{ error }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'GithubCallback',
  data() {
    return {
      loading: true,
      error: null
    };
  },
  created() {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');
    const state = urlParams.get('state');

    if (!code || !state) {
      this.error = '缺少必要的授权参数 code 或 state';
      this.loading = false;
      return;
    }

    // 发送 POST 到统一登录接口 /api/login
    axios.post('/auth/api/login', {
      authType: 'github',
      code: code,
      state: state
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
        .then(response => {
          const token = response.data.data;

          localStorage.setItem('token', token);
          localStorage.setItem('username', code); // 可替换为实际用户名

          this.$router.push('/dashboard');
        })
        .catch(err => {
          console.error('GitHub 登录失败:', err.response?.data || err.message);
          this.error = 'GitHub 登录失败，请重试';
          this.loading = false;
        });
  }
};
</script>

<style scoped>
.callback-container {
  max-width: 400px;
  margin: 60px auto;
  padding: 30px;
  background-color: #f9f9f9;
  border-radius: 8px;
  text-align: center;
}
</style>
