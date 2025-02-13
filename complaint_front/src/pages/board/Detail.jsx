import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import styled from "styled-components";
import { complaintApi } from "../../api";

/* ✅ 스타일 수정 */
const DetailContainer = styled.div`
  max-width: 800px;
  margin: 50px auto;
  padding: 40px 20px;
  background-color: #ffffff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
`;

const Title = styled.h1`
  font-size: 28px;
  color: #1E1E38; /* 네이비 */
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 3px solid #A3E3DD; /* 연한 민트 */
`;

const MetaInfo = styled.div`
  display: flex;
  justify-content: space-between;
  color: #555;
  margin-bottom: 20px;
  font-size: 14px;
`;

const MetaItem = styled.div`
  font-weight: 500;
  color: #1E1E38; /* 네이비 */
`;

const Content = styled.div`
  font-size: 16px;
  line-height: 1.6;
  color: #333;
  margin-top:50px;
  margin-bottom: 30px;
  padding: 15px;
  min-height: 100px; 
  background-color: #F7F9FC; /* 소프트 그레이 */
  border-radius: 8px;
`;

const Attachment = styled.div`
  width: 100%; /* ✅ 부모 요소에 맞게 조정 */
  max-width: 750px; /* ✅ 부모보다 커지지 않도록 설정 */
  background-color: #A3E3DD; /* 연한 민트 */
  padding: 20px; /* ✅ 전체 여백 증가 */
  border-radius: 8px;
  margin: 20px auto; /* ✅ 중앙 정렬 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  box-sizing: border-box;
`;

const AttachmentTitle = styled.h3`
  font-size: 16px;
  color: #1E1E38; /* 네이비 */
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-bottom: 12px; /* ✅ 제목과 링크 사이 간격 조정 */
`;

const AttachmentLink = styled.a`
  color: #1E897D; /* 진한 청록 */
  font-weight: bold;
  text-decoration: none;
  font-size: 16px;
  transition: color 0.3s ease-in-out;

  &:hover {
    text-decoration: underline;
    color: #1E1E38; /* 네이비 */
  }
`;

const ButtonGroup = styled.div`
  display: flex;
  justify-content: flex-end;
  gap: 12px;
`;

/* ✅ 버튼 스타일 */
const Button = styled.button`
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  transition: background-color 0.3s;
  
  /* ✅ 수정 버튼 (청록) */
  &.edit {
    background-color: #2AB3A6; /* 청록 */
    color: white;

    &:hover {
      background-color: #1E897D; /* 진한 청록 */
    }
  }

  /* ✅ 삭제 버튼 (빨강) */
  &.delete {
    background-color: #D32F2F; /* 빨간색 */
    color: white;

    &:hover {
      background-color: #B71C1C; /* 더 진한 빨강 */
    }
  }

  /* ✅ 목록 버튼 (네이비) */
  &.list {
    background-color: #1E1E38; /* 네이비 */
    color: white;

    &:hover {
      background-color: #151527; /* 더 딥한 네이비 */
    }
  }
`;
/* ✅ "로딩 중..." 텍스트 스타일 */
const LoadingText = styled.h3`
  color: #1E1E38;
  font-size: 1.5rem; /* ✅ 더 크고 보기 쉽게 */
  font-weight: bold;
  text-align: center;
  margin: 0;
`;

/* ✅ 게시글 상세보기 컴포넌트 */
const Detail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [post, setPost] = useState(null);

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const response = await complaintApi.getDetail(id);
        setPost(response.data);
      } catch (error) {
        console.error("게시글을 불러오는 중 오류가 발생했습니다.", error);
      }
    };

    if (id) {
      fetchPost();
    }
  }, [id]);

  if (!post) return <DetailContainer><LoadingText>로딩 중...</LoadingText></DetailContainer>;

  return (
    <DetailContainer>
      <Title>{post.title}</Title>
      <MetaInfo>
        <MetaItem>작성자: {post.memberName}</MetaItem>
        <MetaItem>부서: {post.departmentName}</MetaItem>
        <MetaItem>작성일: {new Date(post.updatedAt).toLocaleDateString()}</MetaItem>
      </MetaInfo>
      <Content>{post.content}</Content>
      {post.filePath && (
        <Attachment>
          <AttachmentTitle>📎 첨부파일</AttachmentTitle>
          <AttachmentLink href={post.filePath} target="_blank" rel="noopener noreferrer">
            첨부파일 보기
          </AttachmentLink>
        </Attachment>
      )}
      <ButtonGroup>
  
  <Button className="edit" onClick={() => navigate(`/complaints/edit/${post.complaintSeq}`)}>
    수정
  </Button>
  <Button className="list" onClick={() => navigate("/complaints")}>
    목록
  </Button>
</ButtonGroup>
    </DetailContainer>
  );
};

export default Detail;