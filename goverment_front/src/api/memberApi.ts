// import axios from "axios";

// const API_SERVER_HOST = 'http://localhost:8080';

// // Axios 인스턴스 생성
// const axiosInstance = axios.create({
//  baseURL: API_SERVER_HOST,
//  headers: {
//    'Authorization': 'Bearer fake-jwt-token',
//    'Content-Type': 'application/json'
//  }
// });

// export const loginPost = async (loginParam: { email: string; pw: string }) => {
//  const form = new FormData();
//  form.append("username", loginParam.email);
//  form.append("password", loginParam.pw);

//  const res = await axiosInstance.post('/login', form);
//  return res.data;
// };


import axios, { AxiosError } from "axios";

// API 응답 타입 정의
interface LoginResponse {
  accessToken: string;
  userEmail: string;
}

// 에러 응답 타입 정의
interface ApiError {
  status: number;
  message: string;
}

// 환경변수 검증
const baseURL = import.meta.env.VITE_API_BASE_URL;
if (!baseURL) {
  throw new Error('API base URL is not configured');
}

const API_SERVER_HOST = baseURL;

export const loginPost = async (loginParam: { userEmail: string; userPassword: string }): Promise<LoginResponse> => {
  // 입력값 검증
  if (!loginParam.userEmail || !loginParam.userPassword) {
    throw new Error('이메일과 비밀번호는 필수 입력값입니다.');
  }

  try {
    // axios 설정 객체 올바르게 구조화
    const config = {
      headers: { 
        "Content-Type": "application/json" 
      },
      withCredentials: true
    };
    
    const data = {
      userEmail: loginParam.userEmail,
      userPassword: loginParam.userPassword
    };

    const response = await axios.post<LoginResponse>(
      `${API_SERVER_HOST}/api/login`, 
      data, 
      config
    );

    return response.data;
    
  } catch (error) {
    if (axios.isAxiosError(error)) {
      // API 에러 처리
      throw {
        status: error.response?.status || 500,
        message: error.response?.data?.message || '로그인 요청 실패'
      } as ApiError;
    }
    // 네트워크 에러 등 기타 에러 처리
    throw {
      status: 500,
      message: '알 수 없는 오류가 발생했습니다.'
    } as ApiError;
  }
};
// import axios from "axios";
// // 서버와 통신하는 비동기 함수 정의 파일

// const baseURL= import.meta.env.VITE_API_BASE_URL;// 환경변수에서 url 가져옴
// const API_SERVER_HOST = baseURL;

// export const loginPost = async (loginParam: { userEmail: string; userPassword: string }) => {
//   const headers = { headers: { "Content-Type": "application/json" },
//   withCredentials: true // CORS 쿠키 전송 설정 추가
//   };
  
//   const data = {
//     userEmail: loginParam.userEmail,
//     userPassword: loginParam.userPassword
//   };

//   const res = await axios.post(`${API_SERVER_HOST}/api/login`, data, headers);
//   return res.data;
// };


// // 로그인 API 호출 함수
// export const loginPost = async (loginParam: { email: string; pw: string }) => {
//   const headers = { headers: { "Content-Type": "x-www-form-urlencoded" } };

//   const form = new FormData();
//   form.append("username", loginParam.email);
//   form.append("password", loginParam.pw);

//   const res = await axios.post(`${API_SERVER_HOST}/api/login`, form, headers);

//   return res.data; // 서버 응답 데이터 반환
// };  




// //(test code) 백엔드 서버 없이 하드코딩으로 로그인 테스트 (이메일,패스워드 설정 및 fake jwt token 전달)
// export const loginPost = async (loginParam: { email: string; pw: string }) => {
//   return new Promise((resolve, reject) => {
//     setTimeout(() => {
//       if (loginParam.email === 'test@example.com' && loginParam.pw === 'password123') {
//         resolve({
//           email: 'test@example.com',
//           accessToken: 'fake-jwt-token',
//         });
//       } else {
//         reject({ message: 'Invalid credentials' });
//       }
//     }, 1000); // 1초 딜레이
//   });
// };