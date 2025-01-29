import React, { useState, useEffect } from 'react';
import { useParams, useNavigate} from 'react-router-dom';
import jwtAxios from '../../util/jwtUtils';  // jwtAxios를 import 해야 함
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

  // 현재 게시글 조회
  useEffect(() => {
    const fetchPost = async () => {
      try {
        const response = await jwtAxios.get(`/complaints/${id}`);
        setPost(response.data);
      } catch (error) {
        console.error("게시글을 불러오는 중 오류가 발생했습니다.", error);
      }
    };

    if (id) {
      fetchPost();
    }
  }, [id]);

  

  return (
    <div style={{ padding: '20px' }}>
      {post && (
        <>
          <h1 style={{ fontSize: '24px', marginBottom: '16px' }}>{post.title}</h1>
          <p>작성자: {post.memberName}</p>
          <p>부서명: {post.departmentName}</p>
          <p>업데이트일: {new Date(post.updatedAt).toLocaleDateString()}</p>
          <p>{post.content}</p>
          {post.filePath && <p>첨부파일: {post.filePath}</p>}
          
          <div style={{ marginTop: '20px' }}>
            <button 
            onClick={() => navigate(`/board/edit/${post.complaintSeq}`)}
            style={{
              padding: '8px 16px',
              backgroundColor: '#4CAF50',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer'
            }}
          >
            수정
          </button>
            <button 
              onClick={() => navigate('/board')}
              style={{
                padding: '8px 16px',
                backgroundColor: '#f0f0f0',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer'
              }}
            >
              목록
            </button>
          </div>
        </>
      )}
    </div>
  );
};
export default Detail


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