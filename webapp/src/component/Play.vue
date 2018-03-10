<template>
  <div class="play">
    <h3>playing</h3>
    <div class="player">
      <h4 class="name">{{player.name}}</h4>
      <h1 class="count">{{player.count}}</h1>
      <p class="time"><img src="../asset/timer.svg">{{elapsedTime}}'</p>
    </div>
  </div>
</template>

<script>
  import Firebase from '../firebase';

  export default {
    name: 'play',
    data: () => ({
      player: {},
    }),
    watch: {
      player() {
        if (this.player.id === undefined) {
          this.$router.push('/');
        }
      },
    },
    computed: {
      elapsedTime() {
        return Math.floor((new Date().getTime() - this.player.start) / 1000);
      },
    },
    firebase() {
      return {
        player: {
          source: Firebase.database().ref('player/'),
          asObject: true,
        },
      };
    },
  };
</script>

<style scoped lang="scss">
  .play {
  }

  .name {
    text-transform: capitalize;
  }

  .player {
    margin: 20px 5%;
    width: 85%;
    max-width: 800px;

    h1 {
      font-size: 3em;
    }
  }
</style>
