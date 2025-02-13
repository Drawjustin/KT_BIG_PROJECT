import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import ReactPaginate from "react-paginate";
import styles from "./List.module.css";
import { complaintApi } from "../../api";

const List = () => {
  const [pageData, setPageData] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [searchType, setSearchType] = useState("departmentName");
  const [searchQuery, setSearchQuery] = useState("");
  const postsPerPage = 10;

  const fetchPosts = async (page, searchType, query) => {
    try {
      const params = {
        page,
        size: postsPerPage,
      };

      if (query) {
        params[searchType] = query;
      }

      const response = await complaintApi.getList(params);

      console.log("응답 데이터:", response.data);
      setPageData(response.data);
    } catch (error) {
      console.error("데이터를 가져오는 중 오류 발생:", error);
    }
  };

  useEffect(() => {
    fetchPosts(currentPage, searchType, searchQuery);
  }, [currentPage]);

  const handleSearch = () => {
    setCurrentPage(0);
    fetchPosts(0, searchType, searchQuery);
  };

  const handlePageChange = ({ selected }) => {
    setCurrentPage(selected);
  };

  return (
    <div className={styles.listPageContainer}>
      <div className={styles.headerContainer}>
        <h1 className={styles.headerTitle}>민원 접수</h1>
        <Link 
          to="/complaints/create" 
          className={styles.createButton}
        >
          접수하기
        </Link>
      </div>

      <div className={styles.searchContainer}>
        <form 
          className={styles.searchForm} 
          onSubmit={(e) => {
            e.preventDefault();
            handleSearch();
          }}
        >
          <select 
            className={styles.searchSelect} 
            value={searchType} 
            onChange={(e) => setSearchType(e.target.value)}
          >
            <option value="departmentName">부서</option>
            <option value="title">제목</option>
          </select>
          <input
            className={styles.searchInput}
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="검색어 입력"
          />
          <button 
            className={styles.searchButton} 
            onClick={handleSearch}
          >
            검색
          </button>
        </form>
      </div>

      <table className={styles.table}>
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
                  <Link to={`/complaints/${post.complaintSeq}`}>
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

      <ReactPaginate
        className={styles.pagination}
        previousLabel="<"
        nextLabel=">"
        breakLabel="..."
        pageCount={pageData?.page?.totalPages || 0}
        pageRangeDisplayed={5}
        marginPagesDisplayed={2}
        onPageChange={handlePageChange}
        forcePage={pageData?.page?.number || 0}
        activeClassName={styles.active}
        disabledClassName={styles.disabled}
      />
    </div>
  );
};

export default List;