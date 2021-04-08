<template>
  <v-container>
    <br />
    <h1 class="node-headline" v-if="posts == ''">
      Hi {{ currentUser }}, make your first post below!
    </h1>
    <p class="node-headline" v-if="posts == ''">
      • You can follow other users to see their posts on your Node. <br />
      • If you tag a user using '@', your post will post to their Node in
      addition to yours. For example: "Hey @bob, I want to introduce you to
      @steve!" <br />
      • Other users will not see the posts of who you follow when they visit
      your node, only your posts and posts you are tagged in.
    </p>
    <h1 class="node-headline" v-else>
      Hi {{ currentUser }}! Welcome to your Node.
    </h1>
    <br />
    <v-textarea
      outlined
      style="margin: 30px; width: 50%"
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
    newPost: {
      username: "",
      content: "",
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
  },

  mounted() {
    this.currentUser = this.$store.state.loggedInUsername;
    this.getYourRelevantPosts();
  },
};
</script>

<style>
</style>