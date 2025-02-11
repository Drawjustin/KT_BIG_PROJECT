// 📂 src/components/ResultItem.js
import styles from "./ResultItem.module.css";

const ResultItem = ({ item }) => {
  return (
    <div className={styles.resultItem}>
      <h4 className={styles.itemTitle}>{item.title}</h4>
      <p className={styles.meta}>
        <span>{item.ministry}</span> | <span>{item.department}</span> | <span>{item.document_issue_date}</span>
      </p>
      <p className={styles.summary}>{item.summary}</p>

      {item.file_info && item.file_info.length > 0 && (
        <div className={styles.fileSection}>
          <strong>첨부 파일:</strong>
          <ul>
            {item.file_info.map((file, idx) => (
              <li key={idx}>{file.file_name}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default ResultItem;