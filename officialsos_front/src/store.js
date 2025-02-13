import { configureStore } from '@reduxjs/toolkit';
import loginReducer from './slices/loginSlice';

const store = configureStore({
  reducer: {
    login: loginReducer, // 로그인 상태 관리 리듀서
  },
});
export default store;