import { Cookies } from "react-cookie";

const cookies = new Cookies();

/** 쿠키 저장 */
export const setCookie = (name: string, value: string, days: number) => {
  const expires = new Date();
  expires.setUTCDate(expires.getUTCDate() + days); // 만료일 설정
  cookies.set(name, value, { path: "/", expires });
};

/** 쿠키 가져오기 */
export const getCookie = (name: string) => {
  return cookies.get(name);
};

/** 쿠키 삭제 */
export const removeCookie = (name: string, path = "/") => {
  cookies.remove(name, { path });
};