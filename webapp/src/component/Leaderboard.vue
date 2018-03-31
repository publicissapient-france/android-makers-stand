<template>
  <div class="leaderboard">
    <section v-if="player.id">
      <h3>playing</h3>
      <div>
        <h1>{{player.name}}</h1>
      </div>
    </section>
    <section v-else>
      <h3>Leaderboard</h3>
      <ul>
        <li v-for="(score, index) in leaderboard" :key="score.id">
          <div class="position">{{index+1}} <span v-if="index === 0">🎉</span></div>
          <div v-if="withEmail" class="name email">{{score.email}}</div>
          <div v-else class="name">{{score.name}}</div>
          <div class="count"><img src="../asset/point.svg">{{score.count}}</div>
          <div class="time"><img src="../asset/timer.svg">{{score.time/1000}}'</div>
        </li>
      </ul>
    </section>
  </div>
</template>

<script>
  import _ from 'lodash';
  import Firebase from '../firebase';

  // noinspection JSUnusedGlobalSymbols
  export default {
    name: 'leaderboard',
    data: () => ({
      scores: [],
      player: {},
      withEmail: false,
    }),
    mounted() {
      this.withEmail = this.$route.query.withEmail;
    },
    computed: {
      leaderboard() {
        return _.sortBy(_.filter(this.scores, _.identity), [s => -s.count, s => s.time]);
      },
    },
    watch: {
      player() {
        if (this.player.id) {
          this.$router.push('/play');
        }
      },
    },
    firebase() {
      return {
        scores: Firebase.database().ref('score/').orderByChild('count').limitToLast(5),
        player: {
          source: Firebase.database().ref('player/'),
          asObject: true,
        },
      };
    },
  };
</script>

<style scoped lang="scss">
  .leaderboard {
    li {
      &:nth-of-type(1) {
        background-color: rgba(255, 215, 0, .8);
      }
      &:nth-of-type(2) {
        background-color: rgba(205, 127, 50, .8);
      }
      &:nth-of-type(3) {
        background-color: rgba(192, 192, 192, .8);
      }
      background-color: rgba(255, 255, 255, .2);
      border-radius: 3px;
      margin: 20px 5%;
      padding: 15px;
      display: flex;
      width: 85%;
      max-width: 800px;
      .name {
        text-align: left;
        flex-grow: 1;
        text-transform: capitalize;
        max-width: 400px;
        overflow: hidden;

        &.email {
          font-size: 30px;
          line-height: 70px;
        }
      }
      .position, .count, .time {
        width: 20%;
        line-height: 70px;
      }
      .count, .time {
        img {
          margin-bottom: 0;
          margin-right: 10px;
          height: 40px;
        }
      }
    }
  }
</style>
