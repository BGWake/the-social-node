<template>
  <v-container>
    <br />
    <h1 class="node-headline" v-if="posts == ''">
      {{ userNode }} hasn't made their first post yet, but you can tag them!
    </h1>
    <h1 class="node-headline" v-else>{{ userNode }}'s Node</h1>
    <span class="node-headline" v-show="$store.state.loggedInUsername != ''">
      <v-btn
        v-on:click="follow"
        class="ma-4"
        type="submit"
        v-show="!alreadyFollowing"
      >
        follow {{ userNode }}
      </v-btn>
    </span>

    <v-card
      id="cards"
      outlined
      elevation="6"
      style="margin: 30px; width: 50%"
      class="mx-auto"
      v-for="post in posts.slice().reverse()"
      :key="post.time"
      v-model="post.id"
    >
      <div class="d-flex justify-space-between">
        <v-card-title class="headline font-weight-bold">
          {{ post.username }}</v-card-title
        >
        <v-img
          class="mt-5 mr-5 rounded-xl"
          :lazy-src="findPhoto(post.username)"
          alt=""
          max-height="100"
          max-width="100"
          :src="findPhoto(post.username)"
        ></v-img>
      </div>
      <v-card-subtitle class="mt-n12"
        >posted on {{ post.time.substring(0, 10) }} at
        {{ post.time.substring(11, 19) }}</v-card-subtitle
      >

      <v-card-text class="headline">"{{ post.content }}"</v-card-text>
      <v-card-subtitle
        v-if="
          $store.state.loggedInUsername != '' && liked.indexOf(post.id) != -1
        "
      >
        <v-icon color="red" type="submit" @click="likeClick(post)"
          >mdi-heart
        </v-icon>
        <span v-show="post.likes != '' && post.likes != null">
          Liked by {{ post.likes }}</span
        >
      </v-card-subtitle>

      <v-card-subtitle v-else>
        <v-icon
          v-show="$store.state.loggedInUsername != ''"
          class="heart"
          type="submit"
          @click="likeClick(post)"
          >mdi-heart
        </v-icon>
        <span v-show="post.likes != '' && post.likes != null">
          Liked by {{ post.likes }}</span
        >
      </v-card-subtitle>
    </v-card>
  </v-container>
</template>

<script>
import socialService from "../services/SocialService";

export default {
  data: () => ({
    user: {
      username: "",
      following: "",
    },

    alreadyFollowing: false,

    userNode: "",
    currentUser: "",
    currentUsersFollowing: "",

    posts: [],
    liked: [],
    allUsers: [],
  }),

  watch: {
    "$route.path"(to, from) {
      if (to !== from) {
        this.renderComponent();
      }
    },
  },

  methods: {
    renderComponent() {
      this.currentUser = this.$store.state.loggedInUsername;
      this.userNode = this.$store.state.storeUsername;

      if (this.currentUser != "") {
        socialService
          .getFollowing(this.currentUser)
          .then((response) => {
            if (response.status == 200) {
              this.currentUsersFollowing = response.data;
              this.checkIfAlreadyFollowing(this.currentUsersFollowing);
            } else {
              console.error(response + " errors");
            }
          })
          .catch((err) => {
            console.error(err + " errors");
          });
      }

      socialService
        .getPostsByUsername(this.userNode)
        .then((response) => {
          this.posts = response.data;
          if (response.status == 200) {
            this.posts = response.data;
            this.addPostIdsToLikedArrayIfCurrentUserLikesThem(this.posts);
          } else {
            console.error(response + " errors");
          }
        })
        .catch((err) => {
          console.error(err + " errors");
        });
    },

    checkIfAlreadyFollowing(currentUsersFollowing) {
      if (currentUsersFollowing.includes(this.userNode + ",")) {
        this.alreadyFollowing = true;
      } else {
        this.alreadyFollowing = false;
      }
    },

    addPostIdsToLikedArrayIfCurrentUserLikesThem(posts) {
      for (var i = 0; i < posts.length; i++) {
        if (
          posts[i].likes != null &&
          posts[i].likes.includes(this.currentUser)
        ) {
          this.liked.push(posts[i].id);
        }
      }
    },

    findPhoto(username) {
      for (var i = 0; i < this.$store.state.allUsers.length; i++) {
        if (this.$store.state.allUsers[i].username === username) {
          return this.$store.state.allUsers[i].profilePic;
        }
      }
    },

    likeClick(post) {
      this.$store.commit("STORE_LIKED_ARRAY", this.liked);
      this.$store.commit(
        "EDIT_AND_STORE_LIKES_STRING_IN_POST_AND_EDIT_STORED_LIKED_ARRAY",
        post
      );

      this.liked = this.$store.state.liked;
      this.post = this.$store.state.post;

      socialService.updateRelevantPosts(this.posts);
      this.$store.commit("STORE_POSTS_IN_CASE_OF_LOGOUT", this.posts);
    },

    follow() {
      this.user.username = this.$store.state.loggedInUsername;
      this.user.following = this.userNode;

      socialService
        .follow(this.user)
        .then((response) => {
          if (response.status == 201) {
            this.alreadyFollowing = true;
          } else {
            console.error(response + " errors");
          }
        })
        .catch((err) => {
          console.error(err + " errors");
        });
    },
  },

  mounted() {
    this.renderComponent();
  },
};
</script>

<style>
</style>