// src/store/index.js
import { createStore } from 'vuex'

export default createStore({
  state: {
    isLoggedIn: false,
    user: null
  },
  mutations: {
    login(state, user) {
      state.isLoggedIn = true
      state.user = user
    },
    logout(state) {
      state.isLoggedIn = false
      state.user = null
    }
  },
  actions: {
    login({ commit }, user) {
      commit('login', user)
    },
    logout({ commit }) {
      commit('logout')
    }
  },
  modules: {}
})
