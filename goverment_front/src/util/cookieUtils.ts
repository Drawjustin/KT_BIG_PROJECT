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


/** 로그인 후 쿠키에 accessToken저장 */
export const setMemberCookie = (accessToken: string, days: number = 7) => {
  const memberData = JSON.stringify({ accessToken});
  setCookie("member", memberData, days);
};

/** 로그인 후 쿠키에서 member 정보 가져오기 */
export const getMemberCookie = () => {
  const memberData = getCookie("member");
  return memberData ? JSON.parse(memberData) : null;
};