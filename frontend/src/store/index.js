import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    loggedInUsername: "",
    storeUsername: "",

    postsInCaseOfLogOut: [],
    allUsers: [],
    liked: [],
    post: [],
  },

  mutations: {

    SET_LOGGED_IN_USER(state, user) {
      state.loggedInUsername = user.username;
    },

    STORE_USER(state, username) {
      state.storeUsername = username;
    },

    STORE_POSTS_IN_CASE_OF_LOGOUT(state, posts) {
      state.postsInCaseOfLogOut = posts;
    },

    STORE_ALL_USERS(state, allUsers) {
      state.allUsers = allUsers;
    },

    STORE_LIKED_ARRAY(state, liked) {
      state.liked = liked;
    },

    EDIT_AND_STORE_LIKES_STRING_IN_POST_AND_EDIT_STORED_LIKED_ARRAY(state, post) {

      if (state.liked.includes(post.id)) {

        let currentUserInLikesWithTwoCommas = ", " + state.loggedInUsername + ", ";
        let currentUserInLikesWithCommaBefore = ", " + state.loggedInUsername;
        let currentUserInLikesWithCommaAfter = state.loggedInUsername + ", ";
        let commaSpace = ", ";

        if (post.likes.includes(currentUserInLikesWithTwoCommas)) {
          post.likes = post.likes.replaceAll(
            currentUserInLikesWithTwoCommas,
            commaSpace
          );
        } else if (post.likes.includes(currentUserInLikesWithCommaBefore)) {
          post.likes = post.likes.replaceAll(
            currentUserInLikesWithCommaBefore,
            ""
          );
        } else if (post.likes.includes(currentUserInLikesWithCommaAfter)) {
          post.likes = post.likes.replaceAll(currentUserInLikesWithCommaAfter, "");
        } else {
          post.likes = post.likes.replaceAll(state.loggedInUsername, "");
        }

        for (var i = 0; i < state.liked.length; i++) {
          if (state.liked[i] === post.id) {
            state.liked.splice(i, 1);
          }
        }

      } else {

        state.liked.push(post.id);

        if (state.loggedInUsername != "" && post.likes != null && post.likes != "") {
          post.likes = post.likes + ", " + state.loggedInUsername;
        } else {
          post.likes = state.loggedInUsername;
        }
      }
      state.post = post;
    }
  },
}
)