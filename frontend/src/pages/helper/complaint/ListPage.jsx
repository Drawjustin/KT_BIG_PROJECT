import { useState, useEffect } from "react";
import styles from './ListPage.module.css'
import { Link } from "react-router-dom";
//import axios from "axios";

const ListPage = () => {
  const [listData, setListData] = useState([]); // 현재 페이지 데이터
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지 번호
  const [totalPages, setTotalPages] = useState(1); // 총 페이지 수
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태
  const itemsPerPage = 10; // 한 페이지당 항목 수

//   // 백엔드 API 호출
//   const fetchListData = async (page) => {  
//     try {
//       setLoading(true);
//       const response = await axios.get("http://localhost:5000/api/post-list", {
//         params: {
//           page: page,
//           limit: itemsPerPage, // 한 페이지에 보여줄 데이터 개수
//         },
//       });
//       setListData(response.data.items); // 현재 페이지 데이터 저장
//       setTotalPages(response.data.totalPages); // 총 페이지 수 업데이트
//     } catch (err) {
//       console.error("데이터를 가져오는 중 오류 발생:", err);
//       setError(err.message);
//     } finally {
//       setLoading(false);
//     }
//   };

  // 목데이터 (위 JSON 데이터 사용)
  const mockData = {
    items: [
      { id: 1, title: "민원 제목 1", department: "부서 A", date: "2025-01-01T00:00:00.000Z" },
      { id: 2, title: "민원 제목 2", department: "부서 B", date: "2025-01-02T00:00:00.000Z" },
      { id: 3, title: "민원 제목 3", department: "부서 C", date: "2025-01-03T00:00:00.000Z" },
      { id: 4, title: "민원 제목 4", department: "부서 D", date: "2025-01-04T00:00:00.000Z" },
      { id: 5, title: "민원 제목 5", department: "부서 E", date: "2025-01-05T00:00:00.000Z" },
      { id: 6, title: "민원 제목 6", department: "부서 A", date: "2025-01-06T00:00:00.000Z" },
      { id: 7, title: "민원 제목 7", department: "부서 B", date: "2025-01-07T00:00:00.000Z" },
      { id: 8, title: "민원 제목 8", department: "부서 C", date: "2025-01-08T00:00:00.000Z" },
      { id: 9, title: "민원 제목 9", department: "부서 D", date: "2025-01-09T00:00:00.000Z" },
      { id: 10, title: "민원 제목 10", department: "부서 E", date: "2025-01-10T00:00:00.000Z" },
      { id: 11, title: "민원 제목 11", department: "부서 A", date: "2025-01-11T00:00:00.000Z" },
      { id: 12, title: "민원 제목 12", department: "부서 B", date: "2025-01-12T00:00:00.000Z" },
      { id: 13, title: "민원 제목 13", department: "부서 C", date: "2025-01-13T00:00:00.000Z" },
      { id: 14, title: "민원 제목 14", department: "부서 D", date: "2025-01-14T00:00:00.000Z" },
      { id: 15, title: "민원 제목 15", department: "부서 E", date: "2025-01-15T00:00:00.000Z" },
      { id: 16, title: "민원 제목 16", department: "부서 A", date: "2025-01-16T00:00:00.000Z" },
      { id: 17, title: "민원 제목 17", department: "부서 B", date: "2025-01-17T00:00:00.000Z" },
      { id: 18, title: "민원 제목 18", department: "부서 C", date: "2025-01-18T00:00:00.000Z" },
      { id: 19, title: "민원 제목 19", department: "부서 D", date: "2025-01-19T00:00:00.000Z" },
      { id: 20, title: "민원 제목 20", department: "부서 E", date: "2025-01-20T00:00:00.000Z" }
    ],
    totalPages: 2 // 예시로 2페이지
  };

  // API 호출 대신 mock 데이터 사용
  const fetchListData = async (page) => {
    try {
      setLoading(true);
      // mock API 응답 시뮬레이션
      const response = mockData;

      // 현재 페이지 데이터 저장
      const startIndex = (page - 1) * itemsPerPage;
      const currentPageData = response.items.slice(startIndex, startIndex + itemsPerPage);
      setListData(currentPageData);

      // 총 페이지 수 업데이트
      setTotalPages(response.totalPages);
    } catch (err) {
      console.error("데이터를 가져오는 중 오류 발생:", err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };


  useEffect(() => {
    fetchListData(currentPage); // 페이지 변경 시 데이터 호출
  }, [currentPage]);

  // 페이지 변경 핸들러
  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>오류 발생: {error}</p>;

  return (
    
    <div className={styles.listPageContainer}>
      <h1>민원 목록</h1>
      <table className={styles.listTable}>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>부서</th>
            <th>날짜</th>
          </tr>
        </thead>
        <tbody>
          {listData.map((item, index) => (
            <tr key={item.id}>
              <td>{(currentPage - 1) * itemsPerPage + index + 1}</td>
              <td>
                {/* 제목에 Link 추가 */}
                <Link to={`complaint/view/${item.id}`}>{item.title}</Link>
              </td>
              <td>{item.department}</td>
              <td>{new Date(item.date).toLocaleDateString()}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* 페이지네이션 */}
      <div className={styles.pagination}>
        {Array.from({ length: totalPages }, (_, index) => (
          <button
            key={index}
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

export default ListPage;