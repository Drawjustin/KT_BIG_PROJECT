import axios, { InternalAxiosRequestConfig, AxiosResponse, AxiosError } from "axios";
import { getCookie } from "./cookieUtils";

const jwtAxios = axios.create();

/** 요청 전 처리 */
const beforeReq = (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig | Promise<InternalAxiosRequestConfig> => {
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
const requestFail = (err: AxiosError): Promise<AxiosError> => {
  console.log("request error...");
  return Promise.reject(err);
};

/** 응답 성공 처리 */
const beforeRes = (res: AxiosResponse): AxiosResponse => {
  console.log("before return response...");
  return res;
};

/** 응답 실패 처리 */
const responseFail = (err: AxiosError): Promise<AxiosError> => {
  console.log("response fail error...");
  return Promise.reject(err);
};

// 인터셉터 설정
jwtAxios.interceptors.request.use(beforeReq, requestFail);
jwtAxios.interceptors.response.use(beforeRes, responseFail);

export default jwtAxios;