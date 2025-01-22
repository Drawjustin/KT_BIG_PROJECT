import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { mockPosts as initialPosts } from "../../mock/posts";

interface Post {
  id: number;
  title: string;
  body: string;
  author: string;
  createdAt: string;
}

interface BoardFormProps {
  isEdit: boolean; // 등록/수정 구분
}
/** */
const BoardForm: React.FC<BoardFormProps> = ({ isEdit }) => {
  const { id } = useParams<{ id: string }>(); // 수정 시 게시글 ID
  const navigate = useNavigate();

  const [posts, setPosts] = useState(initialPosts); // mockPosts를 상태로 관리
  const [form, setForm] = useState<Omit<Post, "id" | "createdAt">>({
    title: "",
    body: "",
    author: "",
  });

  //(mock) 수정 모드일 경우 Mock 데이터에서 기존 데이터 불러오기
  useEffect(() => {
    if (isEdit && id) {
      const foundPost = posts.find((post) => post.id === Number(id)); // ID로 게시글 찾기
      if (foundPost) {
        setForm({ title: foundPost.title, body: foundPost.body, author: foundPost.author});
      } else {
        alert("게시글을 찾을 수 없습니다.");
        navigate("/board"); // 게시글이 없을 경우 목록으로 이동
      }
    }
  }, [isEdit, id, posts, navigate]);

//  // 수정시, 기존 데이터 불러오기
//  useEffect(() => {
//   if (isEdit && id) {
//     axios
//       .get(`/api/posts/${id}`)
//       .then((response) => {
//         const { title, body, author } = response.data;
//         setForm({ title, body, author });
//       })
//       .catch(() => {
//         alert("게시글을 찾을 수 없습니다.");
//         navigate("/board");
//       });
//   }
// }, [isEdit, id, navigate]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };



  // (mock)제출 핸들러
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const currentDate = new Date().toISOString().split("T")[0]; // YYYY-MM-DD 형식의 날짜

    if (isEdit && id) {
      // 수정 모드
      const updatedPosts = posts.map((post) =>
        post.id === Number(id)
          ? { ...post, title: form.title, body: form.body }
          : post
      );
      setPosts(updatedPosts); // 상태 업데이트
      alert("게시글이 수정되었습니다.");
    } else {
      // 등록 모드
      const newPost: Post = {
        id: posts.length + 1, // 새로운 ID
        title: form.title,
        body: form.body,
        author: form.author,
        createdAt: currentDate
      };
      setPosts([...posts, newPost]); // 새 게시글 추가
      alert("게시글이 등록되었습니다.");
    }

    navigate("/board");
   };
// // 제출 핸들러
//   const handleSubmit = async (e: React.FormEvent) => {
//     e.preventDefault();

//     if (isEdit && id) {
//       // 수정 API 호출
//       axios
//         .put(`/api/posts/${id}`, form)
//         .then(() => {
//           alert("게시글이 수정되었습니다.");
//           navigate("/board");
//         })
//         .catch(() => {
//           alert("게시글 수정 중 오류가 발생했습니다.");
//         });
//     } else {
//       // 등록 API 호출
//       axios
//         .post("/api/posts", form)
//         .then(() => {
//           alert("게시글이 등록되었습니다.");
//           navigate("/board");
//         })
//         .catch(() => {
//           alert("게시글 등록 중 오류가 발생했습니다.");
//         });
//     }
//   };


  // (mock)삭제 핸들러
  const handleDelete = () => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      const updatedPosts = posts.filter((post) => post.id !== Number(id)); // 해당 ID를 제외한 게시글 리스트
      setPosts(updatedPosts); // 상태 업데이트
      alert("게시글이 삭제되었습니다.");
      navigate("/board"); // 삭제 후 목록으로 이동
    }
  };

    // // 삭제 핸들러
    // const handleDelete = async () => {
    //   if (window.confirm("정말 삭제하시겠습니까?")) {
    //     try {
    //       await axios.delete(`/api/posts/${id}`);
    //       alert("게시글이 삭제되었습니다.");
    //       navigate("/board");
    //     } catch (error) {
    //       alert("게시글 삭제 중 오류가 발생했습니다.");
    //     }
    //   }
    // };


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
      <div>
        <button type="submit">{isEdit ? "수정 완료" : "등록 완료"}</button>
        {isEdit && (
          <button
            type="button"
            onClick={handleDelete}
            style={{
              marginLeft: "10px",
              backgroundColor: "red",
              color: "white",
            }}
          >
            삭제
          </button>
        )}
      </div>
    </form>
  );
};

export default BoardForm;
