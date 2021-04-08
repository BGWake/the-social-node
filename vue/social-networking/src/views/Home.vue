<template>
  <div>
    <br />
    <h1 class="home-header ma-4">Welcome to The Social Node</h1>
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
