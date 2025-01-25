// import axios from "axios";
// // 서버와 통신하는 비동기 함수 정의 파일 : 로그인 상태관리를 위한 파일

// const API_SERVER_HOST = 'http://localhost:8080'; // 수정된 API 서버 주소

// const host = `${API_SERVER_HOST}/api`; // 수정된 API 서버 경로

// // 로그인 API 호출 함수
// export const loginPost = async (loginParam) => {
//   const headers = { headers: { "Content-Type": "x-www-form-urlencoded" } };

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