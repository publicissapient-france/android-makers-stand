import Vue from 'vue';
import Router from 'vue-router';
import Leaderboard from '@/component/Leaderboard';
import Play from '@/component/Play';
import Auth from '@/component/Auth';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/leaderboard',
    },
    {
      path: '/leaderboard',
      component: Leaderboard,
    },
    {
      path: '/play',
      component: Play,
    },
    {
      path: '/auth',
      component: Auth,
    },
  ],
});
