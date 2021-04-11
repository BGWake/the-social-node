import axios from 'axios';

const base = "http://localhost:8080";

// const base = "https://the-social-node.herokuapp.com";

export default {

  getPostsByUsername(username) {
    return axios.get(`${base}/post/${username}`);
  },

  createPost(post) {
    return axios.post(`${base}/post/save`, post);
  },

  getAllUsers() {
    return axios.get(`${base}/user/all`);
  },

  follow(username) {
    return axios.post(`${base}/follow`, username);
  },

  unfollow(username) {
    return axios.put(`${base}/unfollow`, username);
  },

  getRelevantPosts(username) {
    return axios.get(`${base}/posts/${username}`);
  },

  updateRelevantPosts(posts) {
    return axios.put(`${base}/post/all/save`, posts);
  },

  getFollowing(username) {
    return axios.get(`${base}/following/${username}`);
  },
}