import PropTypes from "prop-types";

const Contents = ({ data }) => {
  const { title, isBad, content, summary, date, department, complaint_seq } = data;

  return (
    <div className="post-detail">
      {/* Title */}
      <div className="title-section">
        <h1>{title}</h1>
        {isBad && (
          <div className="alert-message">
            경고이미지 해당 게시글은 악성으로 판단됩니다.
          </div>
        )}
        <div>
          작성일 : {date}
        </div>
        <div>
          부서 : {department}
        </div>
        <div>
          민원 번호 : {complaint_seq}
        </div>
      </div>

      {/* Content */}
      <div className="content-section">
        <div className="case-title">
          <h2>사례: {title}</h2>
        </div>
        <div className="case-content">
          <p>{content}</p>
        </div>
      </div>

      {/* Summary */}
      <div className="summary-section">
        <h3>요약</h3>
        <p>{summary}</p>
      </div>
    </div>
  );
};

/** PropTypes로 props 검증 */
Contents.propTypes = {
  data: PropTypes.shape({
    title: PropTypes.string.isRequired, // 제목
    isBad: PropTypes.bool.isRequired, // 악성 여부
    content: PropTypes.string.isRequired, // 내용
    summary: PropTypes.string.isRequired, // 요약
    date: PropTypes.string.isRequired, // 작성일
    department: PropTypes.string.isRequired, // 부서 정보
    complaint_seq: PropTypes.number.isRequired, // 민원 번호
  }),
};

export default Contents;