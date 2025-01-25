import { useState } from "react";

const Searchbar = ({ onSearch }) => {
  const [searchTerm, setSearchTerm] = useState("");
  const [filter, setFilter] = useState("title"); // 기본 필터를 'title'로 설정

  const handleSearch = () => {
    onSearch(searchTerm, filter); // 전달받은 onSearch 함수 호출
  };

  return (
    <>
      <div>
        <select value={filter} onChange={(e) => setFilter(e.target.value)}>
          <option value="title">제목</option>
          <option value="body">내용</option>
        </select>
      </div>
      <div>
        <input
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder="검색어를 입력하세요"
        />
        <button onClick={handleSearch}>검색</button>
      </div>
    </>
  );
};

export default Searchbar;