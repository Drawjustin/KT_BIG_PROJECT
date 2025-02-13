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
export const logoutPost = async (logoutParam) => {
  try {
    const res = await jwtAxios.post(`${host}/logout`, {
      userEmail: logoutParam.userEmail,
      accessToken: logoutParam.accessToken
    }, {
      withCredentials: true
    });
    
    return res.data;
  } catch (error) {
    console.error("Logout Error:", error);
    throw error;
  }
};

