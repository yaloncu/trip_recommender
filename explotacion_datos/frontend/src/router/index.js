import { createRouter, createWebHistory } from 'vue-router';
import Home from '../components/Home.vue';
import Login from '../components/Login.vue';
import Signup from '../components/Signup.vue';
import Groups from '../components/Groups.vue';
import CreateGroup from '../components/CreateGroup.vue';
import JoinGroup from '../components/JoinGroup.vue';
import Voting from '../components/Voting.vue';
import { auth } from '@/firebaseConfig'; 

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/signup',
    name: 'Signup',
    component: Signup
  },
  {
    path: '/groups',
    name: 'Groups',
    component: Groups,
    meta: { requiresAuth: false } 
  },
  {
    path: '/groups/create',
    name: 'CreateGroup',
    component: CreateGroup,
    meta: { requiresAuth: false } 
  },
  {
    path: '/groups/joinWithPreferences',
    name: 'JoinGroup',
    component: JoinGroup,
    meta: { requiresAuth: false } 
  },
  {
    path: '/groups/:id/voting',
    name: 'Voting',
    component: Voting,
    meta: { requiresAuth: false }
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const currentUser = auth.currentUser;
  
  if (requiresAuth && !currentUser) {
    next('/login');
  } else {
    next();
  }
});

export default router;
