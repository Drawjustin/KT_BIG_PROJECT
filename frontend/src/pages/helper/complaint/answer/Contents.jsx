const  Contents = ({ data }) => {
  const { title, isBad, content, summary, date } = data;

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

export default Contents;