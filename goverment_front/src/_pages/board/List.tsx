 import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Pagination from "./Pagination";
import SearchBar from "./Searchbar";
import jwtAxios from "../../util/jwtUtils";
//import axios from "axios";





interface Post {
  complaintSeq: number;
  title: string | null;
  memberName: string;
  updatedAt: string;
}

const List: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]);
  const [filteredPosts, setFilteredPosts] = useState<Post[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const postsPerPage = 10;

  // 백엔드에서 데이터 가져오기
  const fetchPosts = async (page: number) => {
    try {
      const departmentSeq = 1; // 필요한 부서 ID 설정
      const response = await jwtAxios.get(`/complaints`, {
        params: {
          departmentSeq,
          page: page - 1, // 백엔드 페이지는 0부터 시작
          size: postsPerPage,
        },
      });
      const { content, totalElements, totalPages } = response.data;
      setPosts(content);
      setFilteredPosts(content);
      setTotalElements(totalElements);
      setTotalPages(totalPages);
    } catch (error) {
      console.error("데이터를 가져오는 중 오류 발생:", error);
    }
  };
  // const fetchPosts = async (page: number) => {
  //   try {
  //     const departmentSeq = 1;
  //     console.log('요청 시작:', {
  //       url: '/complaints',
  //       params: { departmentSeq, page: page - 1, size: postsPerPage }
  //     });
  
  //     const response = await axios.get(`https://8c21-122-37-19-2.ngrok-free.app/complaints`, {
  //       params: {
  //         departmentSeq,
  //         page: page - 1,
  //         size: postsPerPage,
  //       },
  //       headers: {
  //         'Accept': 'application/json',
  //         'ngrok-skip-browser-warning': 'true'

  //       }
  //     });
      
  //     // 디버깅용
  //     console.log('응답 데이터:', response.data);
  
  //     console.log('응답:', response);
  //     const { content, totalElements, totalPages } = response.data;
  //     setPosts(content);
  //     setFilteredPosts(content);
  //     setTotalElements(totalElements);
  //     setTotalPages(totalPages);
  //   } catch (error: any) {
  //     console.log('요청 설정:', error.config);
  //     console.log('에러 상세:', error);
  //   }
  // };

  // 페이지 변경 시 데이터 로드
  useEffect(() => {
    fetchPosts(currentPage);
  }, [currentPage]);

  // 검색 핸들러
  const handleSearch = (searchTerm: string, filter: string) => {
    const filtered = posts.filter((post) => {
      if (filter === "title" && post.title) {
        return post.title.toLowerCase().includes(searchTerm.toLowerCase());
      } else if (filter === "memberName") {
        return post.memberName.toLowerCase().includes(searchTerm.toLowerCase());
      }
      return false;
    });
    setFilteredPosts(filtered);
    setCurrentPage(1); // 검색 시 첫 페이지로 이동
  };

  return (
    <div>
      <h1>민원 게시판</h1>
      <SearchBar onSearch={handleSearch} />
      <Link to="/board/create">게시글 작성</Link>
      <table>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
          </tr>
        </thead>
        <tbody>
          {filteredPosts.map((post) => (
            <tr key={post.complaintSeq}>
              <td>{post.complaintSeq}</td>
              <td>
                <Link to={`/board/${post.complaintSeq}`}>
                  {post.title || "(제목 없음)"}
                </Link>
              </td>
              <td>{post.memberName}</td>
              <td>{new Date(post.updatedAt).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <Pagination
        totalItems={totalElements}
        itemsPerPage={postsPerPage}
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
      />
    </div>
  );
};

export default List;

// import React, { useState, useEffect } from "react";
// import { Link } from "react-router-dom";
// //import axios from "axios";
// import Pagination from "./Pagination";
// import SearchBar from "./Searchbar";
// import { mockPosts } from '../../mock/posts';

// interface Post {
//   id: number;
//   title: string;
//   author: string;
//   createdAt: string;
// }

// /**게시판 목록 */
// const List: React.FC = () => {
//   //   const [posts, setPosts] = useState<Post[]>([]);
//   const [posts, setPosts] = useState(mockPosts); // 전체 데이터
//   const [filteredPosts, setFilteredPosts] = useState<Post[]>(mockPosts);
//   const [currentPage, setCurrentPage] = useState(1);
//   const postsPerPage = 10;

// //   //백엔드 호출
// //   useEffect(() => {
// //     axios.get("/api/posts").then((response) => {
// //       setPosts(response.data);
// //       setFilteredPosts(response.data);
// //     });
// //   }, []);

//   // 현재 페이지에 표시할 게시글
//   const indexOfLastPost = currentPage * postsPerPage;
//   const indexOfFirstPost = indexOfLastPost - postsPerPage;
//   const currentPosts = filteredPosts.slice(indexOfFirstPost, indexOfLastPost);

//   // 검색 핸들러
//   const handleSearch = (searchTerm: string, filter: string) => {
//     const filtered = posts.filter((post) => {
//       if (filter === "title") {
//         return post.title.toLowerCase().includes(searchTerm.toLowerCase());
//       } else if (filter === "body") {
//         return post.body.toLowerCase().includes(searchTerm.toLowerCase());
//       }
//       return false;
//     });
//     setFilteredPosts(filtered);
//     setCurrentPage(1); // 검색 시 첫 페이지로 이동
//   };

//   return (
//     <div>
//       <h1>민원 게시판</h1>
//       <SearchBar onSearch={handleSearch} />
//       <Link to="/board/create">게시글 작성</Link>
//       <table>
//         <thead>
//           <tr>
//             <th>번호</th>
//             <th>제목</th>
//             <th>작성자</th>
//             <th>작성일</th>
//           </tr>
//         </thead>
//         <tbody>
//           {currentPosts.map((post) => (
//             <tr key={post.id}>
//               <td>{post.id}</td>
//               <td>
//                 <Link to={`/board/${post.id}`}>{post.title}</Link>
//               </td>
//               <td>{post.author}</td>
//               <td>{post.createdAt}</td>
//             </tr>
//           ))}
//         </tbody>
//       </table>
//       <Pagination
//         totalItems={filteredPosts.length}
//         itemsPerPage={postsPerPage}
//         currentPage={currentPage}
//         onPageChange={setCurrentPage}
//       />
//     </div>
//   );
// };

// export default List;
