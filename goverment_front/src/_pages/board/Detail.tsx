import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import axios from 'axios';

interface Post {
 complaintSeq: number;
 memberName: string;
 departmentName: string;
 title: string;
 content: string;
 filePath: string;
 updatedAt: string;
}

const Detail: React.FC = () => {
 const { id } = useParams<{ id: string }>();
 const navigate = useNavigate();
 const [post, setPost] = useState<Post | null>(null);
 const [allPosts, setAllPosts] = useState<Post[]>([]);
 
 const baseURL = 'https://8c21-122-37-19-2.ngrok-free.app';
 const headers = {
   'Accept': 'application/json',
   'Content-Type': 'application/json',
   'ngrok-skip-browser-warning': 'true'
   
 };

 useEffect(() => {
   const fetchPost = async () => {
     try {
       const response = await axios.get(`${baseURL}/complaint/${id}`, { headers });
       const { complaintSeq, memberName, departmentName, title, content, filePath, updatedAt } = response.data;
       setPost({ complaintSeq, memberName, departmentName, title, content, filePath, updatedAt });
     } catch (error) {
       console.error("게시글 조회 오류:", error);
       alert("게시글을 불러오는 중 오류가 발생했습니다.");
     }
   };

   if (id) fetchPost();
 }, [id]);

 useEffect(() => {
  const fetchAllPosts = async () => {
    try {
      const response = await axios.get(`${baseURL}/complaint`, { headers });
      // response.data.content가 배열인지 확인
      const posts = response.data.content || [];
      console.log('받은 데이터:', posts);
      setAllPosts(posts);
    } catch (error) {
      console.error("게시글 목록 조회 오류:", error);
    }
  };

  fetchAllPosts();
}, []);
 if (!post) return <div>Loading...</div>;

 const handleEdit = () => navigate(`/board/edit/${id}`);

 const currentIndex = allPosts.findIndex((p) => p.complaintSeq === post.complaintSeq);
 const prevPost = allPosts[currentIndex - 1] || null;
 const nextPost = allPosts[currentIndex + 1] || null;

 return (
   <div>
     <h1>{post.title}</h1>
     <p>작성자: {post.memberName} ({post.departmentName})</p>
     <p>업데이트일: {post.updatedAt}</p>
     <p>{post.content}</p>
     <p>첨부파일: {post.filePath}</p>
     <button onClick={handleEdit}>수정하기</button>
     
     <div style={{ marginTop: '20px' }}>
       {prevPost ? (
         <Link to={`/board/${prevPost.complaintSeq}`} style={{ marginRight: '10px' }}>
           이전 글: {prevPost.title}
         </Link>
       ) : (
         <span style={{ marginRight: '10px', color: '#ccc' }}>이전 글 없음</span>
       )}

       {nextPost ? (
         <Link to={`/board/${nextPost.complaintSeq}`} style={{ marginRight: '10px' }}>
           다음 글: {nextPost.title}
         </Link>
       ) : (
         <span style={{ marginRight: '10px', color: '#ccc' }}>다음 글 없음</span>
       )}

       <button onClick={() => navigate('/board')}>목록</button>
     </div>
   </div>
 );
};

export default Detail;
// import React, { useState, useEffect } from 'react';
// import { useParams, useNavigate, Link } from 'react-router-dom';
// import jwtAxios from '../../util/jwtUtils';  // jwtAxios를 import 해야 함

// interface Post {
//   complaintSeq: number;
//   memberName: string;
//   departmentName: string;
//   title: string;
//   content: string;
//   filePath: string;
//   updatedAt: string;
// }

// const Detail: React.FC = () => {
//   const { id } = useParams<{ id: string }>(); // URL에서 id를 가져옴
//   const navigate = useNavigate();
//   const [post, setPost] = useState<Post | null>(null);
//   const [allPosts, setAllPosts] = useState<Post[]>([]); // 게시글 목록을 저장할 상태

//   // 백엔드 호출 (게시글 상세 조회)
//   useEffect(() => {
//     const fetchPost = async () => {
//       try {
//         const response = await jwtAxios.get(`/complaint/${id}`);
//         const { complaintSeq, memberName, departmentName, title, content, filePath, updatedAt } = response.data;
//         setPost({
//           complaintSeq,
//           memberName,
//           departmentName,
//           title,
//           content,
//           filePath,
//           updatedAt,
//         });
//       } catch (error) {
//         console.error("게시글을 불러오는 중 오류가 발생했습니다.", error);
//         alert("게시글을 불러오는 중 오류가 발생했습니다.");
//       }
//     };

//     if (id) {
//       fetchPost();
//     }
//   }, [id]);

//   // 백엔드 호출 (게시글 목록 조회)
//   useEffect(() => {
//     const fetchAllPosts = async () => {
//       try {
//         const response = await jwtAxios.get("/complaint");
//         setAllPosts(response.data); // 게시글 목록을 상태에 저장
//       } catch (error) {
//         console.error("게시글 목록을 불러오는 중 오류가 발생했습니다.", error);
//         alert("게시글 목록을 불러오는 중 오류가 발생했습니다.");
//       }
//     };

//     fetchAllPosts();
//   }, []);

//   // 게시글이 로드되지 않았다면 로딩 메시지 표시
//   if (!post) return <div>Loading...</div>;

//   // 수정하기 버튼 클릭 핸들러
//   const handleEdit = () => {
//     navigate(`/board/edit/${id}`); // 수정 페이지로 이동
//   };

//   // 이전 글과 다음 글 찾기 (목록에서 현재 id를 기준으로 찾음)
//   const currentIndex = allPosts.findIndex((p) => p.complaintSeq === post.complaintSeq);
//   const prevPost = allPosts[currentIndex - 1] || null;
//   const nextPost = allPosts[currentIndex + 1] || null;

//   return (
//     <div>
//       <h1>{post.title}</h1>
//       <p>작성자: {post.memberName} ({post.departmentName})</p>
//       <p>업데이트일: {post.updatedAt}</p>
//       <p>{post.content}</p>
//       <p>첨부파일: {post.filePath}</p>
//       <button onClick={handleEdit}>수정하기</button> {/* 수정하기 버튼 */}
//       <div style={{ marginTop: '20px' }}>
//         {/* 이전 글 */}
//         {prevPost ? (
//           <Link to={`/board/${prevPost.complaintSeq}`} style={{ marginRight: '10px' }}>
//             이전 글: {prevPost.title}
//           </Link>
//         ) : (
//           <span style={{ marginRight: '10px', color: '#ccc' }}>이전 글 없음</span>
//         )}

//         {/* 다음 글 */}
//         {nextPost ? (
//           <Link to={`/board/${nextPost.complaintSeq}`} style={{ marginRight: '10px' }}>
//             다음 글: {nextPost.title}
//           </Link>
//         ) : (
//           <span style={{ marginRight: '10px', color: '#ccc' }}>다음 글 없음</span>
//         )}

//         {/* 목록 돌아가기 버튼 */}
//         <button onClick={() => navigate('/board')}>목록</button>
//       </div>
//     </div>
//   );
// };

// export default Detail;

// import React, { useState, useEffect } from 'react';
// import { useParams, useNavigate, Link } from 'react-router-dom';
// import { mockPosts } from '../../mock/posts';
// //import axios from 'axios';

// interface Post {
//   id: number;
//   title: string;
//   body: string;
//   author: string;
//   createdAt: string;
// }

// const Detail: React.FC = () => {
    
//   const { id } = useParams<{ id: string }>();
//   const navigate = useNavigate();
//   const [post, setPost] = useState<Post | null>(null);

// //   //백엔드 호출
// //   useEffect(() => {
// //     axios.get(`/api/posts/${id}`).then((response) => {
// //       setPost(response.data);
// //     });
// //   }, [id]);
//   useEffect(() => {
//     // Mock 데이터에서 ID와 매칭되는 게시글 찾기
//     const foundPost = mockPosts.find((p) => p.id === Number(id));
//     setPost(foundPost || null);
//   }, [id]);

//   if (!post) return <div>Loading...</div>;

//   // 수정하기 버튼 클릭 핸들러
//   const handleEdit = () => {
//     navigate(`/board/edit/${id}`); // 수정 페이지로 이동
//   };
//   // 이전 글과 다음 글 찾기
//   const currentIndex = mockPosts.findIndex((p) => p.id === Number(id));
//   const prevPost = mockPosts[currentIndex - 1] || null;
//   const nextPost = mockPosts[currentIndex + 1] || null;


//   return (
//     <div>
//       <h1>{post.title}</h1>
//       <p>작성자: {post.author}</p>
//       <p>작성일: {post.createdAt}</p>
//       <p>{post.body}</p>
//       <button onClick={handleEdit}>수정하기</button> {/* 수정하기 버튼 */}
//       <div style={{ marginTop: '20px' }}>
//         {/* 이전 글 */}
//         {prevPost ? (
//           <Link to={`/board/${prevPost.id}`} style={{ marginRight: '10px' }}>
//             이전 글: {prevPost.title}
//           </Link>
//         ) : (
//           <span style={{ marginRight: '10px', color: '#ccc' }}>이전 글 없음</span>
//         )}

//         {/* 다음 글 */}
//         {nextPost ? (
//           <Link to={`/board/${nextPost.id}`} style={{ marginRight: '10px' }}>
//             다음 글: {nextPost.title}
//           </Link>
//         ) : (
//           <span style={{ marginRight: '10px', color: '#ccc' }}>다음 글 없음</span>
//         )}

//         {/* 목록 돌아가기 버튼 */}
//         <button onClick={() => navigate('/board')}>목록</button>
//       </div>
//     </div>
//   );
// };

// export default Detail;