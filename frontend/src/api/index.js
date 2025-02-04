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
/** 민원 도우미 관련 api */
export const complaintApi = {
  // 민원 목록 조회
  getList: (departmentSeq,pageNumber, pageSize) => 
    jwtAxios.get('/complaint-comments', {
        params: { departmentSeq, page: pageNumber, size: pageSize }
      }),  
  
  // 민원 상세 조회
  getDetail: (complaintSeq) => 
    jwtAxios.get(`/complaint-comments/${complaintSeq}`),
  
  // 답변 등록
  create: (postData) => 
    jwtAxios.post('/complaint-comments', postData),

  //민원 수정
  update: (complaintSeq,postData) => 
    jwtAxios.put(`/complaint-comments/${complaintSeq}`, postData),
  //민원 삭제
  remove: (complaintSeq) =>
    jwtAxios.delete(`/complaint-comments/${complaintSeq}`),

};
/** ai 답변생성 api */
export const commentsApi = {
  

};

// 공문서 관련 API -> 공문서 아직 ,,
export const documentApi = {
  // 공문서 검색
  search: (params) => 
    jwtAxios.get('/api/documents/search', { params }),
  
  // 공문서 상세 조회
  getDetail: (id) => 
    jwtAxios.get(`/api/documents/${id}`)
};

/**자료실 관련 api */
export const dataroomApi = {
    // 자료실 목록 조회 (검색 포함)
  getList: (params) => 
    jwtAxios.get('/files', { params }), 

    // 자료실 단건 조회
    getDetail: (fileSeq) => 
      jwtAxios.get(`files/${fileSeq}}`),
  };
  