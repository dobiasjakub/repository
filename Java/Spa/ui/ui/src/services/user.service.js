
import client from '../../http.common.js';

const API_URL = 'http://localhost:8080/api/users/';

class UserService {
    getPublicContent() {
        return client.get(API_URL + 1 );
    }
}

export default new UserService();