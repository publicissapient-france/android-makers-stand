<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  export default {
    name: 'App',
  };
</script>

<style lang="scss">
  body {
    background: #652d90 url('./asset/bg2.jpg') no-repeat -100% center;
    background-size: cover;
    color: #ffffff;
    font-family: 'Oswald', sans-serif;
    font-size: 3em;

    h1, h2, h3 {
      color: #fbb03b;
      text-transform: uppercase;
    }

    ul {
      margin: 0;
      padding: 0;
      list-style: none;
    }
  }
</style>
