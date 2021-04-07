<template>
  <v-container>
    <br />
    <h1 class="timeline-header" v-if="posts == ''">
      Hi {{ currentUser }}, make your first post below!
    </h1>
    <p class="timeline-header" v-if="posts == ''">
      • You can follow other users to see their posts on your Node. <br />
      • If you tag a user using '@', your post will post to their Node in
      addition to yours. For example: "Hey @bob, I want to introduce you to
      @steve!" <br />
      • Other users will not see the posts of who you follow when they visit
      your node, only your posts and posts you are tagged in.
    </p>
    <h1 class="timeline-header" v-else>
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

    <span class="timeline-header">
      <v-btn v-on:click="post" class="mx-2" type="submit"> post </v-btn>
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
  name: "yourFeed",
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

              for (var i = 0; i < this.posts.length; i++) {
                if (this.posts[i].likes.includes(this.currentUser)) {
                  this.liked.push(this.posts[i].id);
                }
              }
            } else {
              console.error(response + " errors");
            }
          })
          .catch((err) => {
            console.error(err + " errors");
          });
      }
    },

    findPhoto(username) {
      this.allUsers = this.$store.state.allUsers;

      for (var i = 0; i < this.allUsers.length; i++) {
        if (this.allUsers[i].username == username) {
          return this.allUsers[i].profilePic;
        }
      }
    },

    updatePosts() {
      socialService.updateRelevantPosts(this.posts);
    },

    likeClick(post) {
      if (this.liked.includes(post.id)) {
        let currentUserInLikesWithTwoCommas = ", " + this.currentUser + ", ";
        let currentUserInLikesWithCommaBefore = ", " + this.currentUser;
        let currentUserInLikesWithCommaAfter = this.currentUser + ", ";
        let commaSpace = ", ";

        if (post.likes.includes(currentUserInLikesWithTwoCommas)) {
          post.likes = post.likes.replace(
            currentUserInLikesWithTwoCommas,
            commaSpace
          );
        } else if (post.likes.includes(currentUserInLikesWithCommaBefore)) {
          post.likes = post.likes.replace(
            currentUserInLikesWithCommaBefore,
            ""
          );
        } else if (post.likes.includes(currentUserInLikesWithCommaAfter)) {
          post.likes = post.likes.replace(currentUserInLikesWithCommaAfter, "");
        } else {
          post.likes = post.likes.replace(this.currentUser, "");
        }

        for (var i = 0; i < this.liked.length; i++) {
          if (this.liked[i] === post.id) {
            this.liked.splice(i, 1);
          }
        }
      } else {
        this.liked.push(post.id);

        if (this.currentUser != "" && post.likes != null && post.likes != "") {
          post.likes = post.likes + ", " + this.currentUser;
        } else {
          post.likes = this.currentUser;
        }
      }

      this.updatePosts();
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