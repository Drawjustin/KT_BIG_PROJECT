import ListPage from "./ListPage";
import AnswerView from "./AnswerConfirm";
import AnswerWrite from "./Answerwrite";
//import { Link } from "react-router-dom";
import { Route, Routes } from "react-router-dom";

/**상위 라우팅 페이지 */
const Complaint = () => {
    return (
        <div className="complaint-container">
        <h1>민원 도우미</h1>
        <nav>
        
        </nav>
        <Routes>
          {/* 상위 라우트에서 기본적으로 목록 페이지를 렌더링 */}
          <Route index element={<ListPage/>} />
          
          {/* 민원 목록 */}
          <Route path="list" element={<ListPage />} />
          
          {/* 민원 조회 */}
          <Route path="view/:id" element={<AnswerView />} />
          
          {/* 민원 글쓰기 */}
          <Route path="write" element={<AnswerWrite />} />
        </Routes>
      </div>
    );
  };
  
  export default Complaint;