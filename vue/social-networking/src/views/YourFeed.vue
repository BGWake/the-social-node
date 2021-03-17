<template>
  <v-container>
    <h1 class="timeline-header">Hi {{ currentUser }}! Welcome back to your Feed.</h1>

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
        "{{ post.content }}"<br />
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

    post() {
      this.newPost.username = this.currentUser;

      socialService
        .createPost(this.newPost)
        .then((response) => {
          if (response.status == 201) {
            console.log("Response was 201!");
            socialService
              .getRelevantPosts(this.currentUser)
              .then((requestData) => {
                this.posts = requestData.data;
              })
              .catch((err) => {
                console.error(err + " errors");
              });
          } else {
            console.log("Response was _NOT_ 201!");
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
  created() {
    if (this.$store.state.loggedInUsername == "") {
      this.$router.push('/');
    }
    this.currentUser = this.$store.state.loggedInUsername;

    socialService
      .getRelevantPosts(this.currentUser)
      .then((response) => {
        if (response.status == 200) {
        console.log("Response was 200!")
        this.posts = response.data.flat();
        } else {
          console.log("Response was _NOT_ 200!");
          console.error(response + " errors")
        }
      })
      .catch((err) => {
        console.error(err + " errors");
      });
    },
};
</script>

<style>
.timeline-header {
  display: flex;
  justify-content: center;
}
</style>