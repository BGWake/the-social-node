import axios from 'axios';

export default {

  getPostsByUsername(username) {
    return axios.get(`http://localhost:8080/post/${username}`);
  },

  createPost(post) {
    return axios.post('http://localhost:8080/post/save', post);
  },

  getAllUsers() {
    return axios.get('http://localhost:8080/user/all');
  },

  follow(username) {
    return axios.post('http://localhost:8080/follow', username);
  },

  getRelevantPosts(username) {
    return axios.get(`http://localhost:8080/feed/posts/${username}`);
  },

  updateRelevantPosts(posts) {
    return axios.put('http://localhost:8080/post/all/save', posts);
  },

  getFollowing(username) {
    return axios.get(`http://localhost:8080/following/${username}`);
  },
}