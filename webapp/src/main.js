import Vue from 'vue';
import App from './App';
import Router from './router';

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router: Router,
  components: {
    App,
  },
  template: '<App/>',
});
