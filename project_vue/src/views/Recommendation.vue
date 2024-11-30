<template>
  <v-container fluid class="center-container">
    <!-- 동적 배경 이미지 -->
    <div
      class="background-image"
      :style="{ backgroundImage: 'url(' + currentBackgroundImage + ')' }"
    ></div>

    <!-- 상단바 -->
    <TopBar />

    <!-- 추천 카드 -->
    <div class="recommendation-card">
      <v-card class="mx-auto" elevation="1" max-width="800">
        <v-card-title class="py-5 title-container">
          <span class="board-title">동네 추천</span>
        </v-card-title>

        <v-card-text>
          <p class="intro-text">
            별점을 매기고 당신에게 어울리는 동네를 추천받아보세요!
          </p>

          <!-- 질문과 별점 -->
          <div class="questions">
            <div
              v-for="(question, index) in questions"
              :key="index"
              class="question"
            >
              <div class="question-row">
                <p class="question-text">{{ question.text }}</p>
                <div class="stars">
                  <span
                    v-for="star in 5"
                    :key="star"
                    :class="{ filled: userRatings[index] >= star }"
                    @click="rateQuestion(index, star)"
                  >
                    ★
                  </span>
                </div>
              </div>
            </div>
          </div>

          <v-btn
            class="submit-btn"
            color="primary"
            block
            @click="submitRatings"
          >
            추천받기
          </v-btn>

          <!-- 추천 결과 -->
          <div v-if="recommendation" class="result">
            <h2 class="recommendation-header">당신에게 추천하는 동네는...</h2>
            <p class="recommendation-text">{{ recommendation }}</p>
            <v-btn color="secondary" block @click="moveToMap">
              알아보러 가기
            </v-btn>
          </div>
        </v-card-text>
      </v-card>
    </div>
  </v-container>
</template>

<script>
import TopBar from "../components/TopBar.vue";

export default {
  name: "Recommendation",
  components: {
    TopBar,
  },
  data() {
    return {
      questions: [
        { text: "나는 자주 아파요" },
        { text: "나는 밥이 중요해요" },
        { text: "나는 플레이보이라 노는 게 좋아요" },
        { text: "나는 운동을 좋아해요" },
        { text: "나는 영양제나 약이 중요해요" },
      ],
      userRatings: [0, 0, 0, 0, 0], // 각 질문의 별점
      recommendation: null, // 추천 결과

      // 배경 이미지 리스트
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
    rateQuestion(index, rating) {
      this.userRatings[index] = rating; // 별점 설정
    },
    async submitRatings() {
      try {
        const response = await this.$rest.getRecommendation(this.userRatings);
        this.recommendation = response; // 추천 결과 저장
      } catch (error) {
        console.error("추천받기 실패:", error);
        alert("추천받는 중 오류가 발생했습니다.");
      }
    },
    moveToMap() {
      if (!this.recommendation) {
        alert("추천받은 동네가 없습니다. 별점을 설정해주세요!");
        return;
      }

      this.$router.push({
        name: "map",
        // query: { location: this.recommendation },
      });
    },
  },
};
</script>

<style>
/* 전체 컨테이너 */
.center-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  position: relative;
  z-index: 0;
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
  z-index: -1; /* 다른 요소 뒤로 배경 이미지 배치 */
  transition: background-image 1s ease-in-out; /* 부드러운 전환 효과 */
}

/* 추천 카드 */
.recommendation-card {
  width: 100%;
  max-width: 800px;
  z-index: 1; /* 배경보다 위 */
}

.board-title {
  font-weight: 900;
  font-size: 2rem;
  text-align: center;
  margin-bottom: 20px;
}

.intro-text {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 30px;
  text-align: center;
}

.questions {
  margin-bottom: 20px;
}

.question-row {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  justify-content: space-between;
}

.question-text {
  font-size: 1.2rem;
  font-weight: 500;
  margin-right: 10px;
  flex: 1;
}

.stars {
  display: flex;
  gap: 5px;
}

.stars span {
  cursor: pointer;
  font-size: 24px;
  color: gray;
}

.stars span.filled {
  color: gold;
}

.submit-btn {
  margin-top: 20px;
}

.result {
  margin-top: 30px;
  text-align: center;
}

.recommendation-header {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.recommendation-text {
  font-size: 1.8rem;
  font-weight: bold;
  color: #007bff;
}

/* 추가된 스타일 */
body {
  margin: 0;
  background-color: transparent; /* 배경색 투명 */
  position: relative; /* z-index가 올바르게 작동하도록 설정 */
  height: 100%; /* 전체 높이 설정 */
}

html {
  height: 100%; /* 전체 높이 설정 */
}
</style>
