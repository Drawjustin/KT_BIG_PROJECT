import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import jwtAxios from '../../util/jwtUtils';

const FileView = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchFileDetail = async () => {
      try {
        setLoading(true);
        const response = await jwtAxios.get(`/files/${id}`);
        setPost(response.data);
      } catch (err) {
        console.error("파일 상세 정보 불러오기 실패:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchFileDetail();
  }, [id]);

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>오류 발생: {error}</div>;
  if (!post) return <div>파일을 찾을 수 없습니다.</div>;


  return (
    <div>
      <h1>{post.fileTitle || '제목 없음'}</h1>
      <p>작성자 : {post.adminId}</p>
      <p>
        작성일 : {new Date(post.updatedAt).toLocaleDateString('ko-KR', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit'
        })}
      </p>
      <p>첨부파일 : {post.fileContent || '내용 없음'}</p>
      {/* <button onClick={handleEdit}>수정하기</button> */}
      <div style={{ marginTop: '20px' }}>
        <button onClick={() => navigate('/dataroom')}>목록</button>
      </div>
    </div>
  );
};

export default FileView;