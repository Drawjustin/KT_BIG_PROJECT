import PropTypes from "prop-types";
import styles from '../Answer.module.css'
/** 저장된 답변 데이터 렌더링 */
const AnswerSave = ({ data }) => {
  if (!data) return <p>저장된 답변이 없습니다.</p>;

  const { title, content, memberName, departmentName, updatedAt, filePath } = data;

  return (
    <div className={styles["answer-view-container"]}>
      <h3>저장된 답변</h3>
      <p>
        <strong>제목:</strong> {title}
      </p>
      <p>
        <strong>내용:</strong> {content}
      </p>
      <p>
        <strong>작성자:</strong> {memberName} ({departmentName})
      </p>
      <p>
        <strong>작성일:</strong> {new Date(updatedAt).toLocaleString()}
      </p>
      {filePath && (
        <p>
          <strong>첨부파일:</strong> <a href={filePath} target="_blank" rel="noopener noreferrer">파일 보기</a>
        </p>
      )}
    </div>
  );
};

AnswerSave.propTypes = {
  data: PropTypes.shape({
    title: PropTypes.string,
    content: PropTypes.string,
    memberName: PropTypes.string,
    departmentName: PropTypes.string,
    updatedAt: PropTypes.string,
    filePath: PropTypes.string,
  }),
};

export default AnswerSave;