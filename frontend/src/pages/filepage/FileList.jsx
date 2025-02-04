import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import styles from "./FileList.module.css"; // CSS 모듈 import 추가
import {dataroomApi} from "../../api"
/**
 * @typedef {Object} Post
 * @property {number} fileSeq
 * @property {string} adminName
 * @property {string[]} departmentName
 * @property {string} title
 * @property {string} content
 * @property {string} filePath
 * @property {string} fileType
 * @property {string} updatedAt
 */

/**
 * @typedef {Object} PageResponse
 * @property {Post[]} content
 * @property {Object} page
 * @property {number} page.size
 * @property {number} page.number
 * @property {number} page.totalElements
 * @property {number} page.totalPages
 */

const FileList = () => {
  /** @type {[PageResponse, Function]} */
  const [pageData, setPageData] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const postsPerPage = 10;
  const [searchType, setSearchType] = useState("title"); // 검색 유형
  const [searchKeyword, setSearchKeyword] = useState(""); // 검색어

  const fetchPosts = async (page) => {
    try {
      setLoading(true);
      const params = {
        page: page - 1,
        size: postsPerPage,
      };
  
      // 검색어가 있는 경우 params에 추가
      if (searchKeyword) {
        params[searchType] = searchKeyword;
      }
  
      const response = await dataroomApi.getList(params);
      console.log("API 응답 데이터:", response.data);

      setPageData(response.data);
      setTotalPages(response.data.page.totalPages);
    } catch (error) {
      console.error("데이터를 가져오는 중 오류 발생:", error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };
  const handleSearch = (e) => {
    e.preventDefault();
    setCurrentPage(1); // 검색 시 첫 페이지로 이동
    fetchPosts(1);
  };
  useEffect(() => {
    fetchPosts(currentPage);
  }, [currentPage]);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>오류 발생: {error}</p>;

  return (
    <div className={styles.listPageContainer}>
      <h1>자료실 목록</h1>
      <div className={styles.searchContainer}>
        <form onSubmit={handleSearch} className={styles.searchForm}>
          <select 
            value={searchType}
            onChange={(e) => setSearchType(e.target.value)}
            className={styles.searchSelect}
          >
            <option value="title">제목</option>
            <option value="departmentName">부서</option>
          </select>
          <input
            type="text"
            value={searchKeyword}
            onChange={(e) => setSearchKeyword(e.target.value)}
            placeholder="검색어를 입력하세요"
            className={styles.searchInput}
          />
          <button type="submit" className={styles.searchButton}>
            검색
          </button>
        </form>
      </div>
      <table className={styles.listTable}>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>부서</th>
            <th>작성자</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          {pageData?.content?.length ? (
            pageData.content.map((post, index) => (
              <tr key={post.fileSeq}>
                <td>{(currentPage - 1) * postsPerPage + index + 1}</td>
                <td>
                  <Link to={`/dataroom/view/${post.fileSeq}`}>
                    {post.title}
                  </Link>
                </td>
                <td>{post.departmentName?.[0] || '부서 미지정'}</td>
                <td>{post.adminName}</td>
                <td>
                  {new Date(post.updatedAt).toLocaleDateString("ko-KR", {
                    year: "numeric",
                    month: "2-digit",
                    day: "2-digit",
                  })}
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={5}>게시글이 없습니다.</td>
            </tr>
          )}
        </tbody>
      </table>

      <div className={styles.pagination}>
        {Array.from({ length: totalPages }, (_, index) => (
          <button
            key={`page-${index}`}
            onClick={() => handlePageChange(index + 1)}
            className={currentPage === index + 1 ? styles.active : ""}
          >
            {index + 1}
          </button>
        ))}
      </div>
    </div>
  );
};

export default FileList;