import FileList from "./FileList";
import FileView from "./FileView";
import { Route, Routes } from "react-router-dom";
import styles from './page.module.css';  // 새로운 CSS 모듈 추가
// import FileDetail from "./FileDetail";


const Filepage = () => {
  return (
    <div className={styles.pageContainer}>
      <div className={styles.titleArea}>
        <h1>자료실</h1>
      </div>
      <Routes>
        {/* 상위 라우트에서 기본적으로 목록 페이지를 렌더링 */}
        <Route index element={<FileList />} />

        {/* 자료실 조회 */}
        {/* <Route path="view/:id" element={<FileDetail />} /> */}
        <Route path="view/:id" element={<FileView />} />
      </Routes>
    </div>
  );
};

export default Filepage;