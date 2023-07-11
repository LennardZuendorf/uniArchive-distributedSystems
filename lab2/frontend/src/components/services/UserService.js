import axios from 'axios'

export default class UserService {

    getUsers() {
        return axios.get('http://localhost:8080/api/v2/users')
            .then((response) => response.data)
    }
    createUser(name, email) {
        return axios
            .post('http://localhost:8080/api/v2/users', {
                user_name: name,
                user_email: email
            })
            .then((response) => console.log(response))
    }

    updateUser(id, name, email) {
        return axios
            .patch('http://localhost:8080/api/v2/users/' + id, {
                user_name: name,
                user_email: email
            })
            .then((response) => console.log(response))
    }

    deleteUser(id) {
        return axios
            .delete('http://localhost:8080/api/v2/users/' + id)
            .then((response) => console.log(response))
    }
}