// JWT 인증을 위한 Axios 인스턴스 및 인터셉터 설정 파일 jwtAxios는 인증 토큰 관리
import axios from "axios";
import { getCookie, setCookie, removeCookie } from "./cookieUtils";

const jwtAxios = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  withCredentials: true, // 쿠키 전송 활성화
  
});

const REFRESH_TOKEN_ENDPOINT = "/auth/refresh";

/** 요청 전 처리 */
const beforeReq = (config) => {
  console.log("before request...");
  const memberInfo = getCookie("member");

  if (!memberInfo) {
    // alert('로그인이 만료되었습니다. 로그인 페이지로 이동합니다.');
    console.log("Member NOT FOUND");
    // // fake token 생성 (테스트용)
    // const fakeToken = "fake-token-12345";

    // // fake token을 헤더에 추가
    // if (config.headers) {
    //   config.headers.Authorization = `Bearer ${fakeToken}`;
    // }

    // // 리디렉션은 하지 않지만 fake token으로 요청을 보냄
    // return config;
    // window.location.href = "/login";
    // return Promise.reject({
    //   response: {
    //     data: { error: "REQUIRE_LOGIN" },
    // //   },
    // });
  
  }
  

  const { accessToken } = memberInfo;

  if (config.headers) {
    config.headers.Authorization = `Bearer ${accessToken}`;
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

  if (error.response?.status === 401 && !originalRequest._retry) {
    originalRequest._retry = true;

    try {
      const refreshResponse = await axios.post(
        REFRESH_TOKEN_ENDPOINT,
        {},
        { withCredentials: true }
      );

      const { accessToken } = refreshResponse.data;
      // refreshToken은 쿠키로 받기 때문에 저장하지 않음
      setCookie("member", JSON.stringify({ accessToken }), 7);

      if (originalRequest.headers) {
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
      }
      return jwtAxios(originalRequest);
    } catch (refreshError) {
      console.error("Token refresh failed. Logging out...",refreshError.message);
      removeCookie("member");
      window.location.href = "/login";
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




// import axios from "axios";
// import { getCookie, setCookie, removeCookie } from "./cookieUtils";

// const jwtAxios = axios.create({
//   baseURL: 'https://c9a3-122-37-19-2.ngrok-free.app', // 기본 API URL
//   withCredentials: true, // 쿠키 전송 활성화
// });

// // Refresh Token 갱신 API 경로
// const REFRESH_TOKEN_ENDPOINT = "/auth/refresh";

// /** 요청 전 처리 */
// const beforeReq = (config) => {
//   console.log("before request...");
//   const memberInfo = getCookie("member");

//   if (!memberInfo) {
//     console.log("Member NOT FOUND");
//     // fake token 생성 (테스트용)
//     const fakeToken = "fake-token-12345";

//     // fake token을 헤더에 추가
//     if (config.headers) {
//       config.headers.Authorization = `Bearer ${fakeToken}`;
//     }

//     // 리디렉션은 하지 않지만 fake token으로 요청을 보냄
//     return config;
//   }
//   //   return Promise.reject({
//   //     response: {
//   //       data: { error: "REQUIRE_LOGIN" },
//   //     },
//   //   });
//   // }

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

// /** 응답 실패 처리 */
// const responseFail = async (error) => {
//   const originalRequest = { ...error.config, _retry: error.config._retry || false };

//   // 401 Unauthorized 처리
//   if (error.response?.status === 401 && !originalRequest._retry) {
//     originalRequest._retry = true; // 재시도 플래그 설정
//     const memberInfo = getCookie("member");

//     if (memberInfo?.refreshToken) {
//       try {
//         console.log("리프레시 토큰 시도");
//         // Refresh Token으로 새 Access Token 요청
//         const refreshResponse = await axios.post(
//           REFRESH_TOKEN_ENDPOINT,
//           {},
//           { withCredentials: true } // 쿠키 포함 요청
//         );

//         const { accessToken, refreshToken } = refreshResponse.data;
//         console.log("토큰 갱신 성공");

//         // 갱신된 토큰 쿠키에 저장
//         setCookie("member", JSON.stringify({ accessToken, refreshToken }), 7);

//         // 원래 요청 재시도
//         if (originalRequest.headers) {
//           originalRequest.headers.Authorization = `Bearer ${accessToken}`;
//         } else {
//           originalRequest.headers = { Authorization: `Bearer ${accessToken}` };
//         }
//         return jwtAxios(originalRequest);
//       } catch (refreshError) {
//         console.error("Token refresh failed. Logging out...", refreshError.message);
//         removeCookie("member"); // 쿠키 삭제
//         window.location.href = "/login"; // 로그인 페이지로 리다이렉트
//       }
//     } else {
//       console.error("No refresh token found. Redirecting to login...");
//       removeCookie("member"); // 쿠키 삭제
//       window.location.href = "/login"; // 로그인 페이지로 리다이렉트
//     }
//   }

//   return Promise.reject(error);
// };

// /** 응답 성공 처리 */
// const beforeRes = (res) => {
//   console.log("before return response...");
//   return res;
// };

// // 인터셉터 설정
// jwtAxios.interceptors.request.use(beforeReq, requestFail);
// jwtAxios.interceptors.response.use(beforeRes, responseFail);

// export default jwtAxios;


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