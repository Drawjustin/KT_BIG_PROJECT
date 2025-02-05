// 회원 관련 API 요청,, 서버 통신
import axios from "axios";
import jwtAxios from "../util/jwtUtils";
const API_SERVER_HOST = import.meta.env.VITE_API_BASE_URL;
const host = `${API_SERVER_HOST}/api`;

/**로그인 */
export const loginPost = async (loginParam) => {
  const headers = { 
    headers: { 
      "Content-Type": "application/json"  
    } 
  };

  const requestBody = {
    userEmail: loginParam.userEmail,
    userPassword: loginParam.userPassword
  };

  try {
    console.log('Request URL:', `${host}/login`);
    console.log('Request Body:', requestBody);
    
    const res = await axios.post(`${host}/login`, requestBody, {
      ...headers,
      // 추가 디버깅용 설정
      timeout: 5000, // 5초 타임아웃
      withCredentials: true // 크로스 오리진 요청 시 필요
    });
    console.log("Login API Response:", res.data); // 응답 구조 확인
    return res.data;
  } catch (error) {
    console.error("Detailed Error:", {
      message: error.message,
      response: error.response,
      request: error.request,
      config: error.config
    });
    throw error;
  }
};
/** 로그아웃 */
export const logoutPost = async () => {
  try {
    const res = await jwtAxios.post(`${host}/logout`);
    return res.data;
  } catch (error) {
    console.error("Logout Error:", error);
    throw error;
  }
};




// // 회원 관련 API 요청 함수 모음 파일
// import axios from "axios";
// // const API_SERVER_HOST = 'http://98.84.14.117:8080'; // 수정된 API 서버 주소
// const API_SERVER_HOST = import.meta.env.VITE_API_BASE_URL; // env에서 주소 import
// const host = `${API_SERVER_HOST}/api`; // 수정된 API 서버 경로

// // 로그인 API 호출 함수
// export const loginPost = async (loginParam) => {
//   const headers = { headers: { "Content-Type": "application/json"  } };

//   const form = new FormData();
//   form.append("username", loginParam.email);
//   form.append("password", loginParam.pw);

//   const res = await axios.post(`${host}/login`, form, headers); // 엔드포인트 호출

//   return res.data; // 서버 응답 데이터 반환
// };


// // (test code) 백엔드 서버 없이 하드코딩으로 로그인 테스트 (이메일, 패스워드 설정 및 fake jwt token 전달)
// export const loginPost = async (loginParam) => {
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