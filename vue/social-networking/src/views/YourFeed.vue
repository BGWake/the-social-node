<template>
  <v-container>
    <h1 class="timeline-header" v-if="posts == ''">Hi {{ currentUser }}, make your first post below!</h1>
    <p class="timeline-header" v-if="posts == ''">• You can follow other users to see their posts on your feed. 
      <br>• If you tag a user using '@', your post will post to their feed in addition yours. 
      For example: "Hey @bob, I want to introduce you to @steve!"
      <br>• Other users will not see the posts of who you follow when they visit your feed, only your posts and posts you are tagged in.</p>
    <h1 class="timeline-header" v-else>Hi {{ currentUser }}! Welcome to your Feed.</h1>
    <br />
    <v-textarea
      outlined
      style="margin: 30px; width: 50%"
      class="mx-auto timeline-header ma-2"
      v-model="newPost.content"
      :counter="255"
      label="Type your post here"
    ></v-textarea>

    <span class="timeline-header">
    <v-btn v-on:click="post" class="mx-2" type="submit"> post </v-btn> <v-btn @click="clear" class="mx-2"> clear </v-btn>
    </span>

    <v-card
      outlined
      elevation="6"
      style="margin: 30px; width: 50%"
      class="mx-auto pa-4 light-blue lighten-5"
      v-for="post in posts.slice().reverse()"
      :key="post.id"
    >
      <body>
        <strong>{{ post.username }}</strong> posted on {{ post.time.substring(0, 10) }} at
        {{ post.time.substring(11, 19) }}<br />
        "{{ post.content }}"
      </body>
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
  }),

  methods: {

    getYourRelevantPosts() {
      socialService
        .getRelevantPosts(this.currentUser)
        .then((response) => {
          if (response.status == 200) {
          this.posts = response.data;
          } else {
          console.error(response + " errors")
        }
      })
      .catch((err) => {
        console.error(err + " errors");
      });
    },

    post() {
      this.newPost.username = this.currentUser;

      socialService
        .createPost(this.newPost)
        .then((response) => {
          if (response.status == 201) {
            this.getYourRelevantPosts(this.currentUser);
          } else {
            console.error(response + " errors")
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
    if (this.$store.state.loggedInUsername == "") {
      this.$router.push('/');
    }
    this.currentUser = this.$store.state.loggedInUsername;
    this.getYourRelevantPosts();
  },
};
</script>

<style>
.timeline-header {
  display: flex;
  justify-content: center;
}
</style>