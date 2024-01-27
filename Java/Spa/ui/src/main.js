import { createApp } from "vue";
import App from "./App.vue";
import router from "./router/router";
import store from "./store";
import { FontAwesomeIcon } from './plugins/font-awesome'
import '../src/assets/css/styles.scss';
import 'bootstrap/scss/bootstrap.scss';
import 'admin-lte/dist/css/adminlte.min.css';
import 'admin-lte/dist/js/adminlte.min.js';

createApp(App)
    .use(router)
    .use(store)
    .component("font-awesome-icon", FontAwesomeIcon)
    .mount("#app");

