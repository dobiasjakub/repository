<template>
   <body class="sidebar-collapse" style="height: auto">
    <nav class="main-header navbar navbar-expand navbar-white navbar-light">
      <div style="margin-left: 20px" class="navbar-brand">SPA</div>
      <router-link to="/dashboard" class="nav-link"><font-awesome-icon icon="home"/> Home </router-link>
      <router-link v-if="!currentUser" to="/login" class="nav-link">Login</router-link>
      <router-link v-if="!currentUser" to="/register" class="nav-link">Register</router-link>

      <ul class="navbar-nav ml-auto" style="justify-content: center">

          <div v-if="currentUser" class="navbar-nav ml-auto">
          <li class="nav-item">
            <router-link to="/user" class="nav-link">
              <font-awesome-icon icon="user" />
              {{ currentUser?.username }}
            </router-link>
          </li>
          <li class="nav-item">
            <a class="nav-link" @click.prevent="logOut">
              <font-awesome-icon icon="sign-out-alt" /> LogOut
            </a>
          </li>
        </div>
      </ul>
    </nav>
    <div class="container-fluid" style="min-height: 1000%;">
      <router-view />
    </div>
  </body>

</template>

<script>
import '@fortawesome/fontawesome-free/css/all.css';

export default {
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    showAdminBoard() {
      if (this.currentUser && this.currentUser['roles']) {
        return this.currentUser['roles'].includes('ROLE_ADMIN');
      }

      return false;
    },
    showModeratorBoard() {
      if (this.currentUser && this.currentUser['roles']) {
        return this.currentUser['roles'].includes('ROLE_MODERATOR');
      }

      return false;
    }
  },
  methods: {
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    }
  }
};
</script>