<template>
  <v-container>
    <br />
    <validation-observer ref="observer" v-slot="{ invalid }">
      <v-card
        outlined
        elevation="6"
        style="margin: 30px width: 50%; max-width: 700px"
        id="cards"
        class="mx-auto pa-4 mt-5"
      >
        <h2>Register An Account</h2>
        <v-spacer></v-spacer>
        <v-form class="ma-2" @submit.prevent="register">
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
              label="Confirm"
              type="Password"
              required
              prepend-inner-icon="mdi-lock-alert-outline"
              append-icon="mdi-eye-off"
            ></v-text-field>
          </validation-provider>

          <validation-provider
            v-slot="{ errors }"
            name="link a picture"
            rules="min:8|max:250"
          >
            <v-text-field
              v-model="user.profilePic"
              :counter="250"
              :error-messages="errors"
              label="Link to a Picture (Optional)"
              prepend-inner-icon="mdi-camera"
            ></v-text-field>
          </validation-provider>
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
    <div class="node-headline mt-4" v-show="registrationErrors">
      {{ registrationErrorMsg }}
    </div>
    <br />
    <div class="node-headline mt-4" v-show="userAlreadyExists">
      {{ userAlreadyExistsMsg }}
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
  name: 'RegisterAccount',

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
        AuthService
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