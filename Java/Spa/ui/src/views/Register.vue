<template>
  <div class="bg-light min-vh-100 d-flex flex-row align-items-center">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-lg-8">
          <div class="card-group d-block d-md-flex row">
            <div class="card col-md-7 p-4 mb-0">
              <div class="card-body">
                <h1>Sign Up</h1>
                <p class="text-medium-emphasis">Register a new account</p>
                <Form @submit="handleRegister" :validation-schema="schema">
                  <div v-if="!successful">
                    <div class="input-group mb-3">
                      <span class="input-group-text">
                        <i class="fa-solid fa-user"></i>
                      </span>
                      <Field name="username" type="text" class="form-control" placeholder="Username" />
                      <ErrorMessage name="username" class="error-feedback" />
                    </div>
                    <div class="input-group mb-3">
                      <span class="input-group-text">
                        <i class="fa-solid fa-envelope"></i>
                      </span>
                      <Field name="email" type="email" class="form-control" placeholder="Email" />
                      <ErrorMessage name="email" class="error-feedback" />
                    </div>
                    <div class="input-group mb-3">
                      <span class="input-group-text">
                        <i class="fa-solid fa-lock"></i>
                      </span>
                      <Field name="password" type="password" class="form-control" placeholder="Password" />
                      <ErrorMessage name="password" class="error-feedback" />
                    </div>
                    <div class="row">
                      <div class="col-12">
                        <button class="btn btn-primary px-4" :disabled="loading">
                          <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                          Sign Up
                        </button>
                      </div>
                    </div>
                  </div>
                </Form>
                <div v-if="message" class="alert" :class="successful ? 'alert-success' : 'alert-danger'">
                  {{ message }}
                </div>
              </div>
            </div>
            <div class="card col-md-5 text-white bg-primary py-5">
              <div class="card-body text-center">
                <div>
                  <h2>Welcome</h2>
                  <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                  <button class="btn btn-lg btn-outline-light mt-3" type="button">Learn More</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";

export default {
  name: "Register",
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    const schema = yup.object().shape({
      username: yup
          .string()
          .required("Username is required!")
          .min(3, "Must be at least 3 characters!")
          .max(20, "Must be maximum 20 characters!"),
      email: yup
          .string()
          .required("Email is required!")
          .email("Email is invalid!")
          .max(50, "Must be maximum 50 characters!"),
      password: yup
          .string()
          .required("Password is required!")
          .min(5, "Must be at least 6 characters!")
          .max(40, "Must be maximum 40 characters!"),
    });

    return {
      successful: false,
      loading: false,
      message: "",
      schema,
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  mounted() {
  },
  methods: {
    handleRegister(user) {
      this.message = "";
      this.successful = false;
      this.loading = true;

      this.$store.dispatch("auth/register", user).then(
          (data) => {
            this.message = data;
            this.successful = true;
            this.loading = false;
          },
          (error) => {
            this.message =
                (error.response &&
                    error.response.data &&
                    error.response.data.message) ||
                error.message ||
                error.toString();
            this.successful = false;
            this.loading = false;
          }
      );
    },
  },
};
</script>

<style scoped>

</style>