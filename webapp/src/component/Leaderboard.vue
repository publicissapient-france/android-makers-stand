<template>
  <div class="leaderboard">
    <h2>Simon Game</h2>
    <section v-if="player.id">
      <h3>{{player.name}} playing</h3>
      <h1>{{player.count}}</h1>
    </section>
    <section v-else>
      <h3>Leaderboard</h3>
      <ul>
        <li v-for="score in leaderboard" :key="score.id">
          <div>{{score.name}}</div>
          <div>{{score.count}}</div>
          <div>{{score.time/1000}}'</div>
        </li>
      </ul>
    </section>
  </div>
</template>

<script>
  import _ from 'lodash';
  import Firebase from '../firebase';

  export default {
    name: 'leaderboard',
    data: () => ({
      scores: [],
      player: {},
    }),
    computed: {
      leaderboard() {
        return _.sortBy(_.filter(this.scores, _.identity), [s => -s.count, s => s.time]);
      },
    },
    firebase() {
      return {
        scores: Firebase.database().ref('score/').orderByChild('count').limitToLast(10),
        player: {
          source: Firebase.database().ref('player/'),
          asObject: true,
        },
      };
    },
  };
</script>

<style scoped lang="scss">
</style>
