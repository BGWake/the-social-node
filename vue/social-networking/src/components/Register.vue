<template>
  <v-container>
    <br />
    <validation-observer ref="observer" v-slot="{ invalid }">
      <v-card
        outlined
        elevation="6"
        style="margin: 30px; width: 50%"
        class="mx-auto pa-4 teal lighten-4"
      >
        <h2>Register A New Account</h2>
        <v-spacer></v-spacer>
        <v-form class="ma-2" @submit.prevent="submit">
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
              prepend-inner-icon="mdi-human-greeting"
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
              label="Password"
              type="Password"
              required
              prepend-inner-icon="mdi-lock"
              append-icon="mdi-eye-off"
            ></v-text-field>
          </validation-provider>

          <validation-provider
            v-slot="{ errors }"
            name="confirm password"
            rules="required|min:4|max:20"
          >
            <v-text-field
              v-model="user.confirmPassword"
              :counter="20"
              :error-messages="errors"
              label="Confirm Password"
              type="Password"
              required
              prepend-inner-icon="mdi-lock-alert-outline"
              append-icon="mdi-eye-off"
            ></v-text-field>
          </validation-provider>

          <validation-provider
            v-slot="{ errors }"
            name="link a profile picture"
            rules="min:8|max:100"
          >
            <v-text-field
              v-model="user.profilePic"
              :counter="100"
              :error-messages="errors"
              label="Link A Profile Picture (Optional)"
              prepend-inner-icon="mdi-camera"
            ></v-text-field>
          </validation-provider>
          <br />
          <div id="login" class="text-center">
            <v-btn v-on:click="register" class="mr-4" :disabled="invalid">
              submit
            </v-btn>
            <v-btn @click="clear"> clear </v-btn>
          </div>
        </v-form>
      </v-card>
    </validation-observer>
    <div class="timeline-header" v-show="registrationErrors">
      {{ registrationErrorMsg }}
    </div>
    <div class="timeline-header" v-show="userAlreadyExists">
      {{ userAlreadyExistsMsg }}
    </div>
  </v-container>
</template>

<script>
import authService from "../services/AuthService";
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
  name: "register",
  components: {
    ValidationProvider,
    ValidationObserver,
  },

  data: () => ({
    user: {
      username: "",
      password: "",
      confirmPassword: "",
      following: "",
      profilePic: "",
    },
    registrationErrors: false,
    registrationErrorMsg: "Password and Confirm Password do not match.",
    userAlreadyExists: false,
    userAlreadyExistsMsg: "Sorry, that username already exists.",
  }),

  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: "/login",
                query: { registration: "success" },
              });
            }
          })
          .catch((err) => {
            this.userAlreadyExists = true;
            console.error(err + " errors");
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
      this.user.profilePic = "";

      this.$refs.observer.reset();
    },
  },
};
</script>

<style>
</style>