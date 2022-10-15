import { createStore } from 'vuex'
import { ref } from 'vue'
export default createStore({
  state: {
    bgOpacity: 0.2,
    ambiguity: 0,
    avatar: ref(localStorage.getItem('avatar') || ''),
    wallpaper: ref(localStorage.getItem('wallpaper') || ''),
  },
  getters: {
    getBgOpacity: state => state.bgOpacity,
    getAmbiguity: state => state.ambiguity,
    getAvatar: state => state.avatar,
    getWallpaper: state => state.wallpaper,
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
    },
    changeWallpaper(state, wallpaper) {
      state.wallpaper = wallpaper
    }
  },
  actions: {
  },
  modules: {
  }
})
