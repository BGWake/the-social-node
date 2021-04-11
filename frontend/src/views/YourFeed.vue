<template>
  <v-container>
    <br />
    <h1 class="node-headline" v-if="posts == ''">
      Hi {{ currentUser }}, make your first post below!
    </h1>
    <p class="node-headline" v-if="posts == ''">
      • You can follow other users to see their posts on your node. <br />
      • If you tag a user using '@', your post will post to their node in
      addition to yours. For example: "Hey @bob, I want to introduce you to
      @jim!" <br />
      • If you share another user's post it will show up on your node. <br />
      • Other users will not see the posts of who you follow when they visit
      your node, only your posts, posts you've shared, and posts you are tagged
      in.
    </p>
    <h1 class="node-headline" v-else>Hi {{ currentUser }}!</h1>
    <br />
    <v-textarea
      outlined
      style="margin: 30px; max-width: 700px"
      class="mx-auto ma-2"
      v-model="newPost.content"
      :counter="255"
      label="Type your post here"
    ></v-textarea>

    <span class="node-headline">
      <v-btn @click="post" class="mx-2" type="submit"> post </v-btn>
      <v-btn @click="clear" class="mx-2"> clear </v-btn>
    </span>

    <v-card
      id="cards"
      outlined
      elevation="6"
      style="margin: 30px; max-width: 700px"
      class="mx-auto"
      v-for="post in posts.slice().reverse()"
      :key="post.time"
      v-model="post.id"
    >
      <div class="d-flex justify-space-between">
        <v-card-title class="headline font-weight-bold">
          <router-link
            id="quicklink"
            :to="'/feed/' + post.username"
            v-if="post.username != currentUser"
            ><span v-on:click="storeUser(post.username)">
              {{ post.username }}
            </span>
          </router-link>
          <span v-else>
            {{ post.username }}
          </span>
        </v-card-title>
        <router-link
          id="quicklink"
          :to="'/feed/' + post.username"
          v-if="post.username != currentUser"
        >
          <v-img
            class="mt-5 mr-5 rounded-xl"
            :lazy-src="findPhoto(post.username)"
            alt=""
            max-height="50"
            max-width="50"
            :src="findPhoto(post.username)"
            v-on:click="storeUser(post.username)"
        /></router-link>
        <v-img
          class="mt-5 mr-5 rounded-xl"
          :lazy-src="findPhoto(post.username)"
          alt=""
          v-else
          max-height="50"
          max-width="50"
          :src="findPhoto(post.username)"
        />
      </div>
      <v-card-subtitle class="mt-n9"
        >posted on
        {{ post.time.substring(5, 10) + "-" + post.time.substring(2, 4) }}
        <br />at
        {{ post.time.substring(11, 19) }}
        EST</v-card-subtitle
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
    newPost: {
      username: "",
      content: "",
      shared: "",
    },

    currentUser: "",

    posts: [],
    liked: [],
    allUsers: [],
  }),

  methods: {
    getYourRelevantPosts() {
      if (this.$store.state.loggedInUsername == "") {
        this.$router.push("/");
      } else {
        socialService
          .getRelevantPosts(this.currentUser)
          .then((response) => {
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
      if (username === "Brandon") {
        return require("@/assets/brandon-headshot.jpg");
      }
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

    post() {
      this.newPost.username = this.currentUser;

      socialService
        .createPost(this.newPost)
        .then((response) => {
          if (response.status == 201) {
            socialService
              .getRelevantPosts(this.currentUser)
              .then((response) => {
                this.posts = response.data;
              });
          } else {
            console.error(response + " errors");
          }
        })
        .catch((err) => {
          console.error(err + " errors");
        });
      this.clear();
    },

    clear() {
      this.newPost.content = "";
    },

    storeUser(username) {
      this.$store.commit("STORE_USER", username);
    },
  },

  mounted() {
    this.currentUser = this.$store.state.loggedInUsername;
    this.getYourRelevantPosts();
  },
};
</script>

<style>
</style>