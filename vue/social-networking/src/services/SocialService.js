import axios from 'axios';

export default {

  getPostsByUsername(username) {
    return axios.get(`http://localhost:8080/post/${username}`);
  },

  createPost(post) {
    return axios.post('http://localhost:8080/post/save', post);
  },

  getAllUsernames() {
    return axios.get('http://localhost:8080/user/all');
  },

  follow(username) {
    return axios.post('http://localhost:8080/follow', username);
  },

  getRelevantPosts(username) {
    return axios.get(`http://localhost:8080/feed/posts/${username}`);
  },
}