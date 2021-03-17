<template>
  <v-container>
    <h1 class="timeline-header">{{ userFeed }}'s Feed</h1>
    <span class="timeline-header">
    <v-btn v-on:click="follow" class="ma-4" type="submit" v-show="$store.state.loggedInUsername != ''"> follow {{ userFeed }} </v-btn>
    </span>

    <v-card
      outlined
      elevation="6"
      style="margin: 30px; width: 50%"
      class="mx-auto pa-4 light-blue lighten-5"
      v-for="post in posts.slice().reverse()"
      :key="post.time"
      v-model="post.id"
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
  name: "feed",
  data: () => ({
    userFeed: "",
    posts: [],
    user: {
      username: "",
      following: "",
    },
  }),

  watch: {
      '$route.path' (to, from) {
        if(to !== from ) {
          this.renderComponent();
        }
      }
  },

  methods: {

    renderComponent() {
      this.userFeed = this.$store.state.storeUsername;

      socialService
      .getPostsByUsername(this.userFeed)
      .then((requestData) => {
        this.posts = requestData.data;
      })
      .catch((err) => {
        console.error(err + " errors");
      });
    },

    follow() {
      this.user.username = this.$store.state.loggedInUsername;
      this.user.following = this.userFeed;

      socialService
      .follow(this.user)
      .then((response) => {
          if (response.status == 201) {
            console.log("Response was 201!");
            //then change button to say following
          } else {
            console.log("Response was _NOT_ 201!");
          }
        })
        .catch((err) => {
          console.error(err + " errors");
        });
    }
  },

  mounted() {
    this.renderComponent();
  },
};
</script>

<style>
.timeline-header {
  display: flex;
  justify-content: center;
}
</style>