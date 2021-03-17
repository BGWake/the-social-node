<template>
  <nav>
    <v-app-bar dense fixed>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <router-link text class="home-link" router :to="{name: 'home'}"><h1><v-toolbar-title class="text-uppercase home-link">
        <span>Tweeter </span>
      </v-toolbar-title></h1></router-link>
      <v-spacer />
     
        <v-btn @click="logout"> logout </v-btn>
     
    </v-app-bar>


    <v-navigation-drawer
      app
      v-model="drawer"
      absolute
      temporary
      class="lighten-5"
    >
      <ul>
        <li>
            <router-link  
            text       
            style="color:black"
            class="links lighten-4"
            :to="{ name: 'your-feed' }"
            >
        Your Tweeter Feed
            </router-link>

        Follow the Community:
        <router-link  
            text       
            style="color:black"
            class="links lighten-4"
            :key="$route.fullPath"
            :to="{name:'other-feed', params:{name : clickedUser}}"
            >
            <p
            style="color:black"
            class="links lighten-4"
            v-for="username in allUsernames" :key="username"
            v-show="username != $store.state.loggedInUsername"
            v-on:click="storeUser(username)"
            >
            {{ username }}'s Feed
            </p>
            </router-link>
        </li>
      </ul>
    </v-navigation-drawer>
  </nav>
</template>

<script>
import authService from "../services/AuthService";
import socialService from '../services/SocialService';

export default {
    name: "headerSection",
  data: () => ({
      drawer: false,
      logOutButton: [
        {name: 'Log Out', route: 'logout'},
      ],
      navLinks: [
        {name: 'Your Feed', route: 'your-feed'},
      ],
      allUsernames: [],
      currentUser: "",
      clickedUser: "",
  }),

  methods: {

    logout() {
      this.currentUser = this.$store.state.loggedInUsername;

      authService
      .logout(this.currentUser)
      .then((response) => {
        if (response.status == 201) {
            console.log("Response was 201!");
            this.$store.commit("SET_LOGGED_IN_USER", '');
            this.$router.push('/');
        } else {
            console.log("Response was _NOT_ 200!");
          }
      })
      .catch((err) => {
        console.error(err + " errors");
      });
    },

    storeUser(username) {
        this.clickedUser = username;
        this.$store.commit("STORE_USER", username);
    }
  },

  mounted() {
      this.$store.commit("STORE_USER", "");

      socialService
      .getAllUsernames()
      .then((requestData) => {
        this.allUsernames = requestData.data;
      })
      .catch((err) => {
        console.error(err + " errors");
      });
    },
  };
</script>

<style>

ul {
  list-style-type: none;
  margin: 5%;
}

.links {
  display: grid;
  text-decoration: none;
  margin: 10%;
  
}

.home-link {
  text-decoration: none;
  color: black;
}
</style>