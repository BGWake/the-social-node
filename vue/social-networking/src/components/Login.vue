<template>
<v-container>
   
  <validation-observer ref="observer" v-slot="{ invalid }">
     
    <v-card outlined elevation="6" style="margin:30px; width: 50%" class="mx-auto pa-4 teal lighten-4"> 
      <h2> Log In </h2>
      <v-spacer></v-spacer>
      <validation-provider 
        v-slot="{ errors }"
        name="username"
        rules="required|max:50"
      >
        <v-text-field
          v-model="user.username"
          :counter="50"
          :error-messages="errors"
          label="Username"
          required
          prepend-inner-icon="mdi-account-circle"
        ></v-text-field>
        
      </validation-provider>

      <validation-provider
        v-slot="{ errors }"
        name="password"
        rules="required|max:200"
      >
        <v-text-field
          v-model="user.password"
          :counter="200"
          :error-messages="errors"
          type="Password"
          label="Password"
          required
          prepend-icon="mdi-lock"
          append-icon="mdi-eye-off"
        ></v-text-field>
      </validation-provider>
      <router-link :to="{ name: 'register' }">Need an account?</router-link>
      <br />
      <br />
      <div id="login" class="text-center">
        <v-btn
          v-on:click="login"
          class="mr-4"
          type="submit"
          :disabled="invalid"
        >
          submit
        </v-btn>
        <v-btn @click="clear"> clear </v-btn>
      </div>
      </v-card>
  </validation-observer>
 
</v-container>
</template>

<script>
import authService from "../services/AuthService";


import { required, max } from "vee-validate/dist/rules";
import {
  extend,
  ValidationObserver,
  ValidationProvider,
  setInteractionMode,
} from "vee-validate";

setInteractionMode("eager");

extend("required", {
  ...required,
  message: "{_field_} can not be empty",
});

extend("max", {
  ...max,
  message: "{_field_} may not be greater than {length} characters",
});

export default {
  name: "login",
  components: {
    ValidationProvider,
    ValidationObserver,
  },
  data: ()=> ({
      user: {
        username: "",
        password: "",
      },
      posts: [],
  }),

  methods: {

    login() {
      authService
      .login(this.user)
      .then((response) => {
        if (response.status == 201) {
            this.$store.commit("SET_LOGGED_IN_USER", this.user);
            this.$router.push('/your-feed');
        } else {
            this.$router.push('/');
          }
      })
      .catch((err) => {
        console.error(err + " errors");
      });
    },
  
    clear() {
      this.user.username = "";
      this.user.password = "";

      this.$refs.observer.reset();
    },
  },
}
</script>


<style>
form {
  margin: ma-8;
}
</style>