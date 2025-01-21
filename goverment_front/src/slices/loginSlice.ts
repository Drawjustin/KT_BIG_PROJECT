import { createSlice, PayloadAction } from '@reduxjs/toolkit';
//타입 정의
interface LoginState {
  email: string;
  password: string;
  isLoggedIn: boolean;
}

//상태 초기값
const initialState: LoginState = {
  email: '',
  password: '',
  isLoggedIn: false,
};

const loginSlice = createSlice({
  name: 'loginSlice',
  initialState,
  reducers: {
    // 로그인 상태 업데이트
    setLoginState(state, action: PayloadAction<{ email: string; password: string }>) { 
      state.email = action.payload.email;
      state.password = action.payload.password;
      state.isLoggedIn = true;
    },
    //(로그아웃) 로그인 상태를 초기화
    logout(state) {
      state.email = '';
      state.password = '';
      state.isLoggedIn = false;
    },
  },
});

export const { setLoginState, logout } = loginSlice.actions;

export default loginSlice.reducer;
