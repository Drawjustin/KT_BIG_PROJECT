import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { dataroomApi } from '../../api';
import styles from './FileView.module.css';
import { FileText, Eye, EyeOff } from 'lucide-react';

// import axios from 'axios'; // for axios

const FileView = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isPreviewVisible, setIsPreviewVisible] = useState(false);


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

  if (loading) return (
      <div className={styles.listPageContainer}>
        <p style={{ 
          textAlign: 'center', 
          position: 'absolute', 
          left: '50%', 
          top: '50%', 
          transform: 'translate(-50%, -50%)',
          color:'#2A5C96'
        }}>로딩 중...</p>
      </div>
    );
    if (error) return (
      <div className={styles.listPageContainer}>
        <p style={{ 
          textAlign: 'center', 
          position: 'absolute', 
          left: '50%', 
          top: '50%', 
          transform: 'translate(-50%, -50%)', 
          color:'red'
        }}>오류발생: {error}</p>
      </div>
    );
    if (!post) return (
      <div className={styles.listPageContainer}>
        <p style={{ 
          textAlign: 'center', 
          position: 'absolute', 
          left: '50%', 
          top: '50%', 
          transform: 'translate(-50%, -50%)', 
          color:'red'
        }}>파일을 찾을 수 없습니다.</p>
      </div>
    );
  

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
        <div className={styles.attachmentTitle}>
          <FileText size={20} />
          첨부파일
        </div>
        {post.filePath && (
          <>
            <div className={styles.attachmentItem}>
              <div className={styles.fileIconWrapper}>
                <FileText size={24} />
              </div>
              
              <div className={styles.fileInfo}>
                <span className={styles.fileName}>
                  {post.fileTitle}
                </span>
                <span className={styles.fileDate}>
                  {formatDate(post.updatedAt)}
                </span>
              </div>

              <div className={styles.actionButtons}>
                <button
                  className={styles.previewButton}
                  onClick={() => setIsPreviewVisible(!isPreviewVisible)}
                >
                  {isPreviewVisible ? <EyeOff size={16} /> : <Eye size={16} />}
                  {isPreviewVisible ? '미리보기 닫기' : '미리보기'}
                </button>
                
              </div>
            </div>

            {isPreviewVisible && (
              <div className={styles.previewContainer}>
                <iframe
                  src={post.filePath}
                  style={{ width: '100%', height: '100%', border: 'none' }}
                  title="PDF Preview"
                />
              </div>
            )}
          </>
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