import { createStore } from "vuex";
import { auth } from "./modules/auth";
import { account } from "./modules/account";
import trips from "./modules/trips";



const store = createStore({
    modules: {
        auth, account, trips
    },

});

export default store;