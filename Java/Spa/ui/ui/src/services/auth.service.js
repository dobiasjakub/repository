import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/';

class AuthService {
    async login(user) {
        try {
        return await axios
            .post(API_URL + 'authenticate', {
                username: user.username,
                password: user.password
            })
            .then(response => {
                if (response.data.token) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }

                return response.data;
            });
    } catch (error) {
            console.error(error.response.data);
            throw error;
        }
    }

    logout() {
        localStorage.removeItem('user');
    }

    async register(user) {
        try {
            console.log(JSON.stringify(user))

            const result = await axios.post(API_URL + 'register', {
                username: user.username,
                email: user.email,
                password: user.password
            });
            console.log(JSON.stringify(result.data));
            return result.data;
        } catch (error) {
            console.error(error.response.data);
            throw error;
        }
    }
}

export default new AuthService();