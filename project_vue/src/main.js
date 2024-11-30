import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import vuetify from "./plugins/vuetify";
import { loadFonts } from "./plugins/webfontloader";
import axios from "axios";
import rest from "./assets/js/Rest";
import "@fortawesome/fontawesome-free/css/all.css";
import "@fortawesome/fontawesome-free/js/all.js";

loadFonts();

const app = createApp(App);
// createApp(App).use(router).use(vuetify).mount("#app");

//axios 기본 설정
// axios.defaults.baseURL = "http://localhost:8000";

//restAPI 호출 관련 js 전역 설정
app.config.globalProperties.$rest = rest;

app.use(router).use(vuetify).mount("#app");


import { useKakao } from 'vue3-kakao-maps/@utils';

useKakao('288f1c76fd5407fd8e826a313a540e93');