<template>
  <v-app class="grey lighten-4">
    <div id="app">
      <div class="body">
        <header-section />
        <router-view />
      </div>
    </div>
  </v-app>
</template>
<script>
import headerSection from "./components/HeaderSection";

export default {
  components: {
    headerSection,
  },
};
</script>

<style>
.body {
  padding: 60px;
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
</style>