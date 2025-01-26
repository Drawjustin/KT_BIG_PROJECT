import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import jwtAxios from "../../util/jwtUtils";

interface FormState {
  title: string;
  body: string;
  userEmail: string;
  files: File[]; // File 타입으로 수정
}

const BoardForm: React.FC<{ isEdit: boolean }> = ({ isEdit }) => {
  const [form, setForm] = useState<FormState>({
    title: "",
    body: "",
    userEmail: "",
    files: [], // 초기값 File[]
  });

  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPost = async () => {
      if (isEdit && id) {
        try {
          const response = await jwtAxios.get(`/complaint/${id}`);
          const { title, body, userEmail } = response.data;
          setForm({ title, body, userEmail, files: [] }); // files 초기화
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

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files ? Array.from(e.target.files) : [];
    setForm((prevForm) => ({
      ...prevForm,
      files, // File[] 타입으로 설정
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("title", form.title);
    formData.append("content", form.body);
    formData.append("memberSeq", form.userEmail);
    form.files.forEach((file) => formData.append("files", file));

    try {
      if (isEdit && id) {
        await jwtAxios.put(`/complaint/${id}`, formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        alert("게시글이 수정되었습니다.");
      } else {
        await jwtAxios.post("/complaint", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        alert("게시글이 등록되었습니다.");
      }
      navigate("/board");
    } catch (error) {
      alert(isEdit ? "게시글 수정 중 오류가 발생했습니다." : "게시글 등록 중 오류가 발생했습니다.");
      console.error(error);
    }
  };

  const handleDelete = async () => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      try {
        await jwtAxios.delete(`/complaint/${id}`);
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
          <label htmlFor="body">내용:</label>
          <textarea
            id="body"
            name="body"
            value={form.body}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label htmlFor="author">작성자:</label>
          <input
            type="text"
            id="author"
            name="author"
            value={form.userEmail}
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


// import React, { useState, useEffect } from "react";
// import { useNavigate, useParams } from "react-router-dom";
// // import { mockPosts as initialPosts } from "../../mock/posts";
// import axios from "axios";

// interface Post {
//   id: number;
//   title: string;
//   body: string;
//   author: string;
//   createdAt: string;
// }

// interface BoardFormProps {
//   isEdit: boolean; // 등록/수정 구분
// }

// /** 등록 수정 삭제*/
// const BoardForm: React.FC<BoardFormProps> = ({ isEdit }) => {
//   const { id } = useParams<{ id: string }>(); // 수정 시 게시글 ID
//   const navigate = useNavigate();

//   //const [posts, setPosts] = useState(initialPosts); // (mockPosts)를 상태로 관리
//   const [form, setForm] = useState<
//     Omit<Post, "id" | "createdAt"> & { files: File[] }
//   >({
//     title: "",
//     body: "",
//     author: "",
//     files: [], // 파일 정보를 추가
//   });

//   // //(mock) 수정 모드일 경우 Mock 데이터에서 기존 데이터 불러오기
//   // useEffect(() => {
//   //   if (isEdit && id) {
//   //     const foundPost = posts.find((post) => post.id === Number(id)); // ID로 게시글 찾기
//   //     if (foundPost) {
//   //       setForm({
//   //         title: foundPost.title,
//   //         body: foundPost.body,
//   //         author: foundPost.author,
//   //         files: foundPost.files || [],

//   //       });
//   //     } else {
//   //       alert("게시글을 찾을 수 없습니다.");
//   //       navigate("/board"); // 게시글이 없을 경우 목록으로 이동
//   //     }
//   //   }
//   // }, [isEdit, id, posts, navigate]);

// // (api) 수정 모드일 경우 기존 데이터 불러오기
// useEffect(() => {
//   if (isEdit && id) {
//     axios
//       .get(`/api/posts/${id}`)
//       .then((response) => {
//         const { title, body, author, files } = response.data;
//         setForm({
//           title,
//           body,
//           author,
//           files: files || [],
//         });
//       })
//       .catch(() => {
//         alert("게시글을 찾을 수 없습니다.");
//         navigate("/board");
//       });
//   }
// }, [isEdit, id, navigate]); // (!) useEffect 의존성 배열에서 navigate를 제거고려

//   const handleChange = (
//     e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
//   ) => {
//     const { name, value } = e.target;
//     setForm({ ...form, [name]: value });
//   };
  
//   /** 파일 선택 핸들러  */
//   const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => { 
//     if (e.target.files) {
//       const selectedFiles = Array.from(e.target.files);

//       // 파일 크기 및 형식 제한
//       const isValid = selectedFiles.every((file) => {
//         if (file.size > 10 * 1024 * 1024) {
//           // 10MB 크기 제한
//           alert(`파일 ${file.name}의 크기가 너무 큽니다.`);
//           return false;
//         }
//         if (
//           !["image/jpeg", "image/png", "application/pdf"].includes(file.type) //파일 형식 제한 
//         ) {
//           alert(`파일 ${file.name}의 형식이 지원되지 않습니다.`);
//           return false;
//         }
//         return true;
//       });

//       if (isValid) {
//         setForm({ ...form, files: selectedFiles });
//       }
//     }
//   };

//   // // (mock)제출 핸들러
//   // const handleSubmit = (e: React.FormEvent) => {
//   //   e.preventDefault();

//   //   const currentDate = new Date().toISOString().split("T")[0]; // YYYY-MM-DD 형식의 날짜

//   //   if (isEdit && id) {
//   //     // 수정 모드
//   //     const updatedPosts = posts.map((post) =>
//   //       post.id === Number(id)
//   //         ? { ...post, title: form.title, body: form.body }
//   //         : post
//   //     );
//   //     setPosts(updatedPosts); // 상태 업데이트
//   //     alert("게시글이 수정되었습니다.");
//   //   } else {
//   //     // 등록 모드
//   //     const newPost: Post = {
//   //       id: posts.length + 1, // 새로운 ID
//   //       title: form.title,
//   //       body: form.body,
//   //       author: form.author,
//   //       createdAt: currentDate,
//   //     };
//   //     setPosts([...posts, newPost]); // 새 게시글 추가
//   //     alert("게시글이 등록되었습니다.");
//   //   }

//   //   navigate("/board");
//   // };

//   // // (api)제출 핸들러---> 아래 코드와 겹침
//   //   const handleSubmit = async (e: React.FormEvent) => {
//   //     e.preventDefault();

//   //     if (isEdit && id) {
//   //       // 수정 API 호출
//   //       axios
//   //         .put(`/api/posts/${id}`, form)
//   //         .then(() => {
//   //           alert("게시글이 수정되었습니다.");
//   //           navigate("/board");
//   //         })
//   //         .catch(() => {
//   //           alert("게시글 수정 중 오류가 발생했습니다.");
//   //         });
//   //     } else {
//   //       // 등록 API 호출
//   //       axios
//   //         .post("/api/posts", form)
//   //         .then(() => {
//   //           alert("게시글이 등록되었습니다.");
//   //           navigate("/board");
//   //         })
//   //         .catch(() => {
//   //           alert("게시글 등록 중 오류가 발생했습니다.");
//   //         });
//   //     }
//   //   };

//   //(api) + 파일 업로드 버전 + 제출 핸들러
//   const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();
  
//     // FormData 객체 생성
//     const formData = new FormData();
//     formData.append("title", form.title); // 텍스트 데이터 추가
//     formData.append("body", form.body);
//     formData.append("author", form.author);
//     form.files.forEach((file) => formData.append("files", file)); // 파일 추가
  
//     try {
//       if (isEdit && id) {
//         // 수정 API 호출
//         await axios.put(`/api/posts/${id}`, formData, {
//           headers: { "Content-Type": "multipart/form-data" }, // 헤더 설정
//         });
//         alert("게시글이 수정되었습니다.");
//       } else {
//         // 등록 API 호출
//         await axios.post("/api/posts", formData, {
//           headers: { "Content-Type": "multipart/form-data" }, // 헤더 설정
//         });
//         alert("게시글이 등록되었습니다.");
//       }
//       navigate("/board"); // 성공 시 목록으로 이동
//     } catch (error) {
//       alert(isEdit ? "게시글 수정 중 오류가 발생했습니다." : "게시글 등록 중 오류가 발생했습니다.");
//       console.error(error);
//     }
//   };

//   // // (mock)삭제 핸들러
//   // const handleDelete = () => {
//   //   if (window.confirm("정말 삭제하시겠습니까?")) {
//   //     const updatedPosts = posts.filter((post) => post.id !== Number(id)); // 해당 ID를 제외한 게시글 리스트
//   //     setPosts(updatedPosts); // 상태 업데이트
//   //     alert("게시글이 삭제되었습니다.");
//   //     navigate("/board"); // 삭제 후 목록으로 이동
//   //   }
//   // };

//   // (api) 삭제 핸들러
//   const handleDelete = async () => {
//     if (window.confirm("정말 삭제하시겠습니까?")) {
//       try {
//         await axios.delete(`/api/posts/${id}`);
//         alert("게시글이 삭제되었습니다.");
//         navigate("/board");
//       } catch (error) {
//         alert("게시글 삭제 중 오류가 발생했습니다.");
//       }
//     }
//   };

//   return (
//     <form onSubmit={handleSubmit}>
//       <h1>{isEdit ? "게시글 수정" : "게시글 등록"}</h1>
//       <label>
//         제목:
//         <input
//           type="text"
//           name="title"
//           value={form.title}
//           onChange={handleChange}
//           placeholder="제목을 입력하세요"
//         />
//       </label>
//       <label>
//         내용:
//         <textarea
//           name="body"
//           value={form.body}
//           onChange={handleChange}
//           placeholder="내용을 입력하세요"
//         />
//       </label>
//       <label> {/**file 업로드 코드 */}
//         파일 업로드:
//         <input
//           type="file"
//           multiple
//           onChange={handleFileChange}
//           accept="image/*,application/pdf" // 허용할 파일 형식
//         />
//       </label>
//       {form.files.length > 0 && (
//         <div>
//           <h3>업로드된 파일:</h3>
//           <ul>
//             {form.files.map((file, index) => (
//               <li key={index}>{file.name}</li>
//             ))}
//           </ul>
//         </div>
//       )}

//       <div>
//         <button type="submit">{isEdit ? "수정 완료" : "등록 완료"}</button>
//         {isEdit && (
//           <button
//             type="button"
//             onClick={handleDelete}
//             style={{
//               marginLeft: "10px",
//               backgroundColor: "red",
//               color: "white",
//             }}
//           >
//             삭제
//           </button>
//         )}
//       </div>
//     </form>
//   );
// };

// export default BoardForm;
