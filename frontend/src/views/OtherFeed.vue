<template>
  <v-container>
    <br />
    <h1 class="node-headline" v-if="posts == ''">
      {{ userNode }} hasn't made their first post yet, but you can tag them!
    </h1>
    <h1 class="node-headline" v-else>{{ userNode }}'s Node</h1>
    <div v-show="alreadyFollowing" class="node-headline">
      <v-icon color="green"> mdi-check </v-icon> You're following
      {{ userNode }}.
    </div>
    <span class="node-headline" v-show="$store.state.loggedInUsername != ''">
      <v-btn
        v-on:click="follow"
        class="ma-4"
        type="submit"
        v-show="!alreadyFollowing"
      >
        follow {{ userNode }}
      </v-btn>
      <v-btn
        v-on:click="unfollow"
        class="ma-4"
        type="submit"
        v-show="alreadyFollowing"
      >
        unfollow {{ userNode }}
      </v-btn>
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
            v-if="post.username != userNode"
            v-on:click="storeUser(post.username)"
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
          v-if="post.username != userNode"
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
      <span class="d-flex justify-space-between">
        <v-card-subtitle
          class="d-flex"
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
        <v-btn
          v-if="
            !post.shared.includes(currentUser + ',') &&
            currentUser != '' &&
            post.username != currentUser
          "
          v-on:click="share(post)"
          v-show="currentUser != ''"
          class="d-flex mt-2"
          icon-color="none"
          text
          >Share</v-btn
        >
        <span
          v-else-if="currentUser != '' && post.username != currentUser"
          class="d-flex align-baseline mr-4"
          ><v-icon color="green"> mdi-check </v-icon>
          <p class="mt-4">Shared!</p></span
        >
      </span>
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
            // this.convertFromMilitaryTime();
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

    unfollow() {
      this.user.username = this.$store.state.loggedInUsername;
      this.user.following = this.userNode;

      socialService
        .unfollow(this.user)
        .then((response) => {
          if (response.status == 201) {
            this.renderComponent();
          } else {
            console.error(response + " errors");
          }
        })
        .catch((err) => {
          console.error(err + " errors");
        });
    },

    storeUser(username) {
      this.$store.commit("STORE_USER", username);
    },

    share(post) {
      if (post.shared == "") {
        post.shared = this.currentUser + ",";
      } else {
        post.shared = post.shared + (this.currentUser + ",");
      }
      socialService
        .createPost(post)
        .then((response) => {
          if (response.status == 201) {
            this.renderComponent();
          } else {
            console.error(response + " errors");
          }
        })
        .catch((err) => {
          console.error(err + " errors");
        });
    },

    // convertFromMilitaryTime() {

    //   this.$store.commit("CONVERT", this.posts);

    //   for (let i = 0; i < this.posts.length; i++) {
    //     let militaryTimeString = this.posts[i].time.substring(11, 19);
    //     let time = militaryTimeString.split(":");

    //     let hours = Number(time[0]);
    //     let minutes = Number(time[1]);
    //     let seconds = Number(time[2]);

    //     let timeResult = "";

    //     if (hours > 0 && hours <= 12) {
    //       timeResult = "" + hours;
    //     } else if (hours > 12) {
    //       timeResult = "" + (hours - 12);
    //     } else if (hours == 0) {
    //       timeResult = "12";
    //     }

    //     timeResult += minutes < 10 ? ":0" + minutes : ":" + minutes;
    //     timeResult += seconds < 10 ? ":0" + seconds : ":" + seconds;
    //     timeResult += hours >= 12 ? " PM" : " AM";

    //     console.log(timeResult);

    //     this.posts[i].time = this.posts[i].time.replaceAll(this.posts[i].time.substring(11), timeResult);
    //   }
    // },
  },

  mounted() {
    this.renderComponent();
  },
};
</script>

<style>
</style>