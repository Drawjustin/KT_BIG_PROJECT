import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
//import axios from "axios";
import Pagination from "./Pagination";
import SearchBar from "./Searchbar";
import { mockPosts } from '../../mock/posts';

interface Post {
  id: number;
  title: string;
  author: string;
  createdAt: string;
}

/**게시판 목록 */
const List: React.FC = () => {
  //   const [posts, setPosts] = useState<Post[]>([]);
  const [posts, setPosts] = useState(mockPosts); // 전체 데이터
  const [filteredPosts, setFilteredPosts] = useState<Post[]>(mockPosts);
  const [currentPage, setCurrentPage] = useState(1);
  const postsPerPage = 10;

//   //백엔드 호출
//   useEffect(() => {
//     axios.get("/api/posts").then((response) => {
//       setPosts(response.data);
//       setFilteredPosts(response.data);
//     });
//   }, []);

  // 현재 페이지에 표시할 게시글
  const indexOfLastPost = currentPage * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = filteredPosts.slice(indexOfFirstPost, indexOfLastPost);

  // 검색 핸들러
  const handleSearch = (searchTerm: string) => {
    const filtered = posts.filter((post) =>
      post.title.toLowerCase().includes(searchTerm.toLowerCase())
    );
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
          {currentPosts.map((post) => (
            <tr key={post.id}>
              <td>{post.id}</td>
              <td>
                <Link to={`/board/${post.id}`}>{post.title}</Link>
              </td>
              <td>{post.author}</td>
              <td>{post.createdAt}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <Pagination
        totalItems={filteredPosts.length}
        itemsPerPage={postsPerPage}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
    </div>
  );
};

export default List;
