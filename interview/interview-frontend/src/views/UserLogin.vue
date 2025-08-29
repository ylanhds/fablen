<template>
  <div class="login-page">
    <div class="container">
      <h2>用户登录</h2>

      <!-- 动态消息 -->
      <p v-if="msg" class="msg">{{ msg }}</p>

      <!-- 登录方式 Tabs -->
      <div class="tabs">
        <div :class="{ tab: true, active: activeTab === 'database' }" @click="switchTab('database')">用户名密码</div>
        <div :class="{ tab: true, active: activeTab === 'ldap' }" @click="switchTab('ldap')">LDAP</div>
        <div :class="{ tab: true, active: activeTab === 'github' }" @click="switchTab('github')">GitHub</div>
      </div>

      <!-- 数据库登录表单 -->
      <form v-show="activeTab === 'database'" @submit.prevent="loginWithAuthType('database')">
        <input type="hidden" name="auth_type" value="database" />
        <div class="form-group">
          <label>用户名</label>
          <input v-model="credentials.username" required />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="credentials.password" type="password" required />
        </div>
        <button type="submit">登录</button>
      </form>

      <!-- LDAP 登录表单 -->
      <form v-show="activeTab === 'ldap'" @submit.prevent="loginWithAuthType('ldap')">
        <input type="hidden" name="auth_type" value="ldap" />
        <div class="form-group">
          <label>LDAP 用户名</label>
          <input v-model="credentials.ldapUsername" required />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="credentials.ldapPassword" type="password" required />
        </div>
        <button type="submit">LDAP 登录</button>
      </form>

      <!-- GitHub 登录表单 -->
      <form v-show="activeTab === 'github'">
        <button class="github-btn" type="button" @click="redirectToGithub">
          <img src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png" alt="GitHub" />
          使用 GitHub 登录
        </button>
      </form>

      <!-- 错误提示 -->
      <p v-if="msg" style="color: red;">{{ msg }}</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';


export default {
  data() {
    return {
      activeTab: 'database',
      credentials: {
        username: '',
        password: '',
        ldapUsername: '',
        ldapPassword: ''
      },
      msg: ''
    };
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab;
    },
    async loginWithAuthType(authType) {
      const payload = {};
      if (authType === 'database') {
        payload.username = this.credentials.username;
        payload.password = this.credentials.password;
        payload.authType = 'database';
      } else if (authType === 'ldap') {
        payload.username = this.credentials.ldapUsername;
        payload.password = this.credentials.ldapPassword;
        payload.authType = 'ldap';
      }

      try {
        const response = await axios.post('/prod/auth/api/login', payload, {
          headers: {
            'Content-Type': 'application/json'
          }
        });

        localStorage.setItem('token', response.data.data);
        localStorage.setItem('username', payload.username);

        this.$router.push('/dashboard');
      } catch (err) {
        this.msg = '登录失败，请检查用户名或密码';
      }
    },
    redirectToGithub() {
      const clientId = "Ov23li74AbOLBmiOMlzn"; // 👈 请使用安全的配置方式
      const redirectUri = "http://localhost:8081/login/oauth2/code/github";
      const state = Math.random().toString(36).substring(2);
      localStorage.setItem('github_oauth_state', state);

      window.open(
          `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${encodeURIComponent(redirectUri)}&state=${state}`,
          '_self'
      );
    }
  }
};
</script>

<style scoped>
.login-page body {
  background: #f7f7f7;
  font-family: sans-serif;
}

.container {
  max-width: 340px;
  margin: 60px auto;
  background: #fff;
  padding: 32px 24px 24px 24px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.tabs {
  display: flex;
  border-bottom: 1px solid #eee;
  margin-bottom: 18px;
}

.tab {
  flex: 1;
  padding: 8px 0;
  text-align: center;
  cursor: pointer;
  border-bottom: 2px solid transparent;
}

.tab.active {
  font-weight: bold;
  color: #1976d2;
  border-bottom: 2px solid #1976d2;
}

.form-group {
  margin-bottom: 16px;
}

label {
  display: block;
  margin-bottom: 4px;
  color: #555;
}

input {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

button {
  width: 100%;
  padding: 10px;
  background: #1976d2;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
}

.github-btn {
  background: #2d333b;
  margin-top: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
}

.github-btn img {
  height: 20px;
  margin-right: 10px;
}

.msg {
  color: #e53935;
  margin-bottom: 10px;
}
</style>
