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


import axios from "axios";
// 서버와 통신하는 비동기 함수 정의 파일


const API_SERVER_HOST = 'https://8c21-122-37-19-2.ngrok-free.app';

export const loginPost = async (loginParam: { email: string; pw: string }) => {
  const headers = { headers: { "Content-Type": "application/json" },


};
  
  const data = {
    userEmail: loginParam.email,
    userPassword: loginParam.pw
  };

  const res = await axios.post(`${API_SERVER_HOST}/api/login`, data, headers);
  return res.data;
};


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