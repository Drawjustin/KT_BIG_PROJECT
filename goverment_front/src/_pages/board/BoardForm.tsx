import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { mockPosts } from "../../mock/posts";
//import axios from 'axios';

interface BoardFormProps {
  isEdit: boolean; // 등록/수정 구분
}
/** */
const BoardForm: React.FC<BoardFormProps> = ({ isEdit }) => {
  const { id } = useParams<{ id: string }>(); // 수정 시 게시글 ID
  const navigate = useNavigate();
  const [form, setForm] = useState({ title: "", body: "" });
  // 수정 모드일 경우 Mock 데이터에서 기존 데이터 불러오기
  useEffect(() => {
    if (isEdit && id) {
      const foundPost = mockPosts.find((post) => post.id === Number(id)); // ID로 게시글 찾기
      if (foundPost) {
        setForm({ title: foundPost.title, body: foundPost.body });
      } else {
        alert("게시글을 찾을 수 없습니다.");
        navigate("/board"); // 게시글이 없을 경우 목록으로 이동
      }
    }
  }, [isEdit, id, navigate]);

  // 수정 모드일 경우 기존 데이터 불러오기 :: 백엔드 api연동시.
  //   useEffect(() => {
  //     if (isEdit && id) {
  //       axios.get(`/api/posts/${id}`).then((response) => {
  //         setForm({ title: response.data.title, body: response.data.body });
  //       });
  //     }
  //   }, [isEdit, id]);

  // 입력 값 변경 핸들러
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };
  // 제출 핸들러
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (isEdit) {
        console.log("수정된 데이터:", form); // Mock 데이터로는 실제 서버에 저장하지 않음
        alert("Mock 데이터로 게시글이 수정되었습니다.");
      } else {
        console.log("등록된 데이터:", form); // Mock 데이터로는 실제 서버에 저장하지 않음
        alert("Mock 데이터로 게시글이 등록되었습니다.");
      }
      navigate("/board");
    } catch (error) {
      console.error("저장 실패:", error);
      alert("저장 중 문제가 발생했습니다.");
    }
  };

  //   // 제출 핸들러
  //   const handleSubmit = async (e: React.FormEvent) => {
  //     e.preventDefault();
  //     try {
  //       if (isEdit) {
  //         await axios.put(`/api/posts/${id}`, form);
  //         alert('게시글이 수정되었습니다.');
  //       } else {
  //         await axios.post('/api/posts', form);
  //         alert('게시글이 등록되었습니다.');
  //       }
  //       navigate('/board');
  //     } catch (error) {
  //       console.error('저장 실패:', error);
  //       alert('저장 중 문제가 발생했습니다.');
  //     }
  //   };

  return (
    <form onSubmit={handleSubmit}>
      <h1>{isEdit ? "게시글 수정" : "게시글 등록"}</h1>
      <label>
        제목:
        <input
          type="text"
          name="title"
          value={form.title}
          onChange={handleChange}
          placeholder="제목을 입력하세요"
        />
      </label>
      <label>
        내용:
        <textarea
          name="body"
          value={form.body}
          onChange={handleChange}
          placeholder="내용을 입력하세요"
        />
      </label>
      <button type="submit">{isEdit ? "수정 완료" : "등록 완료"}</button>
    </form>
  );
};

export default BoardForm;
