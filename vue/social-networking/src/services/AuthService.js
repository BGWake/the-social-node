import axios from 'axios';

export default {

  register(user) {
    return axios.post('http://localhost:8080/register', user);
  },

  login(user) {
    return axios.post('http://localhost:8080/login', user);
  },

  logout(username) {
    return axios.post('http://localhost:8080/logoutUser', username);
  },

}
