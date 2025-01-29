import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import ReactPaginate from 'react-paginate';
import jwtAxios from "../../util/jwtUtils";

// 게시글 타입 정의
interface Post {
  complaintSeq: number;
  title: string | null;
  memberName: string;
  departmentName: string;
  content: string | null;
  filePath: string | null;
  updatedAt: string;
}

// 페이지 정보 타입 정의
interface PageResponse {
  content: Post[];
  page: {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
  };
}

const List: React.FC = () => {
  const [pageData, setPageData] = useState<PageResponse | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const postsPerPage = 10;

  const fetchPosts = async (page: number) => {
    try {
      const response = await jwtAxios.get<PageResponse>('/complaints', {
        params: {
          page : page,
          size: postsPerPage,
        },
      });
      console.log('응답 데이터:', response.data); // 데이터 확인용
      setPageData(response.data);
    } catch (error) {
      console.error("데이터를 가져오는 중 오류 발생:", error);
    }
  };

  useEffect(() => {
    fetchPosts(currentPage);
  }, [currentPage]);

  const handlePageChange = ({ selected }: { selected: number }) => {
    setCurrentPage(selected);
  };

  return (
    <div>
      <div>
        <h1>민원 접수</h1>
        <Link to="/board/create">접수하기</Link>
      </div>

      <table>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>부서</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          {pageData?.content?.length ? (
            pageData.content.map((post) => (
              <tr key={post.complaintSeq}>
                <td>{post.complaintSeq}</td>
                <td>
                  <Link to={`/board/${post.complaintSeq}`}>
                    {post.title || "(제목 없음)"}
                  </Link>
                </td>
                <td>{post.memberName}</td>
                <td>{post.departmentName}</td>
                <td>{new Date(post.updatedAt).toLocaleDateString('ko-KR')}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={5}>게시글이 없습니다.</td>
            </tr>
          )}
        </tbody>
      </table>

      <ReactPaginate
        previousLabel="<"
        nextLabel=">"
        breakLabel="..."
        pageCount={pageData?.page?.totalPages || 0} // 전체 페이지 수
        pageRangeDisplayed={5}
        marginPagesDisplayed={2}
        onPageChange={handlePageChange}
        forcePage={pageData?.page?.number || 0} // 현재 페이지 번호
      />
    </div>
  );
};

export default List;