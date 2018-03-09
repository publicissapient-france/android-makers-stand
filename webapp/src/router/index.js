import Vue from 'vue';
import Router from 'vue-router';
import Leaderboard from '@/component/Leaderboard';

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      component: Leaderboard,
    },
  ],
});
