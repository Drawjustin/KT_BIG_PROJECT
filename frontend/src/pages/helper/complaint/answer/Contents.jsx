import PropTypes from "prop-types";
import styles from '../Answer.module.css'

// const Contents = ({ data }) => {
//   const { 
//     title, 
//     bad,              // isBad -> bad
//     content, 
//     summary, 
//     date, 
//     departmentName,   // department -> departmentName
//     complaintSeq,     // complaint_seq -> complaintSeq
//     memberName        // 추가
//   } = data;

//   return (
//     <div className={styles["post-detail"]}>
//       <div className={styles["title-section"]}>
//       <div className={styles["case-title"]}>
//           <h2>사례: {title}</h2>
//         </div>
//         {bad && (                   // isBad -> bad
//           <div className={styles["alert-message"]}>
//             경고이미지 해당 게시글은 악성으로 판단됩니다.
//           </div>
//         )}
//         <div>작성일 : {date}</div>
//         <div>부서 : {departmentName}</div>    {/* department -> departmentName */}
//         <div>민원 번호 : {complaintSeq}</div> {/* complaint_seq -> complaintSeq */}
//         <div>작성자 : {memberName}</div>      {/* 추가 */}
//       </div>

//       {/* Content */}
//       <div className={styles["content-section"]}>
//         <div className={styles["case-content"]}>
//           <h3>민원 내용</h3>
//           <p>{content}</p>
//         </div>
//       </div>

//       {/* Summary */}
//       <div className={styles["summary-section"]}>
//         <h3>요약</h3>
//         <p>{summary}</p>
//       </div>
//     </div>
//   );
// };
const Contents = ({ data }) => {
  const { 
    title, 
    bad,              // isBad -> bad
    content, 
    summary, 
    date, 
    departmentName,   // department -> departmentName
    complaintSeq,     // complaint_seq -> complaintSeq
    memberName        // 추가
  } = data;
  // 작성일 포맷팅
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    }).replace(/\. /g, '-').replace('.', '');
  };

  return (
    <div className={styles.form}>
      {/* 사례 섹션 */}
      {/* 정보 박스 추가 */}
      <div className={styles.infoBox}>
        <div className={styles.infoRow}>
            <div className={styles.infoLabel}>민원번호</div>
            <div className={styles.infoValue}>{complaintSeq}</div>
          </div>
          <div className={styles.infoRow}>
            <div className={styles.infoLabel}>민원 작성자</div>
            <div className={styles.infoValue}>{memberName}</div>
          </div>
          <div className={styles.infoRow}>
            <div className={styles.infoLabel}>민원 작성일</div>
            <div className={styles.infoValue}>{formatDate(date)}</div>
          </div>
          <div className={styles.infoRow}>
            <div className={styles.infoLabel}>답변 담당부서</div>
            <div className={styles.infoValue}>{departmentName}</div>
          </div>
        </div>
      
      <div className={styles.section}>
        <div className={styles.summaryHeader}>
          <h3>민원 제목  |  {title}</h3>
        </div>
        <div className={styles.sectionContent}>
          <p>{content}</p>
        </div>
      </div>

      {/* 요약 섹션 */}
      <div className={styles.section}>
        <div className={styles.AIsummaryHeader}>
          <h3>AI 요약 서비스</h3>
          <div className={styles.sectionContent}>
          <p>{summary}</p>
        </div>
        
        </div>
      </div>
    </div>
  );
};

Contents.propTypes = {
  data: PropTypes.shape({
    title: PropTypes.string.isRequired,
    bad: PropTypes.bool.isRequired,
    content: PropTypes.string.isRequired,
    summary: PropTypes.string.isRequired,
    departmentName: PropTypes.string.isRequired,
    complaintSeq: PropTypes.number.isRequired,
    memberName: PropTypes.string,
    filePath: PropTypes.string,
    date: PropTypes.string.isRequired,

    commentResponseDTOList: PropTypes.arrayOf(
      PropTypes.shape({
        complaintCommentSeq: PropTypes.number.isRequired,
        content: PropTypes.string.isRequired,
        updatedAt: PropTypes.string.isRequired
      })
    )
  }),
};

export default Contents;