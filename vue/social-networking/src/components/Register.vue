<template>
  <v-card outlined elevation="6" style="margin:30px; width: 50%" class="mx-auto pa-4 teal lighten-4"> 
      <h2> Register A New Account </h2>
      <v-spacer></v-spacer>
    <validation-observer ref="observer" v-slot="{ invalid }">
      <form class="ma-4" @submit.prevent="submit">

        <validation-provider
          v-slot="{ errors }"
          name="username"
          rules="required"
        >
          <v-text-field
            class="mx-16"
            v-model="user.username"
            :error-messages="errors"
            label="Username"
            required
            prepend-inner-icon="mdi-human-greeting"
          ></v-text-field>
        </validation-provider>

        <validation-provider
          v-slot="{ errors }"
          name="password"
          rules="required"
        >
          <v-text-field
            class="mx-16"
            v-model="user.password"
            :error-messages="errors"
            label="Password"
            type="Password"
            required
            prepend-inner-icon="mdi-lock"
          ></v-text-field>
        </validation-provider>

        <validation-provider
          v-slot="{ errors }"
          name="confirm"
          rules="required"
        >
          <v-text-field
            class="mx-16"
            v-model="user.confirmPassword"
            :error-messages="errors"
            label="Confirm Password"
            type="Password"
            required
            prepend-inner-icon="mdi-lock-alert-outline"
          ></v-text-field>
        </validation-provider>

        <div id="login" class="text-center">
          <v-btn
            v-on:click="register"
            class="mr-4"
            type="submit"
            :disabled="invalid"
          >
            submit
          </v-btn>
          <v-btn @click="clear"> clear </v-btn>
        </div>
      </form>
    </validation-observer>
  </v-card>
</template>

<script>
import authService from "../services/AuthService";
import { required } from "vee-validate/dist/rules";
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

export default {
  props: {
    icons: {
      default: '$vueitfy.icons.input'
    }
  },

  components: {
    ValidationProvider,
    ValidationObserver,
  },

  name: "register",
  data: () => ({
    user: {
      username: "",
      password: "",
      confirmPassword: "",
      following: "",
    },
    
    registrationErrors: false,
    registrationErrorMsg: "There were problems registering this user.",
  }),

  methods: {

    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = "Password & Confirm Password do not match.";
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: "/login",
                query: { registration: "success" },
              });
            } else {
              this.$router.push("/register");
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = "Bad Request: Validation Errors";
            }
          });
      }
    },

    submit() {
      this.$refs.observer.validate();
    },
    clear() {
      this.user.username = "";
      this.user.password = "";
      this.user.confirmPassword = "";

      this.$refs.observer.reset();
    },
  },
};
</script>


<style>
form {
  margin: ma-8;
}
</style>