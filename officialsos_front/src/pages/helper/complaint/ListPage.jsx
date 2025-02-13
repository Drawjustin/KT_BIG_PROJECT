import { useState, useEffect } from "react";
import styles from "./ListPage.module.css";
import { Link } from "react-router-dom";
import { complaintApi } from "../../../api";

const ListPage = () => {
  const [pageData, setPageData] = useState([]); // 공백 제거
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const postsPerPage = 10;
  const [searchType, setSearchType] = useState("title"); // 검색 유형
  const [searchKeyword, setSearchKeyword] = useState(""); // 검색어

  const fetchListData = async (page) => {
    try {
      setLoading(true);
      const params = {
        page: page - 1,
        size: postsPerPage,
      };

      //검색어가 있는 경우 params에 추가
      if (searchKeyword) {
        params[searchType] = searchKeyword;
      }
      // const response = await jwtAxios.get("/complaint-comments", { params }); // api 호출 엔드포인트
      const response = await complaintApi.getList(params);// api 호출 엔드포인트
      console.log("목록조회 response:", response.data);


      setPageData(response.data);
      setTotalPages(response.data.page.totalPages);
    } catch (error) {
      console.error("데이터 가져오는 중 오류 발생:", error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };
  const handleSearch = (e) => {
    e.preventDefault();
    setCurrentPage(1); // 검색 시 첫 페이지로 이동
    fetchListData(1); 
  };
  useEffect(() => {
    fetchListData(currentPage);
  }, [currentPage]);

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

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
  
  return (
    <>
    <div className={styles.listPageContainer}>
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
            <th>답변여부</th>
            <th>날짜</th>
          </tr>
        </thead>
        <tbody>
          {pageData?.content?.map((item, index) => (
            <tr key={`complaint-${item.complaintSeq}`}>
              <td>{(currentPage - 1) * postsPerPage + index + 1}</td>
              <td>
                <Link to={`write/${item.complaintSeq}`}>
                  {item.bad ? '⚠️':'' }
                  {item.title && item.title.trim() !== ""
                    ? item.title
                    : "제목 없음"}
                </Link>
              </td>
              <td>{item.departmentName}</td>
              <td style={{ color: item.answered ? 'green' : 'red' }}>
                {item.answered ? '답변완료' : '답변대기'}
              </td>
              <td>
                {item.updatedAt
                  ? new Date(item.updatedAt).toLocaleDateString("ko-KR", {
                      year: "numeric",
                      month: "2-digit",
                      day: "2-digit",
                    })
                  : "-"}
              </td>
            </tr>
          ))}
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
    </>
  );
};

export default ListPage;