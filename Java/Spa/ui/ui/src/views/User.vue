<template>
  <div class="container">
    <header class="jumbotron">
    </header>
    <p>
      <strong>Id:</strong>
      {{ currentUser.userId }}
    </p>
    <p>
      <strong>Uživatelské jméno:</strong>
      {{currentUser.username}}
    </p>
    <p>
      <strong>Token:</strong>
      {{currentUser.token.substring(0, 20)}} ... {{currentUser.token.substr(currentUser.token.length - 20)}}
    </p>
  </div>


</template>

<script>
import UserService from "@/services/user.service";

export default {
  name: 'User',
  data() {
    return {
      content: "",
      view: null,

    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    UserService.getPublicContent().then(
        (response) => {
          this.content = response.data;
        },
        (error) => {
          this.content =
              (error.response &&
                  error.response.data &&
                  error.response.data.message) ||
              error.message ||
              error.toString();
        }
    );
  }
};
</script>