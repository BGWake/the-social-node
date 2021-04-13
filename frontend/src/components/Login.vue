<template>
  <v-container>
    <validation-observer ref="observer" v-slot="{ invalid }">
      <v-card
        outlined
        elevation="6"
        style="margin: 30px width: 50%; max-width: 700px"
        id="cards"
        class="mx-auto pa-4"
      >
        <h2>Login</h2>
        <v-spacer></v-spacer>
        <v-form class="ma-2" @submit.prevent="login">
          <validation-provider
            v-slot="{ errors }"
            name="username"
            rules="required|max:20"
          >
            <v-text-field
              v-model="user.username"
              :counter="20"
              :error-messages="errors"
              label="Username"
              required
              prepend-inner-icon="mdi-account-circle"
            ></v-text-field>
          </validation-provider>

          <validation-provider
            v-slot="{ errors }"
            name="password"
            rules="required|min:4|max:20"
          >
            <v-text-field
              v-model="user.password"
              :counter="20"
              :error-messages="errors"
              type="Password"
              label="Password"
              required
              prepend-inner-icon="mdi-lock"
              append-icon="mdi-eye-off"
            ></v-text-field>
          </validation-provider>
          <router-link :to="{ name: 'RegisterAccount' }"
            >New user? Register here.</router-link
          >
          <br />
          <br />
          <span class="node-headline">
            <v-btn class="mx-2" :disabled="invalid" type="submit">
              submit
            </v-btn>
            <v-btn class="mx-2" @click="clear"> clear </v-btn>
          </span>
        </v-form>
      </v-card>
    </validation-observer>
    <div class="node-headline mt-7" v-show="isPasswordIncorrect">
      {{ isPasswordIncorrectMsg }}
    </div>
  </v-container>
</template>

<script>
import AuthService from "../services/AuthService";

import { required, max, min } from "vee-validate/dist/rules";
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

extend("min", {
  ...min,
  message: "{_field_} must be at least {length} characters",
});

export default {
  name: "LogIn",

  components: {
    ValidationProvider,
    ValidationObserver,
  },

  data: () => ({
    user: {
      username: "",
      password: "",
    },

    isPasswordIncorrect: false,
    isPasswordIncorrectMsg:
      "Incorrect password or account does not exist. You can register a new account with the link above.",
  }),

  methods: {
    login() {
      AuthService.login(this.user)
        .then((response) => {
          if (response.status == 201) {
            this.$store.commit("SET_LOGGED_IN_USER", this.user);
            this.$router.push("/node/current-user");
          }
        })
        .catch((err) => {
          this.isPasswordIncorrect = true;
          console.error(err + " errors");
        });
    },

    clear() {
      this.user.username = "";
      this.user.password = "";

      this.$refs.observer.reset();
    },
  },
};
</script>

<style>
</style>