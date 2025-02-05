import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { dataroomApi } from '../../api';
import styles from './FileView.module.css';
// import axios from 'axios'; // for axios

const FileView = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  // const url = import.meta.env.VITE_API_BASE_URL; // for axios
  const handleFileView = async (filePath) => {
    try {
      // API를 통해 파일 다운로드 or 보기 처리
      const response = await dataroomApi.getFile(filePath);
      // 파일 타입에 따른 처리
      if (post.fileType.includes('image')) {
        // 이미지 보기
        window.open(URL.createObjectURL(response));
      } else {
        // 파일 다운로드
        const url = window.URL.createObjectURL(new Blob([response]));
        const link = document.createElement('a');
        link.href = url;
        link.download = filePath.split('/').pop(); // 파일명
        document.body.appendChild(link);
        link.click();
        link.remove();
      }
    } catch (error) {
      console.error('파일 처리 중 오류 발생:', error);
    }
  };


  useEffect(() => {
    const fetchFileDetail = async () => {
      try {
        setLoading(true);

        const response = await dataroomApi.getDetail(id);
        console.log("자료실 단건조회",response);
        // const response = await axios.get(`${url}/files`, id); // for axios
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

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.headerContent}>
          <div className={styles.title}>
            {post.fileTitle || '제목 없음'}
          </div>
        </div>
        <div className={styles.authorInfo}>
          {post.adminId}
          <div className={styles.verticalDivider}></div>
          {formatDate(post.updatedAt)}
        </div>
      </div>

      <div className={styles.content}>
        <div className={styles.contentSection}>
          {post.fileContent || '내용 없음'}
        </div>
      </div>

      <div className={styles.attachmentSection}>
        <div className={styles.attachmentTitle}>첨부파일</div>
        {post.filePath ? (
          <div className={styles.attachmentItem}>
            <span className={styles.attachmentName}>
              {post.filePath.split('/').pop()}
            </span>
            <div 
              className={styles.viewButton} 
              onClick={() => handleFileView(post.filePath)}
            >
              <span className={styles.viewButtonText}>
                바로보기
              </span>
            </div>
          </div>
        ) : (
          <div className={styles.attachmentItem}>
            <span className={styles.attachmentName}>
              첨부파일이 없습니다.
            </span>
          </div>
        )}
      </div>

      <div 
        className={styles.listButton} 
        onClick={() => navigate('/dataroom')}
      >
        목록
      </div>
    </div>
  );
};

export default FileView;
// import { useState, useEffect } from 'react';
// import { useParams, useNavigate } from 'react-router-dom';
// import { dataroomApi } from '../../api';
// import styles from './FileView.module.css'

// const FileView = () => {
//   const { id } = useParams();
//   const navigate = useNavigate();
//   const [post, setPost] = useState(null);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);

//   useEffect(() => {
//     const fetchFileDetail = async () => {
//       try {
//         setLoading(true);
//         const response = await dataroomApi.getDetail(id);
//         setPost(response.data);
//       } catch (err) {
//         console.error("파일 상세 정보 불러오기 실패:", err);
//         setError(err.message);
//       } finally {
//         setLoading(false);
//       }
//     };

//     fetchFileDetail();
//   }, [id]);

//   if (loading) return <div>로딩 중...</div>;
//   if (error) return <div>오류 발생: {error}</div>;
//   if (!post) return <div>파일을 찾을 수 없습니다.</div>;


//   return (
//       <div className={styles.container}>
//         <h1 className={styles.title}>{post.fileTitle || '제목 없음'}</h1>
//         <div className={styles.infoBox}>
//           <p>작성자 : {post.adminId}</p>
//           <p>
//             작성일 : {new Date(post.updatedAt).toLocaleDateString('ko-KR', {
//               year: 'numeric',
//               month: '2-digit',
//               day: '2-digit'
//             })}
//           </p>
//         </div>
//         <div className={styles.contentBox}>
//           <h2>첨부파일</h2>
//           <p>{post.fileContent || '내용 없음'}</p>
//         </div>
//         <div className={styles.buttonContainer}>
//           <button onClick={() => navigate('/dataroom')} className={styles.button}>목록</button>
//         </div>
//       </div>
    // <div>
    //   <h1>{post.fileTitle || '제목 없음'}</h1>
    //   <p>작성자 : {post.adminId}</p>
    //   <p>
    //     작성일 : {new Date(post.updatedAt).toLocaleDateString('ko-KR', {
    //       year: 'numeric',
    //       month: '2-digit',
    //       day: '2-digit'
    //     })}
    //   </p>
    //   <p>첨부파일 : {post.fileContent || '내용 없음'}</p>
    //   {/* <button onClick={handleEdit}>수정하기</button> */}
    //   <div style={{ marginTop: '20px' }}>
    //     <button onClick={() => navigate('/dataroom')}>목록</button>
    //   </div>
//     // </div>
//   );
// };

// export default FileView;