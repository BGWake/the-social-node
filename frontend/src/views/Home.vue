<template>
  <div>
    <br />
    <h1 class="home-header mt-5 mb-5">Welcome to The Social Node</h1>
    <p class="home-header mb-5">You can see what others are saying through the sidebar. Log in to try out all of the features! </p>
    <login />
  </div>
</template>

<script>
import login from "@/components/Login.vue";

export default {
  components: {
    login,
  },
};
</script>

<style>
.home-header {
  display: flex;
  justify-content: center;
  font-family: monospace;
}
</style>
