import FileList from "./FileList";
import FileView from "./FileView";
import { Route, Routes } from "react-router-dom";

const Filepage = () => {
  return (
    <div className="complaint-container">
      <h1>자료실</h1>
      <nav></nav>
      <Routes>
        {/* 상위 라우트에서 기본적으로 목록 페이지를 렌더링 */}
        <Route index element={<FileList />} />

        {/* 자료실 조회 */}
        <Route path="view/:id" element={<FileView />} />
      </Routes>
    </div>
  );
};

export default Filepage;