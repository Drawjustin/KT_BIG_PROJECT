import PropTypes from "prop-types";
import styles from '../Answer.module.css'

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

  return (
    <div className={styles["post-detail"]}>
      <div className={styles["title-section"]}>
      <div className={styles["case-title"]}>
          <h2>사례: {title}</h2>
        </div>
        {bad && (                   // isBad -> bad
          <div className={styles["alert-message"]}>
            경고이미지 해당 게시글은 악성으로 판단됩니다.
          </div>
        )}
        <div>작성일 : {date}</div>
        <div>부서 : {departmentName}</div>    {/* department -> departmentName */}
        <div>민원 번호 : {complaintSeq}</div> {/* complaint_seq -> complaintSeq */}
        <div>작성자 : {memberName}</div>      {/* 추가 */}
      </div>

      {/* Content */}
      <div className={styles["content-section"]}>
        <div className={styles["case-content"]}>
          <h3>민원 내용</h3>
          <p>{content}</p>
        </div>
      </div>

      {/* Summary */}
      <div className={styles["summary-section"]}>
        <h3>요약</h3>
        <p>{summary}</p>
      </div>
    </div>
  );
};
Contents.propTypes = {
  data: PropTypes.shape({
    title: PropTypes.string.isRequired,
    bad: PropTypes.bool.isRequired,  // isBad -> bad
    content: PropTypes.string.isRequired,
    summary: PropTypes.string.isRequired,
    date: PropTypes.string.isRequired,
    departmentName: PropTypes.string.isRequired, // department -> departmentName
    complaintSeq: PropTypes.number.isRequired,  // complaint_seq -> complaintSeq
    memberName: PropTypes.string.isRequired,    // 추가
    filePath: PropTypes.string,                 // 추가 (필요시)
    commentResponseDTOList: PropTypes.arrayOf(   // 추가
      PropTypes.shape({
        complaintCommentSeq: PropTypes.number.isRequired,
        content: PropTypes.string.isRequired,
        updatedAt: PropTypes.string.isRequired
      })
    )
  }),
};
export default Contents;