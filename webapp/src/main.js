import Vue from 'vue';
import App from './App';
import Router from './router';
import Firebase from './firebase';

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router: Router,
  components: {
    App,
  },
  template: '<App/>',
  beforeCreate() {
    Firebase.auth().onAuthStateChanged((user) => {
      if (user && user.email.endsWith('xebia.fr')) {
        this.$router.push(this.$route.fullPath);
      } else if (user) {
        user.delete().then(() => {
          Firebase.auth().signOut().then(() => {
            this.$router.push('/auth');
          });
        });
      } else {
        this.$router.push('/auth');
      }
    });
  },
});
