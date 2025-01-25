import PropTypes from "prop-types";

const  TestContents = ({ data }) => {
    const { title, isBad, content, summary, date } = data;
    console.log("Received data:", data); // 전달된 데이터 확인

    return (
      <div className="post-detail">
        {/* Title */}
        <div className="title-section">
          <h1>{title}</h1>
          {isBad && (
            <div className="alert-message">
              ⚠️ 해당 게시글은 악성으로 판단됩니다.
            </div>
          )}
          <div>
            작성일 : {date}
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

  /** 기본값 설정 */
TestContents.defaultProps = {
    data: {
      title: "기본 제목",
      isBad: true,
      content: "기본 게시글 내용입니다.",
      summary: "기본 요약 내용입니다.",
      date: "날짜 없음",
    },
  };
  
  /** PropTypes로 props 검증 */
  TestContents.propTypes = {
    data: PropTypes.shape({
      title: PropTypes.string.isRequired,
      isBad: PropTypes.bool.isRequired,
      content: PropTypes.string.isRequired,
      summary: PropTypes.string.isRequired,
      date: PropTypes.string.isRequired,
    }),
  };
  
  export default TestContents;