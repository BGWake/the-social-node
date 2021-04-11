import Vue from 'vue'
import Home from '../views/Home.vue'
import Login from '../components/Login.vue'
import YourFeed from '../views/YourFeed.vue'
import OtherFeed from '../views/OtherFeed.vue'
import Register from '../components/Register.vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/login",
      name: "login",
      component: Login,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: Register,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/your-feed",
      name: "your-feed",
      component: YourFeed,
    },
    {
      path: "/feed/:name",
      name: "feed",
      component: OtherFeed,
    },
  ]
})

export default router;
