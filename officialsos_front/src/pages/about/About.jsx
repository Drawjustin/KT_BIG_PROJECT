import styles from "./About.module.css";

const About = () => {
  const services = [
    {
      title: "민원 도우미 서비스",
      description:
        "민원도우미는 공무원이 민원을 처리할 때 AI 기술을 활용하여 악성민원을 미리 판단하고, 민원 내용을 요약하며 적절한 예시 답변을 제시해주는 지능형 서비스입니다.",
      icon: "📝", // 이모지는 예시입니다. lucide-react 아이콘으로 대체 가능
    },
    {
      title: "공문서 도우미 서비스",
      description:
        "공문서도우미는 공무원의 공문서 작성을 지원하는 AI 서비스입니다. 관련 질문이나 파일을 전송하면 코사인 유사도 분석을 통해 유사 공문서를 찾아주고 핵심 내용을 요약해드립니다.",
      icon: "📄",
    },
    {
      title: "자료실",
      description:
        "자료실은 국가 공문서와 내부규정서의 최신 버전을 체계적으로 관리하고 제공하는 공간입니다. 언제나 최신 정보를 확인하실 수 있습니다.",
      icon: "📚",
    },
  ];

  return (
    <div className={styles.pageContainer}>
      <div className={styles.titleArea}>
        <h1>이용안내</h1>
      </div>
      <div className={styles.servicesGrid}>
        {services.map((service, index) => (
          <div key={index} className={styles.serviceCard}>
            <div className={styles.serviceIcon}>{service.icon}</div>
            <h2 className={styles.serviceTitle}>{service.title}</h2>
            <p className={styles.serviceDescription}>{service.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default About;
