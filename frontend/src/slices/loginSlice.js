// 로그인 상태 관리를 위한 Redux 슬라이스 파일(비동기액션, 로그인정보, 에러상태)
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { setCookie, removeCookie, getCookie } from "../util/cookieUtils";
import { loginPost, logoutPost } from "../api/memberApi";
import axios from "axios"; // AxiosError를 가져옵니다.

// 초기 상태
const initialState = {
  email: "",
  isLoggedIn: false,
  loading: false,
  error: null,
};

/** 로그인 비동기 요청 */
export const loginPostAsync = createAsyncThunk(
  "loginPostAsync",
  async (loginParam, { rejectWithValue }) => {
    try {
      const response = await loginPost(loginParam); // memberApi에서 가져온 loginPost 호출
      const { email, accessToken } = response;

      // 쿠키에 이메일과 토큰 저장
      setCookie("member", JSON.stringify({ email, accessToken }), 7); // 토큰 저장 (유효기간 7일)

      return { email, accessToken };
    } catch (error) {
      // AxiosError 타입으로 명시했던 부분을 일반적인 조건문으로 처리
      if (axios.isAxiosError(error)) {
        const message = error.response?.data?.message || "로그인 요청 실패";
        return rejectWithValue(message);
      }
      // 일반적인 오류 처리
      return rejectWithValue("알 수 없는 오류가 발생했습니다.");
    }
  }
);
/**로그아웃 비동기요청 */
export const logoutAsync = createAsyncThunk(
  "logoutAsync",
  async (_, { getState, rejectWithValue }) => {
    try {
      const { email } = getState().login;
      const { accessToken } = JSON.parse(getCookie("member"));
      const response = await logoutPost(email, accessToken);
      if (response.status === "success") {
        removeCookie("member");
        return response;
      }
      return rejectWithValue(response.message || "로그아웃 실패");
    } catch (error) {
      console.log("logout fail",error)
      return rejectWithValue("로그아웃 중 오류가 발생했습니다.");
    }
  }
);
const loginSlice = createSlice({
  name: "loginSlice",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(loginPostAsync.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(loginPostAsync.fulfilled, (state, action) => {
        state.loading = false;
        state.isLoggedIn = true;
        state.email = action.payload.email;
      })
      .addCase(loginPostAsync.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "로그인 실패";
      })
      //로그아웃 비동기 호출을 위한 케이스 
      .addCase(logoutAsync.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(logoutAsync.fulfilled, (state) => {
        state.loading = false;
        state.isLoggedIn = false;
        state.email = "";
        state.error = null;
      })
      .addCase(logoutAsync.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload || "로그아웃 실패";
      })
  },
});

export const { logout } = loginSlice.actions;

export default loginSlice.reducer;