  import React, { useState, useEffect } from "react";
  import { useNavigate, useParams } from "react-router-dom";
  import jwtAxios from "../../util/jwtUtils";
  //import axios from "axios";

  interface FormState {

    title: string;
    content: string;
    file: File | null; // File 타입, 단일 파일만 처리
  }

  const BoardForm: React.FC<{ isEdit: boolean }> = ({ isEdit }) => {
    const [form, setForm] = useState<FormState>({
      title: "",
      content: "",
      file: null 
    });

    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate(); 

    useEffect(() => {
      const fetchPost = async () => {
        if (isEdit && id) {
          try {
            const response = await jwtAxios.get(`/complaints/${id}`);
            setForm({
              title : response.data.title,
              content : response.data.content,
              file :null
            })
          } catch (error) {
            alert("게시글을 불러오는 중 오류가 발생했습니다.");
            console.error(error);
          }
        }
      };
      fetchPost();
    }, [isEdit, id]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
      const { name, value } = e.target;
      setForm((prevForm) => ({
        ...prevForm,
        [name]: value,
      }));
    };

    // 파일 처리 수정
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const file = e.target.files ? e.target.files[0] : null;
      setForm(prevForm => ({
        ...prevForm,
        file
      }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
      e.preventDefault();
      console.log('Form data being sent:', {
        title: form.title,
        content: form.content,
        file: form.file
      });

      const formData = new FormData();
      formData.append("title", form.title);
      formData.append("content", form.content);
      if (form.file) {
        formData.append("file", form.file);
      }
      try {
        if (isEdit && id) {
          await jwtAxios.put(`/complaints/${id}`, formData, {
            headers: { "Content-Type": "multipart/form-data" },
          });
          alert("게시글이 수정되었습니다.");
        } else {
          await jwtAxios.post("/complaints", formData, {
            headers: { "Content-Type": "multipart/form-data" },
          });
          alert("게시글이 등록되었습니다.");
        }
        window.location.href = '/board';
      } catch (error) {
        alert(isEdit ? "게시글 수정 중 오류가 발생했습니다." : "게시글 등록 중 오류가 발생했습니다.");
        console.error(error);
      }
    };

    const handleDelete = async () => {
      if (window.confirm("정말 삭제하시겠습니까?")) {
        try {
          await jwtAxios.delete(`/complaints/${id}`);
          alert("게시글이 삭제되었습니다.");
          navigate("/board");
        } catch (error) {
          alert("게시글 삭제 중 오류가 발생했습니다.");
          console.error(error);
        }
      }
    };

    return (
      <div className="board-form">
        <h1>{isEdit ? "게시글 수정" : "게시글 등록"}</h1>
        <form onSubmit={handleSubmit}>
          <div>
            <label htmlFor="title">제목:</label>
            <input
              type="text"
              id="title"
              name="title"
              value={form.title}
              onChange={handleChange}
              required
            />
          </div>
          <div>
            <label htmlFor="content">내용:</label>
            <textarea
              id="content"
              name="content"
              value={form.content}
              onChange={handleChange}
              required
            />
          </div>
          <div>
            <label htmlFor="files">파일 업로드:</label>
            <input
              type="file"
              id="files"
              name="files"
              onChange={handleFileChange}
              multiple
            />
          </div>
          <div>
            <button type="submit">{isEdit ? "수정" : "등록"}</button>
            {isEdit && (
              <button type="button" onClick={handleDelete}>
                삭제
              </button>
            )}
            <button type="button" onClick={() => navigate("/board")}>
              취소
            </button>
          </div>
        </form>
      </div>
    );
  };

  export default BoardForm;

