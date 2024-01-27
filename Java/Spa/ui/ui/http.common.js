import axios from 'axios';
import router from "@/router/router";

const API_URL = 'http://localhost:8080/api/';

const client = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-type': 'application/json'
    }
});

client.interceptors.request.use(config => {
    const user = JSON.parse(localStorage.getItem('user'));

    if (user && user.token) {
        config.headers.Authorization = `Bearer ${user.token}`;
    }

    return config;
});

client.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.status === 403) {
            router.push('/login');
        }
        return Promise.reject(error);
    }
);

export default client;