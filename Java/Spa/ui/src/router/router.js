import { createWebHistory, createRouter } from "vue-router";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Dashboard from "../views/Dashboard.vue";
import User from "../views/User.vue";
import TravelBook from "../views/TravelBook.vue";

const routes = [
    {
        path: "/login",
        component: Login,
    },
    {
        path: "/register",
        component: Register,
    },
    {
        path: "/user",
        name: "User",
        component: User,
    },
    {
        path: "/dashboard",
        name: "Dashboard",
        component: Dashboard,
    },
    {
        path: "/travel-book",
        name: "TravelBook",
        component: TravelBook,
    },
   ];


const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router