<template>
  <v-container>
    <br />
    <h1 class="timeline-header" v-if="posts == ''">
      {{ userFeed }} hasn't made their first post yet, but you can tag them!
    </h1>
    <h1 class="timeline-header" v-else>{{ userFeed }}'s Node</h1>
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

          for (var i = 0; i < this.posts.length; i++) {
            if (this.posts[i].likes.includes(this.currentUser)) {
              this.liked.push(this.posts[i].id);
            }
          }
        })
        .catch((err) => {
          console.error(err + " errors");
        });
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

        if (post.likes.includes(currentUserInLikesWithTwoCommas)) {
          post.likes = post.likes.replace(currentUserInLikesWithTwoCommas, "");
        } else if (post.likes.includes(currentUserInLikesWithCommaBefore)) {
          post.likes = post.likes.replace(
            currentUserInLikesWithCommaBefore,
            ""
          );
        } else if (post.likes.includes(currentUserInLikesWithCommaAfter)) {
          post.likes = post.likes.replace(currentUserInLikesWithCommaAfter, "");
        } else if (post.likes.includes(this.currentUser)) {
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
  font-family: monospace;
}

#cards {
  transition: 1.3s;
  background-color: rgba(46, 110, 248, 0.205);
}

#cards:hover {
  transition: 1.3s;
  background-color: rgba(46, 110, 248, 0.41);
}
</style>