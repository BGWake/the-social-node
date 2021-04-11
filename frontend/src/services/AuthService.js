import axios from 'axios';

const base = "http://localhost:8080";

// const base = "https://the-social-node.herokuapp.com";

export default {

  register(user) {
    return axios.post(`${base}/register`, user);
  },

  login(user) {
    return axios.post(`${base}/login`, user);
  },

  logout(username) {
    return axios.post(`${base}/logoutUser`, username);
  },
  
}
