<template>
  <v-app class="grey lighten-4">
    <link href="https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/@mdi/font@4.x/css/materialdesignicons.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vuetify@2.x/dist/vuetify.min.css" rel="stylesheet">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui">
    <div id="app">
      <div class="body">
      <header-section />
      <router-view />
      </div>
    </div>
  </v-app>
</template>
<script>
import headerSection from './components/HeaderSection';

export default {
  components: { 
    headerSection,
    },
}
</script>

<style>
.body {
  padding: 60px;
}

h2 {
  display: flex;
  justify-content: center;
}
</style>