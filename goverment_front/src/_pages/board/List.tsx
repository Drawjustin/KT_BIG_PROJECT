import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import ReactPaginate from "react-paginate";
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
  const [searchType, setSearchType] = useState("departmentName"); // 검색 타입 (부서 or 제목)
  const [searchQuery, setSearchQuery] = useState(""); // 검색어
  const postsPerPage = 10;

  // 게시글 불러오기
  const fetchPosts = async (page: number, searchType?: string, query?: string) => {
    try {
      const params: Record<string, any> = {
        page,
        size: postsPerPage,
      };

      if (query) {
        params[searchType!] = query; // 선택된 검색 조건 적용
      }

      const response = await jwtAxios.get<PageResponse>("/complaints", { params });

      console.log("응답 데이터:", response.data);
      setPageData(response.data);
    } catch (error) {
      console.error("데이터를 가져오는 중 오류 발생:", error);
    }
  };

  // 페이지 변경 시 데이터 로드
  useEffect(() => {
    fetchPosts(currentPage, searchType, searchQuery);
  }, [currentPage]);

  // 검색 버튼 클릭 시 호출
  const handleSearch = () => {
    setCurrentPage(0); // 검색 시 첫 페이지로 이동
    fetchPosts(0, searchType, searchQuery);
  };

  // 페이지네이션 핸들러
  const handlePageChange = ({ selected }: { selected: number }) => {
    setCurrentPage(selected);
  };

  return (
    <div>
      <div>
        <h1>민원 접수</h1>
        <Link to="/board/create">접수하기</Link>
      </div>
      <div style={{ marginBottom: "10px" }}>
        <form onSubmit={(e) => {
        e.preventDefault();
        handleSearch();
        }}>
        <select value={searchType} onChange={(e) => setSearchType(e.target.value)}>
          <option value="departmentName">부서</option>
          <option value="title">제목</option>
        </select>
        <input
          type="text"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          placeholder="검색어 입력"
        />
        <button onClick={handleSearch}>검색</button>
        </form>
      </div>
      

      {/* 게시글 목록 */}
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
                <td>{new Date(post.updatedAt).toLocaleDateString("ko-KR")}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={5}>게시글이 없습니다.</td>
            </tr>
          )}
        </tbody>
      </table>

      {/* 페이지네이션 */}
      <ReactPaginate
        previousLabel="<"
        nextLabel=">"
        breakLabel="..."
        pageCount={pageData?.page?.totalPages || 0}
        pageRangeDisplayed={5}
        marginPagesDisplayed={2}
        onPageChange={handlePageChange}
        forcePage={pageData?.page?.number || 0}
      />
    </div>
  );
};

export default List;