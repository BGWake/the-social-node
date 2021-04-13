<template>
  <div>
    <br />
    <h1 class="home-header mb-5 mt-5 ml-10 mr-10">Welcome to The Social Node</h1>
    <p class="home-header mb-5 ml-10 mr-10">You can see what "others" are saying through the sidebar. Log in to try out all of the features! For demo purposes, the database is currently set up to be temporary.</p>
    <log-in></log-in>
  </div>
</template>

<script>
import LogIn from "@/components/LogIn.vue";

export default {
  name:'HomePage',

  components: {
    LogIn,
  },
};
</script>

<style scoped>
.home-header {
  display: flex;
  justify-content: center;
  font-family: monospace;
}
</style>
