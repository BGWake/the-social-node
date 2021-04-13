<template>
  <v-app class="grey lighten-4">
    <div id="app">
      <div class="body">
        <header-section></header-section>
        <router-view />
      </div>
    </div>
  </v-app>
</template>

<script>
import HeaderSection from "./components/HeaderSection";

export default {
  components: {
    HeaderSection,
  },
};
</script>

<style>
.body {
  margin-top: 50px;
}

h2 {
  display: flex;
  justify-content: center;
  font-family: monospace;
}

ul {
  font-family: monospace;
}

#login {
  font-family: monospace;
}

#register {
  font-family: monospace;
}

#logout {
  font-family: monospace;
}

.node-headline {
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

#quicklink {
  text-decoration: none;
  color: black;
}
</style>