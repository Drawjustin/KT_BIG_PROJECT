import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import styles from "./BoardForm.module.css";
import { complaintApi } from "../../api";
import PropTypes from "prop-types";

const BoardForm = ({ isEdit = false }) => {
  const [form, setForm] = useState({
    title: "",
    content: "",
    file: null,
  });

  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPost = async () => {
      if (isEdit && id) {
        try {
          const response = await complaintApi.getDetail(id);
          setForm({
            title: response.data.title,
            content: response.data.content,
            file: null,
          });
        } catch (error) {
          alert("게시글을 불러오는 중 오류가 발생했습니다.");
          console.error(error);
        }
      }
    };
    fetchPost();
  }, [isEdit, id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prevForm) => ({
      ...prevForm,
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files ? e.target.files[0] : null;
    setForm((prevForm) => ({
      ...prevForm,
      file,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("title", form.title);
    formData.append("content", form.content);

    if (form.file) {
      formData.append("file", form.file);
    }

    try {
      if (isEdit && id) {
        await complaintApi.update(id, formData);
        alert("게시글이 수정되었습니다.");
      } else {
        const response = await complaintApi.create(formData);
        console.log('서버 응답:', response);

        
        alert("게시글이 등록되었습니다.");
      }
      navigate("/complaints");
    } catch (error) {
      // 상세 오류 로깅
      console.error("제출 중 오류 상세 정보:", {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
        headers: error.response?.headers,
      });

      // 사용자에게 보여줄 오류 메시지
      const errorMessage =
        error.response?.data?.message ||
        (isEdit
          ? "게시글 수정 중 오류가 발생했습니다."
          : "게시글 등록 중 오류가 발생했습니다.");

      alert(errorMessage);
    }
  };
  const handleDelete = async () => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      try {
        await complaintApi.remove(id);
        alert("게시글이 삭제되었습니다.");
        navigate("/complaints");
      } catch (error) {
        alert("게시글 삭제 중 오류가 발생했습니다.");
        console.error(error);
      }
    }
  };

  return (
    <div className={styles.boardForm}>
      <div className={styles.boardFormHeader}>
        <div className={styles.boardFormHeaderTitle}>
          {isEdit ? "민원 수정" : "민원 등록"}
        </div>
      </div>

      <div className={styles.boardFormContent}>
        <form onSubmit={handleSubmit}>
          <div className={styles.boardFormInputGroup}>
            
          </div>
          <div className={styles.boardFormInputGroup}>
            <label htmlFor="title" className={styles.boardFormLabel}>
              민원 제목
            </label>
            <input
              type="text"
              id="title"
              name="title"
              value={form.title}
              onChange={handleChange}
              className={styles.boardFormInput}
              required
            />
          </div>

          <div className={styles.boardFormInputGroup}>
            <label htmlFor="content" className={styles.boardFormLabel}>
              민원 내용
            </label>
            <textarea
              id="content"
              name="content"
              value={form.content}
              onChange={handleChange}
              className={styles.boardFormTextarea}
              required
            />
          </div>

          <div className={styles.boardFormInputGroup}>
            <label htmlFor="files" className={styles.boardFormLabel}>
              파일 업로드
            </label>
            <input
              type="file"
              id="files"
              name="files"
              onChange={handleFileChange}
              className={styles.boardFormInput}
              multiple
            />
          </div>

          <div className={styles.boardFormActions}>
            <button
              type="button"
              onClick={() => navigate("/complaints")}
              className={`${styles.boardFormButton} ${styles.cancel}`}
            >
              취소
            </button>

            {isEdit && (
              <button
                type="button"
                onClick={handleDelete}
                className={`${styles.boardFormButton} ${styles.delete}`}
              >
                삭제
              </button>
            )}

            <button
              type="submit"
              className={`${styles.boardFormButton} ${styles.submit}`}
            >
              {isEdit ? "수정" : "등록"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
BoardForm.propTypes = {
  isEdit: PropTypes.bool, // 부울 타입으로 정의
};

export default BoardForm;
