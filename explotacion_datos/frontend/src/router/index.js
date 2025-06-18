import { createRouter, createWebHistory } from 'vue-router';
import Home from '../components/Home.vue';
import Login from '../components/Login.vue';
import Signup from '../components/Signup.vue';
import Groups from '../components/Groups.vue';
import CreateGroup from '../components/CreateGroup.vue';
import JoinGroup from '../components/JoinGroup.vue';
import UserGroups from '@/components/UserGroups.vue';
import GroupDetails from '@/components/GroupDetails.vue';
import Recommendations from '@/components/Recomendations.vue';
import GroupChat from '@/components/GroupChat.vue';
import VotePage from '@/components/Voting.vue'; 
import ActivityGroups from '../components/ActivityGroups.vue';
import SideMenu from '@/components/SideMenu.vue';
import CreateActivity from '@/components/CreateActivity.vue';
import UserActivities from '@/components/UserActivities.vue';
import Profile from '@/components/Profile.vue';
import ActivityChat from '@/components/ActivityChat.vue';
import ActivityParticipants from '@/components/ActivityParticipants.vue';
import GroupParticipants from '@/components/GroupParticipants.vue';
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
    path: '/activities/create',
    name: 'CreateActivity',
    component: CreateActivity
  },
  {
    path: '/groups/joinWithPreferences',
    name: 'JoinGroup',
    component: JoinGroup,
    meta: { requiresAuth: false } 
  },
  {
    path: '/groups/user',
    name: 'UserGroups',
    component: UserGroups,
    meta: { requiresAuth: false } 
  },
  {
    path: '/groups/:groupName', 
    name: 'GroupDetails',
    component: GroupDetails
  },
  {
    path: '/vote/:groupId',
    name: 'Recommendations',
    component: Recommendations
  },
  {
    path: '/chat/:groupId',
    name: 'GroupChat',
    component: GroupChat
  },
   {
    path: '/activity-chat/:activityId',
    name: 'ActivityChat',
    component: ActivityChat
  },
  {
    path: '/activities',
    name: 'ActivityGroups',
    component: ActivityGroups
  },
  {
    path: '/activities/user',
    name: 'UserActivities',
    component: UserActivities
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile
  },
  {
  path: '/activity-participants/:id',
  name: 'ActivityParticipants',
  component: ActivityParticipants
  },
  {
  path: '/group-participants/:id',
  name: 'GroupParticipants',
  component: GroupParticipants
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
