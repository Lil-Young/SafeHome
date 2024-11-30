<template>
  <v-container fluid class="center-container">
    <!-- 동적 배경 이미지 -->
    <div
      class="background-image"
      :style="{ backgroundImage: 'url(' + currentBackgroundImage + ')' }"
    ></div>

    <!-- 상단바 -->
    <TopBar />

    <!-- 로그인 카드 -->
    <div class="center-card">
      <v-card class="mx-auto card-container" elevation="1">
        <!-- 제목 및 아이콘 -->
        <v-card-title class="py-5 title-container">
          <v-img
            src="@/assets/img/login-icon.png"
            max-width="80"
            class="mr-3"
          />
          <span class="board-title">로그인</span>
        </v-card-title>

        <!-- 로그인 폼 -->
        <v-card-text>
          <div class="input-label">ID</div>
          <v-text-field
            v-model="userId"
            label="ID"
            variant="outlined"
            single-line
            class="mb-4"
          ></v-text-field>

          <div class="input-label">PW</div>
          <v-text-field
            v-model="userPwd"
            label="Password"
            type="password"
            variant="outlined"
            single-line
            class="mb-5"
          ></v-text-field>

          <!-- 로그인 버튼 -->
          <v-btn
            :disabled="loading"
            :loading="loading"
            class="text-none mb-4"
            color="indigo-darken-3"
            size="x-large"
            variant="flat"
            block
            @click="handleLogin"
          >
            로그인
          </v-btn>

          <!-- 회원가입 버튼 -->
          <v-btn
            class="text-none mb-4"
            color="grey-lighten-3"
            size="x-large"
            variant="flat"
            block
            @click="goToRegister"
          >
            회원가입
          </v-btn>
        </v-card-text>
      </v-card>
    </div>
  </v-container>
</template>

<script>
import { defineComponent } from "vue";
import TopBar from "../components/TopBar.vue";

export default defineComponent({
  name: "LoginPage",
  components: { TopBar },
  data() {
    return {
      loading: false,
      userId: "",
      userPwd: "",
      // 이미지를 require()로 가져오기
      backgroundImages: [
        require("@/assets/img/background1.jpg"),
        require("@/assets/img/background2.jpg"),
        require("@/assets/img/background3.jpg"),
      ],
      currentBackgroundImage: require("@/assets/img/background1.jpg"),
      imageIndex: 0,
    };
  },
  mounted() {
    console.log("초기 배경 이미지 URL:", this.currentBackgroundImage);

    // 3초마다 배경 이미지 변경
    setInterval(() => {
      this.changeBackgroundImage();
    }, 3000);
  },
  methods: {
    changeBackgroundImage() {
      this.imageIndex = (this.imageIndex + 1) % this.backgroundImages.length;
      this.currentBackgroundImage = this.backgroundImages[this.imageIndex];
      console.log("현재 배경 이미지 URL:", this.currentBackgroundImage);
    },
    async handleLogin() {
      if (!this.userId || !this.userPwd) {
        alert("아이디와 비밀번호를 입력해주세요.");
        return;
      }

      this.loading = true;

      try {
        console.log(this.$rest);

        // 로그인 요청 후 성공하면 이동
        await this.$rest.login(this.userId, this.userPwd, this);

        // 로그인 성공 시 board로 이동
        const token = localStorage.getItem("accessToken");
        if (token) {
          window.location.href = "/board";
        } else {
          throw new Error("로그인 토큰이 저장되지 않았습니다.");
        }
      } catch (error) {
        console.error("로그인 실패:", error);
        alert("로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
      } finally {
        this.loading = false;
      }
    },
    goToRegister() {
      this.$router.push({ name: "join" });
    },
  },
});
</script>

<style>
/* 전체 컨테이너 */
.center-container {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  height: 100vh;
  overflow: hidden;
}

/* 배경 이미지 */
.background-image {
  position: fixed; /* 화면 전체에 배경 고정 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover; /* 배경 비율 유지 */
  background-position: center; /* 이미지 중앙 정렬 */
  z-index: 0; /* 다른 요소 뒤로 배경 이미지 배치 */
  transition: background-image 1s ease-in-out; /* 부드러운 전환 효과 */
}

/* 로그인 카드 */
.center-card {
  width: 100%;
  max-width: 800px;
  z-index: 1; /* 배경보다 위 */
}

.card-container {
  background: rgba(255, 255, 255, 0.8); /* 반투명 */
  border-radius: 10px;
  padding: 2rem;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.title-container {
  display: flex;
  align-items: center;
  justify-content: center;
}

.board-title {
  font-weight: 900;
  font-size: 2rem;
  text-align: center;
}
</style>
