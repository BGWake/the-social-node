<template>
  <v-container>
    <br />
    <h1 class="timeline-header" v-if="posts == ''">
      {{ userFeed }} hasn't made their first post yet, but you can tag them!
    </h1>
    <h1 class="timeline-header" v-else>{{ userFeed }}'s Feed</h1>
    <span class="timeline-header" v-show="$store.state.loggedInUsername != ''">
      <v-btn
        v-on:click="follow"
        class="ma-4"
        type="submit"
        v-show="!alreadyFollowing"
      >
        follow {{ userFeed }}
      </v-btn>
    </span>

    <v-card
      outlined
      elevation="6"
      style="margin: 30px; width: 50%"
      class="mx-auto light-blue lighten-5"
      v-for="post in posts.slice().reverse()"
      :key="post.time"
      v-model="post.id"
    >
      <v-card-title class="headline font-weight-bold">
        {{ post.username }}</v-card-title
      >
      <v-card-subtitle
        >posted on {{ post.time.substring(0, 10) }} at
        {{ post.time.substring(11, 19) }}</v-card-subtitle
      >
      <v-card-text class="headline">"{{ post.content }}"</v-card-text>
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
    currentUser: "",
    alreadyFollowing: false,
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
      this.userFeed = this.$store.state.storeUsername;

      if (this.currentUser != "") {
        socialService
          .getFollowing(this.currentUser)
          .then((response) => {
            if (response.status == 200) {
              var yourFollowing = response.data;

              if (yourFollowing.includes(this.userFeed + ",")) {
                this.alreadyFollowing = true;
              } else {
                this.alreadyFollowing = false;
              }
            } else {
              console.error(response + " errors");
            }
          })
          .catch((err) => {
            console.error(err + " errors");
          });
      }

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
.timeline-header {
  display: flex;
  justify-content: center;
}
</style>