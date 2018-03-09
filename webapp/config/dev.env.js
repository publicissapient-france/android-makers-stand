'use strict';
const merge = require('webpack-merge');
const prodEnv = require('./prod.env');

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  FIREBASE_CONF: JSON.stringify({
    apiKey: "AIzaSyA9DoFlwXCj_edYgjMTI1JYan3r6hMLUuY",
    authDomain: "android-makers-stand.firebaseapp.com",
    databaseURL: "https://android-makers-stand.firebaseio.com",
    projectId: "android-makers-stand",
    storageBucket: "android-makers-stand.appspot.com",
    messagingSenderId: "269643408137"
  }),
});
