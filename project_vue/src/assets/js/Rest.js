import axios from "axios";

export const BASE_URL = "http://localhost:8080";
// export const BASE_URL = "http://192.168.205.74:8080";

/**
 *   로그인
 */
const login = async (userId, userPwd, vm) => {
  try {
    // 로그인 요청
    const response = await axios.post(`${BASE_URL}/user/login`, {
      userId,
      userPwd,
    });

    // HTTP 상태 코드가 200일 경우 로그인 성공 처리
    if (response.status === 200 && response.data?.accessToken) {
      console.log("로그인 응답 데이터:", response.data);

      const { accessToken, refreshToken } = response.data;

      // 로컬 스토리지에 토큰 저장
      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);

      console.log("accessToken 저장됨:", accessToken);
      console.log("refreshToken 저장됨:", refreshToken);

      // 로그인 성공 후 board 페이지로 이동
      vm.$router.push({ name: "board" });
    } else {
      // 응답 상태는 200이지만 필요한 데이터가 없는 경우
      console.error("로그인 실패: 응답에 필요한 데이터가 없습니다", response);
      alert("로그인에 실패했습니다. 서버 응답을 확인해주세요.");
    }
  } catch (error) {
    // 오류가 발생한 경우
    console.error("로그인 요청 중 오류 발생:", error);

    if (error.response) {
      // 서버 응답 오류 처리
      const { status, data } = error.response;
      if (status === 401) {
        alert("아이디 또는 비밀번호를 확인 후 다시 시도해주세요.");
      } else {
        alert(
          data?.message || "로그인 중 오류가 발생했습니다. 다시 시도해주세요."
        );
      }
    } else {
      // 네트워크 오류 또는 기타 클라이언트 오류 처리
      alert("네트워크 오류가 발생했습니다. 인터넷 연결을 확인해주세요.");
    }
  }
};

/**
 * 사용자 정보 가져오기
 */
const getUserInfo = async () => {
  try {
    const token = localStorage.getItem("accessToken");
    if (!token) {
      console.error("토큰이 없습니다. 다시 로그인해주세요.");
      return null;
    }

    const response = await axios.get(`${BASE_URL}/user/info`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.status === 200) {
      return response.data;
    }
  } catch (error) {
    console.error("사용자 정보 요청 중 오류 발생:", error);
    return null;
  }
};

/**
 * 전체 게시글
 */
const boardAll = async (pageNum) => {
  try {
    // 쿼리 파라미터로 페이지 번호를 전달
    const response = await axios.get(`${BASE_URL}/article/list`, {
      params: {
        pgno: pageNum, // 여기에서 'pgno'는 서버에서 요구하는 파라미터 이름입니다.
      },
    });

    if (response.status === 200) {
      console.log("response : ", response.data.articles);
      const formattedData = response.data.articles.map((post) => ({
        id: post.articleNo, // articleNo -> id
        content: post.content,
        views: post.hit, // hit -> views
        createdAt: post.registerTime, // registerTime -> createdAt
        title: post.subject, // subject -> title
        authorId: post.userId, // userId -> authorId
        author: post.userName, // userName -> author
        type:
          post.type === 1 ? "general" : post.type === 2 ? "broker" : "general",
        date: post.registerTime.split(" ")[0], // 날짜를 'YYYY-MM-DD' 형식으로 분리
      }));
      return { posts: formattedData };
    }
  } catch (error) {
    console.log("boardAll 실패", error);
  }
};

// 사용자의 토큰을 통해서 id를 찾는 코드
const getUserId = async () => {
  try {
    const token = localStorage.getItem("accessToken");

    const response = await axios.post(`${BASE_URL}/user/getId`, null, {
      // "URL 경로"를 실제 엔드포인트로 변경 필요
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    console.log("userID " + response.data.userId);
    return response.data.userId;
  } catch (error) {
    console.log("getUserId error :", error);
  }
};

// board 신규 create 코드
const boardCreate = async (post, vm) => {
  try {
    //userId 값 받기
    const userInfo = await getUserInfo();
    const userId = userInfo.userId;
    const response = await axios.post(`${BASE_URL}/article/write`, {
      userId: userId,
      subject: post.title,
      content: post.content,
    });

    if (response.status === 200) {
      console.log("response : ", response.data);

      vm.$router.push({
        name: "boardDetail",
        query: { articleno: response.data },
      });
    }
  } catch (error) {
    console.log("저장 실패 ", error);
    return "fail";
  }
};

// 게시글 정보 확인하는 곳
const boardDetail = async (articleno) => {
  try {
    const response = await axios.post(`${BASE_URL}/article/view`, {
      articleno: articleno,
    });

    const data = response.data.article;
    console.log("data : ", data);

    if (response.status === 200) {
      // 데이터 정제
      const formattedData = {
        id: data.articleNo,
        title: data.boardSubject, // 게시글 번호 => 제목
        author: data.boardUserId, // 작성자 ID => 작성자
        content: data.content, // 게시글 내용
        createdAt: data.boardRegisterTime, // 게시글 등록 시간
        views: data.hit, // 조회수
        comments: data.comments.map((comment) => ({
          id: comment.commentId, // 댓글 ID
          commentUserId: comment.commentUserId, // 댓글 작성자 ID
          commentContent: comment.commentContent, // 댓글 내용
          commentRegisterTime: comment.commentRegisterTime, // 댓글 등록 시간
          replying: false, // 대댓글 입력 여부
          replyContent: "", // 대댓글 내용
          repliesVisible: false, // 대댓글 보기 여부
          replies: comment.childComments
            ? comment.childComments.map((child) => ({
                id: child.commentId, // 자식 댓글 ID
                author: child.commentUserId, // 자식 댓글 작성자 ID
                content: child.commentContent, // 자식 댓글 내용
                createdAt: child.commentRegisterTime, // 자식 댓글 등록 시간
              }))
            : [], // 자식 댓글이 있을 경우 정제하여 포함
        })),
      };
      return formattedData;
    }
  } catch (error) {
    console.log("boardDetail error : ", error);
  }
};

// 댓글 추가
const addComment = async (articleno, content, vm) => {
  try {
    const userInfo = await getUserInfo();
    const userId = userInfo.userId;
    console.log("article : ", articleno);
    console.log("content : ", content);
    const response = await axios.post(`${BASE_URL}/article/addComment`, {
      userId: userId,
      articleNo: articleno,
      content: content,
    });

    if (response.status === 200) {
      console.log("새로고침 됩니다");
      // 필요 시 페이지 새로고침 또는 상태 업데이트 로직 추가
    }
  } catch (error) {
    console.log("addComment Error :", error);
  }
};

// 대댓글 추가
const addChildComment = async (articleNo, parentId, content) => {
  console.log("childComment 실행됐음");
  try {
    const userInfo = await getUserInfo();
    const userId = userInfo.userId;

    const response = await axios.post(`${BASE_URL}/article/addComment`, {
      userId: userId,
      articleNo: articleNo,
      parentId: parentId,
      content: content,
    });

    if (response.status === 200) {
      console.log("추가 댓글 성공");
      // 필요 시 상태 업데이트 로직 추가
    }
  } catch (error) {
    console.log("addChildComment ERROR :", error);
  }
};

// 수정 페이지 이동 관련 검사
const modifyCheck = async (id) => {
  const userInfo = await getUserInfo();
  const userId = userInfo.userId;
  console.log("id : ", id);
  console.log("user id : ", userId);
  return userId === id;
};

const modifyData = async (articleNo) => {
  try {
    const response = await axios.get(`${BASE_URL}/article/getModifyData`, {
      params: {
        articleNo: articleNo,
      },
    });

    if (response.status === 200) {
      console.log("success");
      return response.data;
    }
  } catch (error) {
    console.log("modifyData ERROR :", error);
  }
};

const modifyBoard = async (articleNo, boardInfo, vm) => {
  try {
    const response = await axios.put(`${BASE_URL}/article/modify`, {
      articleNo: articleNo,
      subject: boardInfo.title,
      content: boardInfo.content,
    });

    if (response.status === 200) {
      alert("게시물 수정 완료");
      vm.$router.push({
        name: "boardDetail",
        query: { articleno: articleNo },
      });
    }
  } catch (error) {
    console.log("modifyBoard ERROR :", error);
  }
};
// 게시글 삭제 함수
const deleteBoard = async (articleNo) => {
  console.log("deletePost Rest.js  들어옴");
  try {
    const token = localStorage.getItem("accessToken");
    if (!token) {
      alert("로그인이 필요합니다.");
      return null;
    }
    console.log("deletePost: " + articleNo);
    const response = await axios.delete(
      `${BASE_URL}/article/delete/${articleNo}`
    );

    if (response.status === 200) {
      console.log("게시글 삭제 성공:", response.data);
      return true;
    }
  } catch (error) {
    console.error("게시글 삭제 실패:", error);
    return false;
  }
};

/**
 * 공인중개사 게시글
 */
const search = () => {};

/**
 * 일반사용자 게시글
 */
const boardUser = () => {};

/**
 * 작성자 검색
 */
const searchAuthor = async (searchTerm) => {
  try {
    const response = await axios.get(`${BASE_URL}/article/searchAuthor`, {
      params: {
        term: searchTerm,
      },
    });
    if (response.status === 200) {
      const formattedData = response.data.articles.map((post) => ({
        id: post.articleNo, // articleNo -> id
        content: post.content,
        views: post.hit, // hit -> views
        createdAt: post.registerTime, // registerTime -> createdAt
        title: post.subject, // subject -> title
        authorId: post.userId, // userId -> authorId
        author: post.userName, // userName -> author
        type: post.type ?? "general", // type 필드가 없다면 기본값 설정
        date: post.registerTime.split(" ")[0], // 날짜를 'YYYY-MM-DD' 형식으로 분리
      }));
      return { posts: formattedData };
    }
  } catch (error) {
    console.log("searchAuthor Error :", error);
  }
};

/**
 * 제목 검색
 */
const searchTitle = async (searchTerm) => {
  try {
    const response = await axios.get(`${BASE_URL}/article/searchTitle`, {
      params: {
        term: searchTerm,
      },
    });

    if (response.status === 200) {
      const formattedData = response.data.articles.map((post) => ({
        id: post.articleNo, // articleNo -> id
        content: post.content,
        views: post.hit, // hit -> views
        createdAt: post.registerTime, // registerTime -> createdAt
        title: post.subject, // subject -> title
        authorId: post.userId, // userId -> authorId
        author: post.userName, // userName -> author
        type: post.type ?? "general", // type 필드가 없다면 기본값 설정
        date: post.registerTime.split(" ")[0], // 날짜를 'YYYY-MM-DD' 형식으로 분리
      }));
      return { posts: formattedData };
    }
  } catch (error) {
    console.log("searchTitle Error :", error);
  }
};

/**
 * 작성자 + 제목 검색
 */
const searchAll = async (searchTerm) => {
  try {
    const response = await axios.get(`${BASE_URL}/article/searchAll`, {
      params: {
        term: searchTerm,
      },
    });

    if (response.status === 200) {
      const formattedData = response.data.articles.map((post) => ({
        id: post.articleNo, // articleNo -> id
        content: post.content,
        views: post.hit, // hit -> views
        createdAt: post.registerTime, // registerTime -> createdAt
        title: post.subject, // subject -> title
        authorId: post.userId, // userId -> authorId
        author: post.userName, // userName -> author
        type: post.type ?? "general", // type 필드가 없다면 기본값 설정
        date: post.registerTime.split(" ")[0], // 날짜를 'YYYY-MM-DD' 형식으로 분리
      }));
      return { posts: formattedData };
    }
  } catch (error) {
    console.log("searchAll Error :", error);
  }
};

/**
 * 회원가입
 */
// 회원가입 관련 함수가 필요하다면 여기에 추가하세요.

/**
 * 마이페이지 수정
 */
const updateUserInfo = async (updateData, vm) => {
  try {
    const token = localStorage.getItem("accessToken");
    if (!token) {
      throw new Error("토큰이 없습니다. 다시 로그인해주세요.");
    }

    const response = await axios.put(
      `${BASE_URL}/user/updateMember`,
      updateData,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    console.log("updateUser", response);
    if (response.status === 200) {
      console.log("회원 정보가 성공적으로 수정되었습니다.");
    }
  } catch (error) {
    console.error("회원 정보 수정 중 오류 발생:", error);
    throw error; // 오류가 발생한 경우 상위 메소드에서 잡히도록 예외를 다시 던짐
  }
};

/**
 * 회원 탈퇴
 */
const deleteUserInfo = async (vm) => {
  try {
    // 로컬 스토리지에서 토큰 가져오기
    const token = localStorage.getItem("accessToken");
    if (!token) {
      alert("토큰이 유효하지 않습니다. 다시 로그인해주세요.");
      vm.$router.push({ name: "login" });
      return;
    }

    // 회원 탈퇴 API 호출
    const response = await axios.delete(`${BASE_URL}/user/deleteMember`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    // 성공 시 처리
    if (response.status === 200) {
      alert("회원 탈퇴가 완료되었습니다.");
      // 로그아웃과 동일한 상태 초기화
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      vm.$router.push({ name: "home" });
    } else {
      console.error("회원 탈퇴 실패:", response);
      alert("회원 탈퇴에 실패했습니다. 다시 시도해주세요.");
    }
  } catch (error) {
    // 오류 처리
    console.error("회원 삭제 중 오류 발생:", error);
    alert("회원 탈퇴 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
    throw error;
  }
};

/**
 * 토큰 만료 확인 함수
 */
const isTokenExpired = (token) => {
  if (!token) return true;
  const payload = JSON.parse(atob(token.split(".")[1]));
  console.log("payload", payload);
  return payload.exp * 1000 < Date.now();
};

/**
 * 강제 로그아웃 함수
 */
const forceLogout = (vm) => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  vm.$router.push({ name: "home" });
};

/**
 * Access Token 및 Refresh Token 확인 및 갱신
 */
const validateAndRefreshToken = async (vm) => {
  let accessToken = localStorage.getItem("accessToken");
  const refreshToken = localStorage.getItem("refreshToken");

  // Access Token 만료 확인
  if (!accessToken || isTokenExpired(accessToken)) {
    if (!refreshToken || isTokenExpired(refreshToken)) {
      // Refresh Token도 만료 -> 강제 로그아웃
      console.error("Refresh Token이 만료되었습니다. 로그아웃합니다.");
      forceLogout(vm);
      return false;
    }

    try {
      // Refresh Token을 사용하여 Access Token 재발급
      const response = await axios.post(`${BASE_URL}/user/refresh`, {
        refreshToken,
      });
      accessToken = response.data.accessToken;
      localStorage.setItem("accessToken", accessToken);
    } catch (error) {
      console.error("토큰 갱신 실패:", error);
      forceLogout(vm);
      return false;
    }
  }

  return true;
};

// Kakao Map

/**
 * 시도 검색
 */
const selectSido = async (sido) => {
  const response = await axios.get(`${BASE_URL}/home/searchSido`, {
    params: { sido },
  });
  console.log("response", response);

  // 데이터 매핑
  return response.data.map((item) => ({
    id: item.aptSeq,
    name: item.aptName || "정보 없음",
    oldAddress: item.jibun || "정보 없음",
    newAddress: item.newAddress || "정보 없음",
    transactionDate: `${item.dealYear}-${String(item.dealMonth).padStart(
      2,
      "0"
    )}`,
    dealAmount: item.dealAmount || "정보 없음",
    latitude: parseFloat(item.latitude) || 0,
    longitude: parseFloat(item.longitude) || 0,
  }));
};

/**
 * 시도 + 구군 검색
 */
const selectSidoGugun = async (sido, gugun) => {
  const response = await axios.get(`${BASE_URL}/home/searchAll`, {
    params: { sido, gugun },
  });
  console.log("response", response);

  // 데이터 매핑
  return response.data.map((item) => ({
    id: item.aptSeq,
    name: item.aptName || "정보 없음",
    oldAddress: item.jibun || "정보 없음",
    newAddress: item.newAddress || "정보 없음",
    transactionDate: `${item.dealYear}-${String(item.dealMonth).padStart(
      2,
      "0"
    )}`,
    dealAmount: item.dealAmount || "정보 없음",
    latitude: parseFloat(item.latitude) || 0,
    longitude: parseFloat(item.longitude) || 0,
  }));
};

/**
 * 이름으로 검색
 */
const selectName = async (name) => {
  const response = await axios.get(`${BASE_URL}/home/searchName`, {
    params: { name },
  });
  console.log("response", response);

  // 데이터 매핑
  return response.data.map((item) => ({
    id: item.aptSeq,
    name: item.aptName || "정보 없음",
    oldAddress: item.jibun || "정보 없음",
    newAddress: item.newAddress || "정보 없음",
    transactionDate: `${item.dealYear}-${String(item.dealMonth).padStart(
      2,
      "0"
    )}`,
    dealAmount: item.dealAmount || "정보 없음",
    latitude: parseFloat(item.latitude) || 0,
    longitude: parseFloat(item.longitude) || 0,
  }));
};

/**
 * 유형과 거래 형태로 데이터 가져오기
 */
const getProperties = async (
  type,
  dealType,
  addresses,
  area,
  price,
  deposit,
  monthlyRent
) => {
  console.log("getProperties 호출");
  console.log("보낼 데이터:", {
    type,
    dealType,
    addresses,
    area,
    price,
    deposit,
    monthlyRent,
  });

  // 전/월세와 매매에 따른 조건 설정
  const payload = {
    type, // 매물 유형 (APT, VILLA, OFFICETEL 등)
    dealType, // 거래 형태 (SALE, RENT)
    area, // 선택한 면적 범위 [min, max]
    addresses, // 읍면동 및 지번 목록
  };

  if (dealType === "SALE") {
    // 매매: 가격 필터 추가
    payload.price = price;
  } else if (dealType === "RENT") {
    // 전/월세: 보증금과 월세 필터 추가
    payload.deposit = deposit;
    payload.monthlyRent = monthlyRent;
  }

  try {
    const response = await axios.post(
      `${BASE_URL}/home/findProperties`,
      payload,
      {
        headers: {
          "Content-Type": "application/json", // 헤더 설정
        },
      }
    );
    console.log("응답 데이터:", response.data);
    return response.data; // 서버에서 처리된 데이터를 반환
  } catch (error) {
    console.error("getProperties 에러:", error);
    throw error;
  }
};

/**
 * 시/도 목록 가져오기
 */
const getSido = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/home/sido`);
    console.log("getSido 응답데이터::: ", response.data);
    return response.data; // 시/도 데이터 반환
  } catch (error) {
    console.error("getSido 에러:", error);
    throw error;
  }
};

/**
 * 구/군 목록 가져오기
 */
const getGugun = async (sidoName) => {
  try {
    const response = await axios.get(`${BASE_URL}/home/gugun`, {
      params: { sidoName },
    });
    console.log("getGugun 응답데이터::: ", response.data);
    return response.data; // 구/군 데이터 반환
  } catch (error) {
    console.error("getGugun 에러:", error);
    throw error;
  }
};

/**
 * 동 목록 가져오기
 */
const getDong = async (sidoName, gugunName) => {
  try {
    const response = await axios.get(`${BASE_URL}/home/dong`, {
      params: { sidoName, gugunName },
    });
    console.log("getDong 응답데이터::: ", response.data);
    return response.data; // 동 데이터 반환
  } catch (error) {
    console.error("getDong 에러:", error);
    throw error;
  }
};

/**
 * 동을 기반으로 시/도와 구/군 가져오기
 */
const getLocationByDong = async (code, dongName) => {
  try {
    const response = await axios.get(`${BASE_URL}/home/getPropertyDetails`, {
      params: { code, dongName }, // 쿼리 파라미터로 전달
    });
    console.log("getLocationByDong 응답데이터::: ", response.data);
    return response.data;
  } catch (error) {
    console.error("getLocationByDong 에러:", error);
    throw error;
  }
};
const deleteAccount = async (token) => {
  try {
    const response = await axios.delete(`${BASE_URL}/user/deleteMember`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return response.data; // 서버 응답 데이터 반환
  } catch (error) {
    console.error("회원 탈퇴 요청 실패:", error);
    throw error;
  }
};
/**
 * 추천받기
 */
const getRecommendation = async (ratings) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/home/getRecommendation`,
      ratings
    );
    return response.data; // 백엔드의 추천 결과 반환
  } catch (error) {
    console.error("추천받기 요청에 실패했습니다.", error);
    throw new Error("추천받기 요청에 실패했습니다.");
  }
};

export default {
  deleteAccount,
  boardAll,
  getUserInfo,
  login,
  getUserId,
  boardCreate,
  boardDetail,
  updateUserInfo,
  validateAndRefreshToken,
  addComment,
  addChildComment,
  modifyCheck,
  modifyData,
  modifyBoard,
  searchTitle,
  searchAuthor,
  searchAll,
  selectSido,
  selectSidoGugun,
  selectName,
  getProperties,
  getSido,
  getGugun,
  getDong,
  getLocationByDong,
  getRecommendation,
  deleteBoard,
};
