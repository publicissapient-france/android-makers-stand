import Vue from 'vue';
import Router from 'vue-router';
import Leaderboard from '@/component/Leaderboard';
import Play from '@/component/Play';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      component: Leaderboard,
    },
    {
      path: '/play',
      component: Play,
    },
  ],
});
