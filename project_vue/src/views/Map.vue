<template>
  <div class="container">
    <!-- 전체 레이아웃 -->
    <TopBar />

    <!-- 검색 옵션을 지도 위에 배치 -->
    <div class="search-toolbar">
      <!-- 지역 및 유형/거래 형태 선택 -->
      <div class="search-group">
        <!-- 지역 선택 -->
        <select class="select-box" v-model="selectedSido" @change="fetchGugun">
          <option value="" disabled hidden>시/도 선택</option>
          <option v-for="sido in sidoList" :key="sido" :value="sido">
            {{ sido }}
          </option>
        </select>

        <select
          class="select-box"
          v-model="selectedGugun"
          @change="fetchDong"
          :disabled="!selectedSido"
        >
          <option value="" disabled hidden>구/군 선택</option>
          <option v-for="gugun in gugunList" :key="gugun" :value="gugun">
            {{ gugun }}
          </option>
        </select>

        <select
          class="select-box"
          v-model="selectedDong"
          @change="moveToDong"
          :disabled="!selectedGugun"
        >
          <option value="" disabled hidden>동 선택</option>
          <option v-for="dong in dongList" :key="dong" :value="dong">
            {{ dong }}
          </option>
        </select>

        <!-- 유형 선택 -->
        <select
          class="select-box"
          v-model="propertyType"
          @change="handlePropertyTypeChange"
        >
          <option value="" disabled hidden>유형 선택</option>
          <option value="APT">아파트</option>
          <option value="VILLA">주택/빌라</option>
          <option value="OFFICETEL">오피스텔</option>
        </select>

        <!-- 거래 형태 선택 -->
        <select
          class="select-box"
          v-model="dealType"
          @change="handleDealTypeChange"
        >
          <option value="" disabled hidden>거래 형태 선택</option>
          <option value="SALE">매매</option>
          <option value="RENT">전/월세</option>
        </select>

        <!-- 가격 필터 -->
        <div v-if="dealType === 'SALE'">
          <select class="select-box" v-model="selectedPrice">
            <option value="" disabled hidden>가격 선택</option>
            <option value="[0,1000000]">전체</option>
            <option value="[0,5000]">0 ~ 5천만 원</option>
            <option value="[5000,10000]">5천만 ~ 1억 원</option>
            <option value="[10000,20000]">1억 ~ 2억 원</option>
            <option value="[20000,30000]">2억 ~ 3억 원</option>
            <option value="[30000,1000000]">3억 이상</option>
          </select>
        </div>

        <!-- 전/월세 필터 -->
        <div v-if="dealType === 'RENT'" class="rent-filters-container">
          <!-- 전/월세 선택 -->
          <div class="filter-item">
            <select
              id="rentTypeFilter"
              class="select-box"
              v-model="selectedRentType"
            >
              <option value="" disabled hidden>전/월세 선택</option>
              <option value="전세">전세</option>
              <option value="월세">월세</option>
            </select>
          </div>

          <!-- 보증금 필터 -->
          <div class="filter-item">
            <select
              id="depositFilter"
              class="select-box"
              v-model="selectedDeposit"
            >
              <option value="" disabled hidden>보증금 선택</option>
              <option value="[0,5000]">0 ~ 5천만 원</option>
              <option value="[5000,10000]">5천만 ~ 1억 원</option>
              <option value="[10000,20000]">1억 ~ 2억 원</option>
              <option value="[20000,30000]">2억 ~ 3억 원</option>
              <option value="[30000,100000]">3억 이상</option>
            </select>
          </div>

          <!-- 월세 필터 -->
          <div class="filter-item" v-if="selectedRentType === '월세'">
            <select
              id="monthlyRentFilter"
              class="select-box"
              v-model="selectedMonthlyRent"
            >
              <option value="" disabled hidden>월세 범위 선택</option>
              <option value="[0,10000]">전체</option>
              <option value="[0,10]">0 ~ 10만 원</option>
              <option value="[10,30]">10만 ~ 30만 원</option>
              <option value="[30,50]">30만 ~ 50만 원</option>
              <option value="[50,100]">50만 ~ 100만 원</option>
              <option value="[100,200]">100만 ~ 200만 원</option>
              <option value="[200,10000]">200만 이상</option>
            </select>
          </div>
        </div>

        <!-- 면적 필터 -->
        <select class="select-box" v-model="selectedArea">
          <option value="" disabled hidden>면적 선택</option>
          <option value="[0,200]">전체</option>
          <option value="[0,50]">0 ~ 50㎡</option>
          <option value="[50,100]">50 ~ 100㎡</option>
          <option value="[100,150]">100 ~ 150㎡</option>
          <option value="[150,200]">150㎡ 이상</option>
        </select>
      </div>

      <!-- 검색 버튼 -->
      <button class="search-btn" @click="fetchProperties">검색</button>
    </div>

    <!-- 전체 레이아웃 -->
    <div class="content-wrapper">
      <!-- 사이드바: 매물 상세 정보 -->
      <div class="sidebar">
        <div v-if="selectedDetails.length" class="details-container">
          <h3 class="section-title">📋 매물 상세 정보</h3>
          <ul class="details-list">
            <li
              v-for="(detail, index) in selectedDetails"
              :key="index"
              class="detail-item"
            >
              <!-- 매물 이름 -->
              <div v-if="index === 0" class="detail-title-container">
                <strong class="detail-title">
                  {{ detail.aptNm ? "🏢" : detail.mhouseNm ? "🏠" : "🛎️" }}
                  {{
                    detail.aptNm ||
                    detail.mhouseNm ||
                    detail.offiName ||
                    "매물 이름 없음"
                  }}
                </strong>
              </div>
              <!-- 매물 세부 정보 -->
              <div class="detail-info">
                <!-- 건축 날짜 -->
                <div
                  class="info-item"
                  :style="{ backgroundColor: '#005f99', color: 'white' }"
                >
                  <span class="info-label">건축 날짜:</span>
                  <span class="info-value"
                    >{{ detail.buildYear || "정보 없음" }}년</span
                  >
                </div>
                <!-- 면적 -->
                <div
                  class="info-item"
                  :style="{ backgroundColor: '#991f00', color: 'white' }"
                >
                  <span class="info-label">면적:</span>
                  <span class="info-value"
                    >{{ detail.excluUseAr || "정보 없음" }}㎡</span
                  >
                </div>
                <!-- 주소 -->
                <div
                  class="info-item"
                  :style="{ backgroundColor: '#198754', color: 'white' }"
                >
                  <span class="info-label">주소:</span>
                  <span class="info-value info-value-multiline">
                    {{ detail.fullAddress }} ({{ detail.jibun }}번지,
                    {{ detail.floor }}층)
                  </span>
                </div>
                <!-- 매매 정보 -->
                <div
                  class="info-item"
                  :style="{ backgroundColor: '#cc7000', color: 'white' }"
                >
                  <template v-if="detail.dealAmount">
                    <span class="info-label">매매:</span>
                    <span class="info-value"
                      >{{ formatPrice(detail.dealAmount) }}억</span
                    >
                  </template>
                  <template v-else-if="detail.monthlyRent === '0'">
                    <span class="info-label">전세:</span>
                    <span class="info-value"
                      >{{ formatPrice(detail.deposit) }}억</span
                    >
                  </template>
                  <template v-else>
                    <span class="info-label">보증금:</span>
                    <span class="info-value"
                      >{{ formatPrice(detail.deposit) }}억</span
                    >
                    /
                    <span class="info-label">월세:</span>
                    <span class="info-value">{{ detail.monthlyRent }}만</span>
                  </template>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>

      <!-- 지도와 카테고리 -->
      <div class="map-container">
        <!-- 지도 위 카테고리 버튼 -->
        <div class="category-toolbar">
          <button
            v-for="category in categories"
            :key="category.code"
            class="category-btn"
            @click="selectCategory(category.code)"
          >
            <div class="category-content">
              <div class="category-emoji">{{ category.emoji }}</div>
              <span>{{ category.name }}</span>
            </div>
          </button>
        </div>
        <div id="map" ref="mapContainer" class="map-placeholder"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent } from "vue";
import TopBar from "../components/TopBar.vue";
import Rest from "../assets/js/Rest.js";

export default defineComponent({
  name: "MapComponent",
  components: { TopBar },
  data() {
    return {
      propertyType: "",
      dealType: "",
      map: null,
      markers: [],
      selectedDetails: [], // 빈 배열로 초기화
      selectedSido: "",
      selectedGugun: "",
      selectedDong: "",
      sidoList: [],
      gugunList: [],
      dongList: [],
      categories: [
        { code: "MT1", name: "대형마트", emoji: "🛒" },
        { code: "CS2", name: "편의점", emoji: "🏪" },
        { code: "PS3", name: "어린이집/유치원", emoji: "🧒" },
        { code: "SC4", name: "학교", emoji: "🏫" },
        { code: "AC5", name: "학원", emoji: "📚" },
        { code: "PK6", name: "주차장", emoji: "🅿️" },
        { code: "OL7", name: "주유소/충전소", emoji: "⛽" },
        { code: "SW8", name: "지하철역", emoji: "🚇" },
        { code: "BK9", name: "은행", emoji: "🏦" },
        { code: "CT1", name: "문화시설", emoji: "🎭" },
        { code: "AG2", name: "중개업소", emoji: "💼" },
        { code: "PO3", name: "공공기관", emoji: "🏛️" },
        { code: "AT4", name: "관광명소", emoji: "🗼" },
        { code: "AD5", name: "숙박", emoji: "🛏️" },
        { code: "FD6", name: "음식점", emoji: "🍴" },
        { code: "CE7", name: "카페", emoji: "☕" },
        { code: "HP8", name: "병원", emoji: "🏥" },
        { code: "PM9", name: "약국", emoji: "💊" },
      ],
      selectedCategory: null, // 현재 선택된 카테고리

      // 새로 추가된 필터
      selectedRentType: "", // 전세 or 월세
      selectedPrice: "", // 매매 시 선택되는 가격
      selectedDeposit: "", // 보증금
      selectedMonthlyRent: "", // 월세
      selectedArea: "", // 면적 필터
    };
  },
  mounted() {
    this.initMap();
    this.fetchSido(); // 시/도 데이터 요청
  },
  methods: {
    initMap() {
      if (typeof kakao === "undefined" || !kakao.maps) {
        console.error("카카오 지도 API가 로드되지 않았습니다.");
        return;
      }

      kakao.maps.load(() => {
        const container = this.$refs.mapContainer;
        const options = {
          center: new kakao.maps.LatLng(37.5665, 126.978),
          level: 5,
        };
        this.map = new kakao.maps.Map(container, options);

        // kakao.maps.event.addListener(this.map, "dragend", this.fetchProperties);
        // kakao.maps.event.addListener(this.map, "zoom_changed", this.fetchProperties);
      });
    },

    // 카테고리 선택
    selectCategory(categoryCode) {
      this.selectedCategory = categoryCode;
      this.fetchCategoryMarkers(); // 해당 카테고리 마커를 지도에 표시
    },

    // 카테고리 검색 후 마커 표시
    async fetchCategoryMarkers() {
      if (!this.selectedCategory) {
        alert("카테고리를 선택해주세요.");
        return;
      }

      // 기존 마커와 오버레이 모두 삭제
      this.markers.forEach((marker) => {
        if (marker.overlay) {
          marker.overlay.setMap(null); // 기존 오버레이 숨기기
        }
        marker.setMap(null); // 마커 삭제
      });
      this.markers = [];

      const bounds = this.map.getBounds(); // 현재 지도 경계
      const swLat = bounds.getSouthWest().getLat();
      const swLng = bounds.getSouthWest().getLng();
      const neLat = bounds.getNorthEast().getLat();
      const neLng = bounds.getNorthEast().getLng();

      const step = 0.01; // 범위를 나누는 간격 (작을수록 더 세밀)
      const places = new kakao.maps.services.Places();
      const requests = [];

      // 지도 범위를 나누어 여러 요청 생성
      for (let lat = swLat; lat < neLat; lat += step) {
        for (let lng = swLng; lng < neLng; lng += step) {
          const boundsForSearch = new kakao.maps.LatLngBounds(
            new kakao.maps.LatLng(lat, lng),
            new kakao.maps.LatLng(lat + step, lng + step)
          );

          requests.push(
            new Promise((resolve) => {
              places.categorySearch(
                this.selectedCategory,
                (data, status) => {
                  if (status === kakao.maps.services.Status.OK) {
                    resolve(data);
                  } else {
                    resolve([]); // 검색 실패 시 빈 결과 반환
                  }
                },
                { bounds: boundsForSearch }
              );
            })
          );
        }
      }

      // 모든 요청을 병렬로 처리
      const results = (await Promise.all(requests)).flat();
      console.log("검색된 마커 개수:", results.length);

      // 선택된 카테고리의 이모지 가져오기
      const selectedCategory = this.categories.find(
        (cat) => cat.code === this.selectedCategory
      );
      const categoryEmoji = selectedCategory ? selectedCategory.emoji : "?"; // 선택된 카테고리 이모지

      // 마커 표시
      results.forEach((place) => {
        // HTML로 이모지 마커 생성
        const emojiMarker = document.createElement("div");
        emojiMarker.style.width = "40px";
        emojiMarker.style.height = "40px";
        emojiMarker.style.backgroundColor = "white";
        emojiMarker.style.border = "1px solid #ddd";
        emojiMarker.style.borderRadius = "50%";
        emojiMarker.style.display = "flex";
        emojiMarker.style.alignItems = "center";
        emojiMarker.style.justifyContent = "center";
        emojiMarker.style.boxShadow = "0px 2px 5px rgba(0, 0, 0, 0.2)";
        emojiMarker.style.fontSize = "24px";
        emojiMarker.textContent = categoryEmoji;

        // 마커를 커스텀 오버레이로 표시
        const customOverlay = new kakao.maps.CustomOverlay({
          position: new kakao.maps.LatLng(place.y, place.x),
          content: emojiMarker, // 이모지 마커를 HTML content로 설정
          yAnchor: 1,
        });

        // 오버레이 내용 생성
        const overlayContent = `
      <div class="placeinfo">
        <a class="title" href="${place.place_url}" target="_blank" title="${
          place.place_name
        }">
          ${place.place_name}
        </a></br>
        ${
          place.road_address_name
            ? `
          <span title="${place.road_address_name}">${place.road_address_name}</span></br>
          <span class="jibun" title="${place.address_name}">
            (지번 : ${place.address_name})
          </span>`
            : `
          <span title="${place.address_name}">${place.address_name}</span>`
        }</br>
        <span class="tel">${place.phone || "전화번호 없음"}</span></br>
      </div>
      <div class="after"></div>
    `;

        const infoOverlay = new kakao.maps.CustomOverlay({
          position: new kakao.maps.LatLng(place.y, place.x),
          content: overlayContent,
          yAnchor: 1.5,
        });

        // 클릭 이벤트 등록
        emojiMarker.addEventListener("click", () => {
          console.log(`Clicked Marker Position: ${place.place_name}`);
          console.log(`Marker State Before Click: ${customOverlay.isOpen}`);

          if (customOverlay.isOpen) {
            infoOverlay.setMap(null); // 정보 오버레이 숨기기
            customOverlay.isOpen = false;
          } else {
            // 기존 오버레이 닫기
            this.markers.forEach((m) => {
              if (m.overlay) {
                m.overlay.setMap(null);
                m.isOpen = false;
              }
            });

            infoOverlay.setMap(this.map); // 정보 오버레이 표시
            customOverlay.isOpen = true;
          }

          console.log(`Marker State After Click: ${customOverlay.isOpen}`);
        });

        // 마커와 오버레이 객체를 저장
        customOverlay.overlay = infoOverlay;
        customOverlay.setMap(this.map); // 지도에 표시
        this.markers.push(customOverlay);
      });

      console.log("모든 마커가 생성되었습니다.");
    },

    groupByLocation(properties) {
      this.groupedLocations = {};

      properties.forEach((property) => {
        const key = `${property.latitude},${property.longitude}`;
        if (!this.groupedLocations[key]) {
          this.groupedLocations[key] = [];
        }
        this.groupedLocations[key].push(property);
      });
    },
    // 유형 선택 시 동 선택 여부 확인
    handlePropertyTypeChange() {
      if (!this.selectedDong) {
        alert("먼저 동을 선택해주세요.");
        this.propertyType = ""; // 선택 취소
      }
    },

    // 거래 형태 선택 시 동 선택 여부 확인
    handleDealTypeChange() {
      if (!this.selectedDong) {
        alert("먼저 동을 선택해주세요.");
        this.dealType = ""; // 선택 취소
      }
    },
    async fetchProperties() {
      // 거래 형태와 유형 선택 확인
      if (!this.propertyType || !this.dealType) {
        alert("유형과 거래 형태를 선택해주세요.");
        return;
      }

      // 매매일 경우 가격 필터 확인
      if (this.dealType === "SALE" && !this.selectedPrice) {
        alert("매매 가격 범위를 선택해주세요.");
        return;
      }

      // 전/월세일 경우 보증금 및 월세 필터 확인
      if (this.dealType === "RENT") {
        if (!this.selectedDeposit) {
          alert("보증금 범위를 선택해주세요.");
          return;
        }
        if (this.selectedRentType === "월세" && !this.selectedMonthlyRent) {
          alert("월세 범위를 선택해주세요.");
          return;
        }
      }

      // 선택된 필터 출력
      console.log("선택된 필터:", {
        dealType: this.dealType,
        propertyType: this.propertyType,
        area: this.selectedArea ? JSON.parse(this.selectedArea) : null,
        price: this.dealType === "SALE" ? JSON.parse(this.selectedPrice) : null,
        deposit:
          this.dealType === "RENT" ? JSON.parse(this.selectedDeposit) : null,
        monthlyRent:
          this.selectedRentType === "월세"
            ? JSON.parse(this.selectedMonthlyRent)
            : null,
      });

      if (!kakao || !kakao.maps || !kakao.maps.services) {
        console.error("카카오 지도 API가 로드되지 않았습니다.");
        return;
      }

      // 기존 카테고리 마커와 오버레이 제거
      this.markers.forEach((marker) => {
        if (marker.overlay) {
          marker.overlay.setMap(null); // 오버레이 숨기기
        }
        marker.setMap(null); // 마커 숨기기
      });
      this.markers = []; // 초기화

      this.selectedCategory = null; // 현재 선택된 카테고리 초기화

      // 현재 지도 레벨 확인
      const currentLevel = this.map.getLevel();
      const maxLevel = 7; // 확대된 상태일 때만 호출
      const minLevel = 2; // 너무 세부적으로 확대된 상태는 제외
      if (currentLevel > maxLevel || currentLevel < minLevel) {
        console.log(
          `현재 레벨(${currentLevel})은 데이터 호출 조건에 맞지 않습니다.`
        );
        return;
      }

      const geocoder = new kakao.maps.services.Geocoder();
      const bounds = this.map.getBounds();
      const swLat = bounds.getSouthWest().getLat();
      const swLng = bounds.getSouthWest().getLng();
      const neLat = bounds.getNorthEast().getLat();
      const neLng = bounds.getNorthEast().getLng();

      console.log("현재 지도 경계:", { swLat, swLng, neLat, neLng });

      const step = 0.005; // 좌표 간격 설정
      const dongSet = new Set(); // 중복 방지를 위해 Set 사용

      const center = this.map.getCenter(); // 지도 중심 좌표
      const range = 0.01; // 중심에서 범위 제한
      try {
        // 지도 경계를 순회하며 동 정보 수집
        for (
          let lat = center.getLat() - range;
          lat <= center.getLat() + range;
          lat += step
        ) {
          for (
            let lng = center.getLng() - range;
            lng <= center.getLng() + range;
            lng += step
          ) {
            const result = await new Promise((resolve, reject) => {
              geocoder.coord2RegionCode(lng, lat, (res, status) => {
                if (status === kakao.maps.services.Status.OK) {
                  resolve(res.filter((item) => item.region_type === "H"));
                } else {
                  reject(`RegionCode 실패: ${status}`);
                }
              });
            });

            result.forEach((region) => {
              dongSet.add(region.region_3depth_name);
            });
          }
        }

        const dongList = Array.from(dongSet);
        console.log("추출된 '동' 리스트:", dongList);

        const filters = {
          area: this.selectedArea ? JSON.parse(this.selectedArea) : null,
          price:
            this.dealType === "SALE" ? JSON.parse(this.selectedPrice) : null,
          deposit:
            this.dealType === "RENT" ? JSON.parse(this.selectedDeposit) : null,
          monthlyRent:
            this.dealType === "RENT" && this.selectedRentType === "월세"
              ? JSON.parse(this.selectedMonthlyRent)
              : null, // 전세인 경우 null
        };

        // 서버로 요청
        const properties = await Rest.getProperties(
          this.propertyType,
          this.dealType,
          dongList.map((dong) => ({ umdNm: dong })),
          filters.area,
          filters.price,
          filters.deposit,
          filters.monthlyRent
        );

        console.log("응답받은 매물 데이터:", properties);

        // `properties`를 `addMarkers`에 전달
        this.groupByLocation(properties);
        this.addMarkers(properties);
      } catch (error) {
        console.error("데이터 가져오기 실패:", error);
      }
    },

    async addMarkers(properties) {
      // 기존 마커 삭제
      this.markers.forEach((marker) => marker.setMap(null));
      this.markers = [];

      const geocoder = new kakao.maps.services.Geocoder();
      const groupedLocations = {};

      for (const property of properties) {
        if (!property.umdNm || !property.jibun) {
          console.warn("유효하지 않은 주소 데이터:", property);
          continue;
        }

        const address = `${property.umdNm} ${property.jibun}`;
        console.log("지오코딩 요청 주소:", address); // 디버깅용 로그

        try {
          // 지오코딩 요청
          const position = await new Promise((resolve, reject) => {
            geocoder.addressSearch(address, (result, status) => {
              if (
                status === kakao.maps.services.Status.OK &&
                result.length > 0
              ) {
                resolve({
                  latitude: result[0].y,
                  longitude: result[0].x,
                });
              } else {
                console.error(
                  `지오코딩 실패 - 주소: ${address}, 상태: ${status}`
                );
                reject(`지오코딩 실패: ${status}`);
              }
            });
          });

          // 같은 위치 마커 그룹화
          const key = `${position.latitude},${position.longitude}`;
          if (!groupedLocations[key]) {
            groupedLocations[key] = {
              position: new kakao.maps.LatLng(
                position.latitude,
                position.longitude
              ),
              properties: [],
            };
          }
          groupedLocations[key].properties.push(property);
        } catch (error) {
          console.error("지오코딩 에러:", error);
        }
      }

      for (const [key, group] of Object.entries(groupedLocations)) {
        const { position, properties } = group;

        // **기존 markerContent 유지**
        const firstProperty = properties[0];
        let markerContent = "";

        if (firstProperty.dealAmount) {
          markerContent = `
          <div style="background-color: #8a4af3; color: white; padding: 5px 10px; border-radius: 5px; text-align: center; font-size: 12px;">
            <strong>매매 ${this.formatPrice(
              firstProperty.dealAmount
            )}억</strong><br>
            ${firstProperty.excluUseAr ? `${firstProperty.excluUseAr}㎡` : ""}
          </div>`;
        } else if (firstProperty.deposit && firstProperty.monthlyRent != 0) {
          markerContent = `
          <div style="background-color: #ff7b00; color: white; padding: 5px 10px; border-radius: 5px; text-align: center; font-size: 12px;">
            <strong>보증금 ${this.formatPrice(firstProperty.deposit)} / 월세 ${
            firstProperty.monthlyRent
          }만</strong><br>
            ${firstProperty.excluUseAr ? `${firstProperty.excluUseAr}㎡` : ""}
          </div>`;
        } else if (firstProperty.deposit) {
          markerContent = `
          <div style="background-color: #00a8ff; color: white; padding: 5px 10px; border-radius: 5px; text-align: center; font-size: 12px;">
            <strong>전세 ${this.formatPrice(
              firstProperty.deposit
            )}억</strong><br>
            ${firstProperty.excluUseAr ? `${firstProperty.excluUseAr}㎡` : ""}
          </div>`;
        }

        // CustomOverlay 생성
        const customOverlay = new kakao.maps.CustomOverlay({
          position: position,
          content: markerContent,
          yAnchor: 1,
        });

        // 이벤트 등록
        const overlayDiv = document.createElement("div");
        overlayDiv.innerHTML = markerContent;
        overlayDiv.style.cursor = "pointer"; // 클릭 가능 표시
        overlayDiv.addEventListener("click", async () => {
          console.log("클릭된 위치의 매물 데이터:", properties);
          try {
            const locationDetails = await Rest.getLocationByDong(
              firstProperty.sggCd,
              firstProperty.umdNm
            );
            const fullAddress = `${locationDetails.sidoName} ${locationDetails.gugunName} ${firstProperty.umdNm}`;
            console.log("상세 주소:", fullAddress);

            // 상세 매물 정보에 주소 포함
            this.selectedDetails = properties.map((property) => ({
              ...property,
              fullAddress: fullAddress,
            }));
          } catch (error) {
            console.error("주소 상세 정보 가져오기 실패:", error);
          }
        });

        customOverlay.setContent(overlayDiv); // CustomOverlay의 content로 설정
        customOverlay.setMap(this.map); // Overlay를 지도에 추가

        // 마커 배열에 저장
        this.markers.push(customOverlay);
      }

      console.log("마커 추가 완료:", this.markers.length);
    },

    formatPrice(value) {
      if (!value && value !== 0) return "정보 없음"; // 0은 허용
      const num = parseInt(value, 10);
      return (num / 10000).toFixed(1); // 억 단위 변환
    },

    // 시/도 목록 가져오기
    async fetchSido() {
      console.log("시/도 목록 가져오기::: fetchSido()");
      try {
        const response = await Rest.getSido();
        this.sidoList = response.map((item) => item.sidoName); // 시/도 데이터 정제
      } catch (error) {
        console.error("시/도 데이터를 가져오는 중 오류 발생:", error);
      }
    },

    // 구/군 목록 가져오기
    async fetchGugun() {
      console.log("구/군 목록 가져오기::: fetchGugun()");
      if (!this.selectedSido) return;
      try {
        const response = await Rest.getGugun(this.selectedSido);
        this.gugunList = response.map((item) => item.gugunName); // 구/군 데이터 정제
        this.selectedGugun = ""; // 초기화
        this.selectedDong = ""; // 초기화
        this.dongList = []; // 초기화
      } catch (error) {
        console.error("구/군 데이터를 가져오는 중 오류 발생:", error);
      }
    },

    // 동 목록 가져오기
    async fetchDong() {
      console.log("동 목록 가져오기::: fetchDong()");
      if (!this.selectedGugun) return;
      try {
        const response = await Rest.getDong(
          this.selectedSido,
          this.selectedGugun
        );
        this.dongList = response.map((item) => item.dongName); // 동 데이터 정제
        console.log("dong 데이터 정제후::: ", this.dongList);
        this.selectedDong = ""; // 초기화
      } catch (error) {
        console.error("동 데이터를 가져오는 중 오류 발생:", error);
      }
    },

    // 동 선택 시 지도 이동
    async moveToDong() {
      if (!this.selectedDong || !this.selectedGugun || !this.selectedSido) {
        console.error("시/도, 구/군, 동 정보를 모두 선택해야 합니다.");
        return;
      }

      try {
        const geocoder = new kakao.maps.services.Geocoder();
        const fullAddress = `${this.selectedSido} ${this.selectedGugun} ${this.selectedDong}`;
        console.log("이동할 전체 주소:", fullAddress);

        const result = await new Promise((resolve, reject) => {
          geocoder.addressSearch(fullAddress, (res, status) => {
            if (status === kakao.maps.services.Status.OK && res.length > 0) {
              resolve(res[0]);
            } else {
              reject(`동 데이터가 올바르지 않습니다: ${fullAddress}`);
            }
          });
        });

        // 지도 중심 이동
        this.map.setCenter(new kakao.maps.LatLng(result.y, result.x));
        this.map.setLevel(4); // 지도 확대 레벨 설정
      } catch (error) {
        console.error("지도 이동 실패:", error);
      }
    },
  },
});
</script>

<style scoped>
/* 전체 레이아웃 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  height: 100%;
}

/* 컨테이너 스타일: 전체 구조의 루트 */
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

/* 검색 툴바: TopBar 아래 검색 옵션 UI */
.search-toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  background-color: #ffffff;
  border-bottom: 1px solid #ddd;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  margin-top: 20px; /* TopBar와의 간격 */
}

/* 메인 레이아웃 */
.main-layout {
  display: flex;
  flex: 1;
  height: calc(100vh - 80px); /* TopBar와 검색 옵션 높이를 제외한 남은 영역 */
}

/* 검색 툴바 내부의 각 그룹 */
.search-group {
  display: flex;
  align-items: center;
  gap: 10px;
}

/* 드롭다운 (Select) 스타일 */
.select-box {
  padding: 8px 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #fff;
  cursor: pointer;
  font-size: 14px;
  transition: border-color 0.3s ease;
}

.select-box:hover {
  border-color: #007bff;
}

/* 검색 버튼 스타일 */
.search-btn {
  padding: 10px 20px;
  background-color: #007bff;
  border: none;
  color: #fff;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.search-btn:hover {
  background-color: #0056b3;
}

/* 콘텐츠 래퍼: 사이드바와 지도 영역을 포함 */
.content-wrapper {
  display: flex;
  flex: 1;
  overflow: hidden; /* 내부 스크롤을 방지 */
  height: calc(100vh - 80px); /* TopBar와 검색 옵션 높이를 제외 */
}

/* 사이드바: 매물 상세 정보를 표시 */
.sidebar {
  width: 400px; /* 고정된 너비 */
  background-color: #f8f9fa; /* 배경색 */
  border-right: 1px solid #ddd; /* 경계선 */
  padding: 20px;
  overflow-y: auto; /* 스크롤 활성화 */
  height: 100%; /* 부모 높이에 맞게 채우기 */
  box-sizing: border-box; /* 패딩 포함 크기 계산 */
}

/* 상세 정보 컨테이너 */
.details-container {
  background-color: #ffffff; /* 컨테이너 배경 흰색 */
  padding: 20px; /* 내부 여백 */
  border-radius: 8px; /* 모서리 둥글게 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 */
}

/* 상세 정보 타이틀 */
.details-container h3 {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 15px;
  text-align: center;
}

/* 상세 정보 항목 */
.detail-item {
  padding: 15px 0; /* 항목 간격 */
  border-bottom: 1px solid #201f1f; /* 항목 간 구분선 */
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-title-container {
  text-align: center;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #ffffff; /* 흰색 배경 */
  border: 2px solid #007bff; /* 파란색 테두리 */
  border-radius: 10px; /* 둥근 모서리 */
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 */
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.detail-title-container:hover {
  transform: scale(1.05); /* 호버 시 확대 */
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.2); /* 더 강한 그림자 */
}

.detail-title {
  font-size: 1.5rem; /* 글씨 크기 */
  font-weight: bold;
  color: #007bff; /* 파란색 텍스트 */
  display: inline-block;
}

.detail-icon {
  font-size: 1.2rem;
  color: #ff5722; /* 밝은 주황색 */
  margin-right: 10px; /* 아이콘과 텍스트 간격 */
}

.detail-info {
  margin-top: 10px;
}

.detail-info p {
  margin: 5px 0;
}

.detail-info span {
  font-weight: bold;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 10px; /* 항목 간 간격 */
  font-weight: bold;
}

/* 공통 스타일 */
.info-label {
  font-weight: bold;
  min-width: 80px; /* 일정한 폭을 유지하여 열 맞춤 */
}

.info-value {
  text-align: left; /* 값의 시작 정렬 */
  flex: 1; /* 유연하게 너비를 차지 */
  white-space: wrap; /* 기본적으로 한 줄 표시 */
  overflow: hidden;
  text-overflow: ellipsis;
}

.info-value-multiline {
  white-space: normal;
  word-wrap: break-word;
}

.detail-info .info-item:nth-child(odd) {
  background-color: #f9f9f9;
  padding: 10px;
  border-radius: 5px;
}

.detail-info .info-item:nth-child(even) {
  background-color: #eaeaea;
  padding: 10px;
  border-radius: 5px;
}

.section-title {
  font-size: 1.8rem; /* 제목 크기 */
  font-weight: bold;
  text-align: center;
  color: #333; /* 짙은 회색 */
  background-color: #f0f4f8; /* 연한 파란색 배경 */
  padding: 15px;
  border-radius: 10px; /* 둥근 모서리 */
  margin-bottom: 20px;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 */
}

.details-list {
  list-style: none; /* 기본 점 제거 */
  padding: 0;
  margin: 0;
}

/* 지도 및 카테고리 영역 */
.map-container {
  flex: 1;
  position: relative;
}

.map-placeholder {
  width: 100%;
  height: 100%;
}

/* 카테고리 툴바: 지도 위에 표시되는 카테고리 버튼 */
.category-toolbar {
  display: flex;
  flex-wrap: nowrap;
  gap: 5px;
  position: absolute;
  top: 20px;
  left: 20px;
  max-width: calc(9 * 120px + 8 * 10px); /* 버튼 최대 너비 120px + 간격 */
  z-index: 1000;
}

::v-deep(.category-emoji) {
  font-size: 30px; /* 이모지 크기 */
  line-height: 1; /* 간격 조절 */
}

::v-deep(.category-btn) {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

::v-deep(.category-btn:hover) {
  transform: scale(1.1); /* 크기 확대 효과 */
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
}

::v-deep(.category-content) {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px; /* 이모지와 텍스트 간격 */
}

::v-deep(.category-btn span) {
  font-size: 8px;
  color: #333;
  margin-top: 5px;
}

/* 커스텀 오버레이 스타일 */
::v-deep(.placeinfo) {
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 6px; /* 내부 여백 축소 */
  box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1); /* 그림자 크기 축소 */
  font-family: Arial, sans-serif;
  width: 180px; /* 너비 축소 */
  word-wrap: break-word; /* 줄바꿈 설정 */
  white-space: normal; /* 텍스트 줄바꿈 허용 */
}

::v-deep(.placeinfo .title) {
  display: block;
  font-size: 14px; /* 글씨 크기 축소 */
  font-weight: bold;
  color: white;
  background-color: #f04e30; /* 빨간색 배경 */
  padding: 6px; /* 여백 축소 */
  border-radius: 4px 4px 0 0; /* 상단 모서리 둥글게 */
  text-decoration: none;
  overflow: hidden; /* 초과 텍스트 숨김 */
  text-overflow: ellipsis; /* 초과 시 말줄임 표시 */
  white-space: nowrap; /* 한 줄로 유지 */
}

::v-deep(.placeinfo .title:hover) {
  text-decoration: underline;
}

::v-deep(.placeinfo .jibun),
::v-deep(.placeinfo .tel) {
  font-size: 10px;
  color: #555;
  word-wrap: break-word; /* 줄바꿈 설정 */
  white-space: normal; /* 텍스트 줄바꿈 허용 */
}

::v-deep(.placeinfo .tel) {
  color: #2b8a3e; /* 초록색 */
  font-weight: bold;
}

/* 반응형 레이아웃 */
@media (max-width: 768px) {
  /* 검색 툴바 내부의 그룹을 세로 배치 */
  .search-group {
    flex-direction: column;
  }

  .select-box,
  .search-btn {
    width: 100%; /* 전체 폭 사용 */
  }
}

/* 전/월세 필터 컨테이너 */
.rent-filters-container {
  display: flex; /* 옆으로 배치 */
  align-items: center; /* 세로 정렬 */
  gap: 10px; /* 필터 간 간격 */
}

/* 각 필터 아이템 */
.filter-item {
  display: flex; /* 라벨과 선택 박스를 가로로 정렬 */
  flex-direction: column; /* 기본적으로 세로 정렬 */
  align-items: flex-start; /* 좌측 정렬 */
}

.filter-item label {
  margin-bottom: 5px; /* 라벨과 선택 박스 간 간격 */
  font-weight: bold; /* 라벨 강조 */
}

.select-box {
  width: 150px; /* 선택 박스 너비 */
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}
</style>
