// 쿠키 관리를 위한 유틸리티 함수 모음 파일
import { Cookies } from "react-cookie";

const cookies = new Cookies();

/** 쿠키 저장 */
export const setCookie = (name, value, days) => {
  const expires = new Date();
  expires.setUTCDate(expires.getUTCDate() + days); // 만료일 설정
  cookies.set(name, value, { path: "/", expires });
  
};

/** 쿠키 가져오기 */
export const getCookie = (name) => {
  return cookies.get(name);
};

/** 쿠키 삭제 */
export const removeCookie = (name, path = "/") => {
  cookies.remove(name, { path });
};