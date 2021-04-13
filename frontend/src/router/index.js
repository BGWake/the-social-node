import Vue from 'vue'
import HomePage from '../views/HomePage.vue'
import LogIn from '../components/LogIn.vue'
import YourNode from '../views/YourNode.vue'
import OtherNode from '../views/OtherNode.vue'
import RegisterAccount from '../components/RegisterAccount.vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "HomePage",
      component: HomePage,
    },
    {
      path: "/login",
      name: "LogIn",
      component: LogIn,
    },
    {
      path: "/register",
      name: "RegisterAccount",
      component: RegisterAccount,
    },
    {
      path: "/node/current-user",
      name: "YourNode",
      component: YourNode,
    },
    {
      path: "/node/:name",
      name: "OtherNode",
      component: OtherNode,
    },
  ]
})

export default router;
