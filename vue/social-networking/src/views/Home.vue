<template>
  <div>
    <br />
    <h1 class="home-header ma-4">Welcome to Tweeter</h1>
    <login />
  </div>
</template>

<script>
import Login from "@/components/Login.vue";

export default {
  components: {
    Login,
  },
};
</script>

<style>
.home-header {
  display: flex;
  justify-content: center;
}
</style>
