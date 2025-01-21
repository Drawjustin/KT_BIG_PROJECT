import { createSlice, PayloadAction } from '@reduxjs/toolkit';
//타입 
interface LoginState {
  email: string;
  password: string;
  isLoggedIn: boolean;
}

//초기화
const initialState: LoginState = {
  email: '',
  password: '',
  isLoggedIn: false,
};


const loginSlice = createSlice({
  name: 'loginSlice',
  initialState,
  reducers: {
    setLoginState(state, action: PayloadAction<{ email: string; password: string }>) {
      state.email = action.payload.email;
      state.password = action.payload.password;
      state.isLoggedIn = true;
    },
    logout(state) {
      state.email = '';
      state.password = '';
      state.isLoggedIn = false;
    },
  },
});

export const { setLoginState, logout } = loginSlice.actions;

export default loginSlice.reducer;
