<template>
  <nav>
    <v-app-bar fixed>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <router-link text class="home" router :to="{ name: 'home' }"
        ><h1>
          <v-toolbar-title class="text-uppercase home">
            Tweeter
          </v-toolbar-title>
        </h1></router-link
      >
      <v-spacer />

      <v-btn v-show="currentUser != ''" @click="logout"> logout </v-btn>
    </v-app-bar>

    <v-navigation-drawer app v-model="drawer" temporary class="lighten-5">
      <ul>
        <li>
          <router-link
            text
            style="color: black"
            class="links lighten-4"
            :to="{ name: 'your-feed' }"
          >
            Your Tweeter Feed
          </router-link>

          <u class="ma-3">Follow the Community</u>
          <router-link
            text
            style="color: black"
            class="links lighten-4"
            :key="$route.fullPath"
            :to="{ name: `feed`, params: { name: clickedUser } }"
          >
            <span
              style="color: black"
              class="links lighten-4"
              v-for="username in allUsernames"
              :key="username"
              v-show="username != currentUser"
              v-on:click="storeUser(username)"
            >
              {{ username }}'s Feed
            </span>
          </router-link>
        </li>
      </ul>
    </v-navigation-drawer>
  </nav>
</template>

<script>
import authService from "../services/AuthService";
import socialService from "../services/SocialService";

export default {
  name: "headerSection",
  data: () => ({
    drawer: false,
    logOutButton: [{ name: "Log Out", route: "logout" }],
    navLinks: [{ name: "Your Feed", route: "your-feed" }],
    allUsernames: [],
    currentUser: "",
    clickedUser: "",
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

      socialService
        .getAllUsernames()
        .then((requestData) => {
          this.allUsernames = requestData.data;
        })
        .catch((err) => {
          console.error(err + " errors");
        });
    },

    logout() {
      let user = { username: "", following: "" };

      authService
        .logout(this.currentUser)
        .then((response) => {
          if (response.status == 201 && this.$route.path != "/") {
            this.$store.commit("SET_LOGGED_IN_USER", user);
            this.$router.push("/");
          } else if (response.status == 201 && this.$route.path == "/") {
            this.currentUser = "";
            this.$store.commit("SET_LOGGED_IN_USER", user);
            this.renderComponent;
          } else {
            console.error(response + " errors");
            this.renderComponent;
          }
        })
        .catch((err) => {
          console.error(err + " errors");
        });
    },

    storeUser(username) {
      this.clickedUser = username;
      this.$store.commit("STORE_USER", username);
    },
  },

  mounted() {
    this.renderComponent();
  },
};
</script>

<style>
ul {
  list-style-type: none;
}

.links {
  display: grid;
  text-decoration: none;
  margin: 10%;
}

.home {
  text-decoration: none;
  color: black;
}
</style>