import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {

    loggedInUsername: "",
    storeUsername: "",
    postsInCaseOfLogOut: [],
    allUsers: [],
  },

  mutations: {

    SET_LOGGED_IN_USER(state, user) {
      state.loggedInUsername = user.username;
      state.following = user.following;
    },
    STORE_USER(state, username) {
      state.storeUsername = username;
    },
    STORE_POSTS_IN_CASE_OF_LOGOUT(state, posts) {
      state.postsInCaseOfLogOut = posts;
    },
    STORE_ALL_USERS(state, allUsers) {
      state.allUsers = allUsers;
    }
  },
}
)