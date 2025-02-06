import PropTypes from "prop-types";
import styles from '../Answer.module.css'

/** 저장된 답변 데이터 렌더링 */
const AnswerSave = ({ data }) => {
   // 기본 데이터 설정
   const defaultData = {
    title: "맛집 추천",
    content: `안녕하세요

음식에는 한식,일식,양식,분식이 있습니다.
한식 맛집으로는 밥상머리, 현주식당, 신가네 부대찌개가 있습니다.
일식에는 스시생선가게, 양식에는 알레 피자&파스타, 분식에는 신전 떡볶이가 있습니다.
원하시는 종류에 맞는 맛집 추천해드렸습니다.

참조맛집리스트: 에이블 2조 노션`,
    memberName: "조강윤",
    departmentName: "진해구청/행정과",
    updatedAt: "2024-02-07",
    filePath: ""
  };

  // data가 없으면 기본 데이터 사용
  const displayData = data || defaultData;
  // if (!data) return <p>저장된 답변이 없습니다.</p>;

  // const { title, content, memberName, departmentName, updatedAt, filePath } = data;
  const { title, content, memberName, departmentName, updatedAt, filePath } = displayData;
  
  return (
    <div className={styles.form}>
      <div className={styles.section}>
        <div className={styles.summaryHeader}>
          <h3>답변 제목 | {title}</h3>
        </div>
        <div className={styles.sectionContent}>
          <p>{content}</p>
        </div>
      </div>
      
      <div className={styles.infoBox}>
        <div className={styles.infoRow}>
          <div className={styles.infoLabel}>답변 담당자</div>
          <div className={styles.infoValue}>{memberName}</div>
        </div>
        <div className={styles.infoRow}>
          <div className={styles.infoLabel}>담당 부서</div>
          <div className={styles.infoValue}>{departmentName}</div>
        </div>
        <div className={styles.infoRow}>
          <div className={styles.infoLabel}>답변일</div>
          <div className={styles.infoValue}>{updatedAt}</div>
        </div>
        <div className={styles.infoRow}>
          <div className={styles.infoLabel}>첨부 파일</div>
          <div className={styles.infoValue}>{filePath}</div>
        </div>
      </div>
    </div>
  );
};

AnswerSave.propTypes = {
  data: PropTypes.shape({
    content: PropTypes.string,
    memberName: PropTypes.string,
    departmentName: PropTypes.string,
  }),
};



export default AnswerSave;