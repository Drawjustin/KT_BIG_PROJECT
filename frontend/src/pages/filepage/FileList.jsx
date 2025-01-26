import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Pagination from "../../_components/button/Pagination";
import { mockPosts } from "../mocks/posts";
import SearchBar from "../../_components/button/SearchBar";

const FileList = () => {
  const [posts, setPosts] = useState(mockPosts);
  const [filteredPosts, setFilteredPosts] = useState(mockPosts);
  const [currentPage, setCurrentPage] = useState(1);
  const postsPerPage = 10;

  const indexOfLastPost = currentPage * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = filteredPosts.slice(indexOfFirstPost, indexOfLastPost);

  const handleSearch = (searchTerm, filter) => {
    const filtered = posts.filter((post) => {
      if (filter === "title") {
        return post.title.toLowerCase().includes(searchTerm.toLowerCase());
      } else if (filter === "body") {
        return post.body.toLowerCase().includes(searchTerm.toLowerCase());
      }
      return post;
    });
    setFilteredPosts(filtered);
  };

  return (
    <div>
      <h1>게시글 목록</h1>
      <SearchBar onSearch={handleSearch} />
      <table>
        <thead>
          <th>번호</th>
          <th>제목</th>
          <th>작성자</th>
          <th>작성일</th>
        </thead>
        <tbody>
        {currentPosts.map((post) => (
          <tr key={post.id}>
            <td>{post.id}</td>
            <td><Link to={`/dataroom/view/${post.id}`}>{post.title}</Link></td>
            <td>{post.author}</td>
            <td>{post.createdAt}</td>
          </tr>
        ))}
      </tbody>
      </table>
      <Pagination
        totalPosts={filteredPosts.length}
        postsPerPage={postsPerPage}
        currentPage={currentPage}
        setCurrentPage={setCurrentPage}
      />
    </div>
  );
};

export default FileList;