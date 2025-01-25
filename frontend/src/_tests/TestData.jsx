import TestContents from "./TestContents";

const tempData = {
  title: "도로 교통이 좋지 않습니다.",
  isBad: true,
  content: "도로교통이 너무 혼잡합니다. 개선이 필요합니다.",
  summary: "이 게시글은 사례를 소개하고 있으며, 요약하면 다음과 같습니다.",
  date: "2023-05-01",
};

const TestData = () => {
  return (
    <div>
      <h2>TestData 컴포넌트</h2>
      <TestContents data={tempData} />
    </div>
  );
};

export default TestData;