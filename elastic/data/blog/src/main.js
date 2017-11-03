import Vue from 'vue';
import App from './App.vue';
import VueResource from 'vue-resource';

const root = ('production' === process.env.NODE_ENV)
 ? 'http://0.0.0.0:8001'
 : 'http://127.0.0.1:8001';

Vue.use(VueResource);
Vue.http.options = {
  root,
};

console.log('root', root);

new Vue({
  el: '#app',
  render: h => h(App),
});
