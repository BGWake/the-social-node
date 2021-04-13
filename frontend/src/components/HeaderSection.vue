<template>
  <nav>
    <v-app-bar fixed>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <router-link text class="home" router :to="{ name: 'HomePage' }"
        ><h1>
          <v-toolbar-title class="text-uppercase home">
            The Social Node
          </v-toolbar-title>
        </h1></router-link
      >
      <v-spacer />

      <v-btn id="logout" v-show="!mobile && currentUser != ''" @click="logout">
        logout
      </v-btn>
    </v-app-bar>

    <v-navigation-drawer app v-model="drawer" temporary class="lighten-5">
      <v-btn
        v-if="mobile && currentUser != ''"
        id="logout"
        class="node-headline mx-auto mt-10"
        @click="logout"
      >
        <span class="links"> logout </span></v-btn
      >
      <v-btn
        v-if="mobile && currentUser == ''"
        id="login"
        class="node-headline mx-auto mt-10"
      >
        <router-link
          class="links"
          style="color: black"
          :to="{ name: 'YourNode' }"
        >
          login
        </router-link></v-btn
      >
      <ul>
        <li>
          <router-link
            text
            style="color: black"
            class="links lighten-4"
            :to="{ name: 'YourNode' }"
          >
            <span
              v-show="currentUser == '' && !mobile"
              class="text-center mr-9 mb-4"
            >
              <br />Back to Home</span
            >
            <span v-show="currentUser != ''" class="text-center mr-7 mb-4">
              <br />Visit Your Node</span
            >
          </router-link>

          <u class="text-center ml-4">Follow the Community</u>
          <router-link
            text
            style="color: black"
            class="links lighten-4"
            :key="$route.fullPath"
            :to="'/node/' + clickedUser"
          >
            <span
              style="color: black"
              class="links lighten-4 ml-4"
              v-for="user in allUsers"
              :key="user.username"
              @click="storeUser(user.username)"
              v-show="user.username != currentUser"
            >
              {{ user.username }}'s Node
            </span>
          </router-link>
        </li>
      </ul>
    </v-navigation-drawer>
  </nav>
</template>

<script>
import AuthService from "../services/AuthService";
import SocialService from "../services/SocialService";

export default {
  name: "HeaderSection",

  data: () => ({
    drawer: false,
    mobile: false,

    allUsers: [],
    posts: [],

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

  created() {
    window.addEventListener("resize", this.onResize);
  },

  mounted() {
    this.renderComponent();
  },

  destroyed() {
    window.removeEventListener("resize", this.onResize);
  },

  methods: {
    renderComponent() {
      if (window.innerWidth < 500) {
        this.mobile = true;
      }

      this.currentUser = this.$store.state.loggedInUsername;

      SocialService.getAllUsers()
        .then((requestData) => {
          this.allUsers = requestData.data;
          this.$store.commit("STORE_ALL_USERS", this.allUsers);
        })
        .catch((err) => {
          console.error(err + " errors");
        });
    },

    logout() {
      this.posts = this.$store.state.postsInCaseOfLogOut;
      SocialService.updateRelevantPosts(this.posts);

      let user = { username: "", following: "" };

      AuthService.logout(this.currentUser)
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

    onResize(event) {
      if (window.innerWidth < 500 && event) {
        this.mobile = true;
      } else {
        this.mobile = false;
      }
    },
  },
};
</script>

<style scoped>
ul {
  list-style-type: none;
}

.links {
  display: flex;
  flex-direction: column;
  color: black;
  text-decoration: none;
  margin-top: 20px;
  margin-bottom: 20px;
}

.home {
  margin-left: 20px;
  transform: scale(1.1);
  transition: 1.3s;
  letter-spacing: 3px;
  text-shadow: 2px 2px 3px rgba(46, 110, 248, 0.41);
  text-decoration: none;
  color: black;
}

.home:hover {
  margin-left: 20px;
  transform: scale(1.1);
  transition: 1.3s;
  letter-spacing: 3px;
  text-shadow: 2px 2px 3px black;
  text-decoration: none;
  color: rgba(90, 6, 6, 0);
}
</style>