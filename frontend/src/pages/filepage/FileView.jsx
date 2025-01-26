import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { mockPosts } from '../mocks/posts';

const FileView = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);

  useEffect(() => {
    const foundPost = mockPosts.find((p) => p.id === Number(id));
    setPost(foundPost || null);
  }, [id]);

  if (!post) return <div>Loading...</div>;

  const handleEdit = () => {
    navigate(`/board/edit/${id}`);
  };

  const currentIndex = mockPosts.findIndex((p) => p.id === Number(id));
  const prevPost = mockPosts[currentIndex - 1] || null;
  const nextPost = mockPosts[currentIndex + 1] || null;

  return (
    <div>
      <h1>{post.title}</h1>
      <p>작성자: {post.author}</p>
      <p>작성일: {post.createdAt}</p>
      <p>{post.body}</p>
      <button onClick={handleEdit}>수정하기</button>
      <div style={{ marginTop: '20px' }}>
        {prevPost ? (
          <Link to={`/dataroom/view/${prevPost.id}`} style={{ marginRight: '10px' }}>
            이전 글: {prevPost.title}
          </Link>
        ) : (
          <span style={{ marginRight: '10px', color: '#ccc' }}>이전 글 없음</span>
        )}
        {nextPost ? (
          <Link to={`/dataroom/view/${nextPost.id}`} style={{ marginRight: '10px' }}>
            다음 글: {nextPost.title}
          </Link>
        ) : (
          <span style={{ marginRight: '10px', color: '#ccc' }}>다음 글 없음</span>
        )}
        <button onClick={() => navigate('/dataroom')}>목록</button>
      </div>
    </div>
  );
};

export default FileView;