import TestAnswerForm from "./TestAnswerForm";
const TestWrapper = () => {
  // 더미 데이터를 반환하는 함수
  const mockGenerateAnswer = async () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve("이것은 AI가 생성한 더미 답변입니다.");
      }, 1000);
    });
  };

  return (
    <div>
      <h2>TestAnswerForm 컴포넌트 테스트</h2>
      <TestAnswerForm
        initialAnswer="초기 답변입니다."
        mockGenerateAnswer={mockGenerateAnswer} // 올바른 함수 전달
      />
    </div>
  );
};

export default TestWrapper;