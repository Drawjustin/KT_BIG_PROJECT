import { useDispatch } from 'react-redux';
import type { AppDispatch } from '../store'; // store.ts에서 내보낸 AppDispatch 타입을 가져옵니다.

export const useAppDispatch: () => AppDispatch = useDispatch;