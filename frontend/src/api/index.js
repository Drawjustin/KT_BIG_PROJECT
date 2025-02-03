// src/api/index.js
import apiClient from './apiClient';

/**회원관련 api registar, login */
export const memberApi = {
  // 회원가입
  register: (userData) => 
    apiClient.post('/api/join', userData),
  
  // 로그인
  login: (loginData) => 
    apiClient.post('/api/login', loginData),
};

/** 민원 도우미 관련 api */
export const complaintApi = {
  // 민원 목록 조회
  getList: (departmentSeq,pageNumber, pageSize) => 
    apiClient.get(`/complaint-comments/departmentSeq=${departmentSeq}&page=${pageNumber}&size=${pageSize}`),
  
  // 민원 상세 조회
  getDetail: (complaintSeq) => 
    apiClient.get(`/complaint-comments/${complaintSeq}`),
  
  // 답변 등록
  create: (postData) => 
    apiClient.post('/complaints', postData),

  //민원 수정
  modified: (complaintSeq,postData) => 
    apiClient.put(`/complaint-comments/${complaintSeq}`, postData),
  //민원 삭제
  delete: (complaintSeq) =>
    apiClient.delete(`/complaint-comments/${complaintSeq}`),

};

// 공문서 관련 API -> 공문서 아직 ,,
export const documentApi = {
  // 공문서 검색
  search: (params) => 
    apiClient.get('/api/documents/search', { params }),
  
  // 공문서 상세 조회
  getDetail: (id) => 
    apiClient.get(`/api/documents/${id}`)
};

/**자료실 관련 api */
export const dataroomApi = {
    // 자료실 목록 조회
    getList: (departmentSeq,pageNumber, pageSize) => 
      apiClient.get(`files/departmentSeq=${departmentSeq}&page=${pageNumber}&size=${pageSize}`),
    
    // 자료실 단건 조회
    getDetail: (fileSeq) => 
      apiClient.get(`files/${fileSeq}}`),
  };
  