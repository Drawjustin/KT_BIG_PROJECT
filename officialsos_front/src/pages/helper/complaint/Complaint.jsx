import ListPage from "./ListPage";
//import AnswerView from "./AnswerView";
//import { Link } from "react-router-dom";
import { Route, Routes } from "react-router-dom";
import ComplaintDetail from "./ComplaintDetail";
import styles from '../../filepage/page.module.css'


/** 민원 도우미 상위 라우팅 페이지 */
const Complaint = () => {
    return (
    <div className={styles.pageContainer}>
      <div className={styles.titleArea}>
          <h1>민원 도우미</h1>
        </div>
        <Routes>
          {/* 상위 라우트에서 기본적으로 목록 페이지를 렌더링 */}
          <Route index element={<ListPage/>} />
          
          
          {/* 민원 조회 */}
          {/* <Route path="complaint/view/:id" element={<AnswerView />} /> */}
          
          {/* 민원 글쓰기 */}
          <Route path="write/:id" element={<ComplaintDetail />} />
        </Routes>
      </div>
    );
  };
  
  export default Complaint;