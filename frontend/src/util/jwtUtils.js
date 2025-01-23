import axios from "axios";
import { getCookie, setCookie, removeCookie } from "./cookieUtils";

const jwtAxios = axios.create({
  baseURL: "https://your-api-url.com", // 기본 API URL
  withCredentials: true, // 쿠키 전송 활성화
});

// Refresh Token 갱신 API 경로
const REFRESH_TOKEN_ENDPOINT = "/auth/refresh";

/** 요청 전 처리 */
const beforeReq = (config) => {
  console.log("before request...");
  const memberInfo = getCookie("member");

  if (!memberInfo) {
    console.log("Member NOT FOUND");
    return Promise.reject({
      response: {
        data: { error: "REQUIRE_LOGIN" },
      },
    });
  }

  const { accessToken } = memberInfo;

  if (config.headers) {
    config.headers.Authorization = `Bearer ${accessToken}`; // Authorization 헤더 추가
  }

  return config;
};

/** 요청 실패 처리 */
const requestFail = (err) => {
  console.log("request error...");
  return Promise.reject(err);
};

/** 응답 실패 처리 */
const responseFail = async (error) => {
  const originalRequest = { ...error.config, _retry: error.config._retry || false };

  // 401 Unauthorized 처리
  if (error.response?.status === 401 && !originalRequest._retry) {
    originalRequest._retry = true; // 재시도 플래그 설정
    const memberInfo = getCookie("member");

    if (memberInfo?.refreshToken) {
      try {
        console.log("Attempting to refresh tokens...");
        // Refresh Token으로 새 Access Token 요청
        const refreshResponse = await axios.post(
          REFRESH_TOKEN_ENDPOINT,
          { refreshToken: memberInfo.refreshToken },
          { withCredentials: true } // 쿠키 포함 요청
        );

        const { accessToken, refreshToken } = refreshResponse.data;

        // 갱신된 토큰 쿠키에 저장
        setCookie("member", JSON.stringify({ accessToken, refreshToken }), 7);

        // 원래 요청 재시도
        if (originalRequest.headers) {
          originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        } else {
          originalRequest.headers = { Authorization: `Bearer ${accessToken}` };
        }
        return jwtAxios(originalRequest);
      } catch (refreshError) {
        console.error("Token refresh failed. Logging out...", refreshError.message);
        removeCookie("member"); // 쿠키 삭제
        window.location.href = "/login"; // 로그인 페이지로 리다이렉트
      }
    } else {
      console.error("No refresh token found. Redirecting to login...");
      removeCookie("member"); // 쿠키 삭제
      window.location.href = "/login"; // 로그인 페이지로 리다이렉트
    }
  }

  return Promise.reject(error);
};

/** 응답 성공 처리 */
const beforeRes = (res) => {
  console.log("before return response...");
  return res;
};

// 인터셉터 설정
jwtAxios.interceptors.request.use(beforeReq, requestFail);
jwtAxios.interceptors.response.use(beforeRes, responseFail);

export default jwtAxios;


/** 주석 처리된 원래 코드 */

// //갱신 요청 처리 전 코드 
// import axios from "axios";
// import { getCookie } from "./cookieUtils";

// const jwtAxios = axios.create();

// /** 요청 전 처리 */
// const beforeReq = (config) => {
//   console.log("before request...");
//   const memberInfo = getCookie("member");

//   if (!memberInfo) {
//     console.log("Member NOT FOUND");
//     return Promise.reject({
//       response: {
//         data: { error: "REQUIRE_LOGIN" },
//       },
//     });
//   }

//   const { accessToken } = memberInfo;

//   if (config.headers) {
//     config.headers.Authorization = `Bearer ${accessToken}`; // Authorization 헤더 추가
//   }

//   return config;
// };

// /** 요청 실패 처리 */
// const requestFail = (err) => {
//   console.log("request error...");
//   return Promise.reject(err);
// };

// /** 응답 성공 처리 */
// const beforeRes = (res) => {
//   console.log("before return response...");
//   return res;
// };

// /** 응답 실패 처리 */
// const responseFail = (err) => {
//   console.log("response fail error...");
//   return Promise.reject(err);
// };

// // 인터셉터 설정
// jwtAxios.interceptors.request.use(beforeReq, requestFail);
// jwtAxios.interceptors.response.use(beforeRes, responseFail);

// export default jwtAxios;