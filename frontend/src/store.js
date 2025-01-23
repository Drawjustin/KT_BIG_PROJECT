import { configureStore } from '@reduxjs/toolkit';
import loginReducer from './slices/loginSlice';

const store = configureStore({
  reducer: {
    login: loginReducer, // 로그인 상태 관리 리듀서
  },
});

// RootState와 AppDispatch는 TypeScript 전용이므로 삭제
// export type RootState = ReturnType<typeof store.getState>;
// export type AppDispatch = typeof store.dispatch;

export default store;