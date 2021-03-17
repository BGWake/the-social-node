import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {

    loggedInUsername: "",
    following: "",

    storeUsername: "",
    
  },
  mutations: {

    SET_LOGGED_IN_USER(state, user) {
      state.loggedInUsername = user.username;
      state.following = user.following;
    },
    STORE_USER(state, username) {
      state.storeUsername = username;
    },
    STORE_FOLLOWING(state, following) {
      state.following = following;
    }
    },
  }
)