import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { setCookie, removeCookie, getCookie } from '../util/cookieUtils'; // cookieUtils를 import

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

// const loadMembarCookie = () => { //쿠키에서 로그인 정보 로딩
//   const memberInfo = getCookie("member")

//   //닉네임 처리
//   if (memberInfo && memberInfo.nickname){
//     memberInfo.nickname = decodeURIComponent(memberInfo.nickname)
//   }
//   return memberInfo
// }

// export const loginPostAsync = createAsyncThunk('loginPostAsync', (param)
// =>{
//   return loginPostAsync(param)
// })

const loginSlice = createSlice({
  name: 'loginSlice',
  initialState,
  reducers: {
    // 로그인 상태 업데이트
    setLoginState(state, action: PayloadAction<{ email: string; password: string }>) {
      const { email, password } = action.payload;
      state.email = email;
      state.password = password;
      state.isLoggedIn = true;

      // 쿠키에 로그인 정보 저장 (쿠키 이름: 'member')
      setCookie('member', JSON.stringify({ email, isLoggedIn: true }), 7); // 유효기간 7일이라고 설정했지만, refresh token과 동일하게 변경 예정.
    },
    //(로그아웃) 로그인 상태를 초기화
    logout(state) {
      state.email = '';
      state.password = '';
      state.isLoggedIn = false;

      // 쿠키에서 로그인 정보 제거
      removeCookie('member');

      return {...initialState}
    },
  },
});

export const { setLoginState, logout } = loginSlice.actions;

export default loginSlice.reducer;