<template>
  <div id="app">
    <h1 @click="doIt()">{{ msg }}</h1>
    <pre>{{ users }}</pre>
  </div>
</template>

<script>
  import Vue from 'vue';

  export default {
    name: 'app',
    data() {
      return {
        msg: 'Welcome to Your Vue.js blog!',
        users: {},
      }
    },
    methods: {
      doIt() {
        return {
          res: Vue.http.get(`${Vue.http.options.root}/users`)
            .then(data => {
              this.users = data;
            })
        }
      }
    }
  }
</script>

<style lang="scss">
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    margin: 0;
    padding: 0;
  }

  h1, h2, h3 {
    font-weight: normal;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }
</style>
