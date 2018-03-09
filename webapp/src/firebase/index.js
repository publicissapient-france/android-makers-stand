import Vue from 'vue';
import VueFire from 'vuefire';
import Firebase from 'firebase';

Vue.use(VueFire);

Firebase.initializeApp(process.env.FIREBASE_CONF);

export default Firebase;
