import { createStore } from 'vuex'
import { ref } from 'vue'
export default createStore({
  state: {
    bgOpacity: 0.2,
    ambiguity: 0,
    avatar: ref(localStorage.getItem('avatar') || ''),
  },
  getters: {
    getBgOpacity: state => state.bgOpacity,
    getAmbiguity: state => state.ambiguity,
    getAvatar: state => state.avatar,
  },
  mutations: {
    changeBgOpacity(state, opacity) {
      state.bgOpacity = opacity
    },
    changeAmbiguity(state, ambiguity) {
      state.ambiguity = ambiguity
    },
    changeAvatar(state, avatar) {
      state.avatar = avatar
    }
  },
  actions: {
  },
  modules: {
  }
})
