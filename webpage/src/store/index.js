import { createStore } from 'vuex'

export default createStore({
  state: {
    bgOpacity: 0.2,
    ambiguity: 0,
  },
  getters: {
    getBgOpacity: state => state.bgOpacity,
    getAmbiguity: state => state.ambiguity
  },
  mutations: {
    changeBgOpacity(state, opacity) {
      state.bgOpacity = opacity
    },
    changeAmbiguity(state, ambiguity) {
      state.ambiguity = ambiguity
    }
  },
  actions: {
  },
  modules: {
  }
})
