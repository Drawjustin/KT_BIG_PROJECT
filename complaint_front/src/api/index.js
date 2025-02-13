// src/api/index.js
import jwtAxios from '../util/jwtUtils';
import axios from 'axios';

// 공통 axios
const apiClient = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    timeout: 5000,
    headers: {
      'Content-Type': 'application/json'
    }
  });

/**axois 회원관련 api registar,  */
export const signApi = {
  // 회원가입
  register: (userData) => 
    apiClient.post('/api/join', userData),
};

// jwt 인증 필요한 api
/** 민원 게시판 관련 api */
export const complaintApi = {
  // 민원 목록 조회
  getList: (params) => jwtAxios.get('/complaints', { params }),
  // 민원 상세 조회
  getDetail: (complaintSeq) => 
    jwtAxios.get(`/complaints/${complaintSeq}`),
  
  // 등록
  create: (postData) => 
    jwtAxios.post('/complaints', postData,{
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }),

  // 수정
  update: (complaintSeq,postData) => 
    jwtAxios.put(`/complaints/${complaintSeq}`, postData),
  // 삭제
  remove: (complaintSeq) =>
    jwtAxios.delete(`/complaints/${complaintSeq}`),

};
