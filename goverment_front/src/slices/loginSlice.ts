  import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
  import { setCookie, removeCookie } from "../util/cookieUtils";
  import { loginPost } from "../api/memberApi";
  import axios from "axios"; // AxiosError를 가져옵니다.

  // 서버에서 반환되는 데이터 타입
  interface LoginResponse {
    userEmail: string;
    accessToken: string;
  }

  // 로그인 요청에 필요한 데이터 타입
  interface LoginCredentials {
    userEmail: string;
    userPassword: string;
  }

  // 상태 타입
  interface LoginState {
    userEmail: string;
    isLoggedIn: boolean;
    loading: boolean;
    error: string | null;
  }

  // 초기 상태
  const initialState: LoginState = {
    userEmail: "",
    isLoggedIn: false,
    loading: false,
    error: null,
  };

  // 비동기 로그인 요청
  export const loginPostAsync = createAsyncThunk<
    LoginResponse,
    LoginCredentials,
    { rejectValue: string }
  >(
    "loginPostAsync",
    async (loginParam, { rejectWithValue }) => {
      try {
        const response = await loginPost(loginParam); // memberApi에서 가져온 loginPost 호출
        const { userEmail, accessToken } = response.data;

        // 쿠키에 이메일과 토큰 저장
        setCookie("member", JSON.stringify({ userEmail, accessToken }), 7); // 토큰 저장 (유효기간 7일)

        return { userEmail, accessToken };
      } catch (error) {
        // AxiosError 타입으로 명시
        if (axios.isAxiosError(error)) {
          const message = error.response?.data?.message || "로그인 요청 실패";
          return rejectWithValue(message);
        }
        // 일반적인 오류 처리
        return rejectWithValue("알 수 없는 오류가 발생했습니다.");
      }
    }
  );

  const loginSlice = createSlice({
    name: "loginSlice",
    initialState,
    reducers: {
      logout(state) {
        state.userEmail = "";
        state.isLoggedIn = false;

        // 쿠키에서 로그인 정보 제거
        removeCookie("member");
      },
    },
    extraReducers: (builder) => {
      builder
        .addCase(loginPostAsync.pending, (state) => {
          state.loading = true;
          state.error = null;
        })
        .addCase(loginPostAsync.fulfilled, (state, action: PayloadAction<LoginResponse>) => {
          state.loading = false;
          state.isLoggedIn = true;
          state.userEmail = action.payload.userEmail;
        })
        .addCase(loginPostAsync.rejected, (state, action: PayloadAction<string | undefined>) => {
          state.loading = false;
          state.error = action.payload || "로그인 실패";
        });
    },
  });

  export const { logout } = loginSlice.actions;

  export default loginSlice.reducer;




