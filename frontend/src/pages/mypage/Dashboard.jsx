import MyPageLayout from "../../_components/layout/MyPageLayout";
import styled from "styled-components";
import { ComplaintRatioChart, MonthlyComplaintChart } from "./charts";
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { complaintApi } from "../../api";
// 공통 카드 스타일
const DashboardCard = styled.div`
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
`;

// 대시보드 그리드
const DashboardGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
`;
const ChartContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
`;

// 원형 차트 컴포넌트
const PieChartCard = () => (
  <DashboardCard>
    <h3>악성 민원 비율</h3>
    <ChartContainer>
      <ComplaintRatioChart />
    </ChartContainer>
  </DashboardCard>
);

// 막대 차트 컴포넌트
const BarChartCard = () => (
  <DashboardCard>
    <h3>월/년 민원 건수</h3>
    <ChartContainer>
      <MonthlyComplaintChart />
    </ChartContainer>
  </DashboardCard>
);

const ComplaintTableCard = () => {
  const [pageData, setPageData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchListData = async () => {
      try {
        setLoading(true);
        const params = {
          page: 0,
          size: 4, // 대시보드에는 4개만 표시
          departmentSeq: 1,
        };
        const response = await complaintApi.getList(params);
        setPageData(response.data);
      } catch (error) {
        console.error("데이터 가져오는 중 오류 발생:", error);
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchListData();
  }, []);

  if (loading) return <p>로딩 중...</p>;
  if (error) return <p>오류 발생: {error}</p>;

  return (
    <DashboardCard>
      <h3>민원 도우미</h3>
      <table style={{ width: "100%", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>답변여부</th>
          </tr>
        </thead>
        <tbody>
          {pageData?.content?.slice(0, 4).map((item, index) => (
            <tr key={`complaint-${item.complaintSeq}`}>
              <td>{index + 1}</td>
              <td>
                <Link to={`/complaint/write/${item.complaintSeq}`}>
                  {item.bad ? "⚠️" : ""}
                  {item.title && item.title.trim() !== ""
                    ? item.title
                    : "제목 없음"}
                </Link>
              </td>
              <td style={{ color: item.answered ? "green" : "red" }}>
                {item.answered ? "답변완료" : "답변대기"}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </DashboardCard>
  );
};
// // 민원 도우미 테이블 컴포넌트
// const ComplaintTableCard = () => (
//   <DashboardCard>
//     <h3>민원 도우미</h3>
//     <table style={{ width: '100%', borderCollapse: 'collapse' }}>
//       <thead>
//         <tr>
//           <th>번호</th>
//           <th>제목</th>
//         </tr>
//       </thead>
//       <tbody>
//         {/* 실제 데이터로 매핑 */}
//         {[1,2,3,4].map((item, index) => (
//           <tr key={index}>
//             <td>{index + 1}</td>
//             <td>민원 항목 {index + 1}</td>
//           </tr>
//         ))}
//       </tbody>
//     </table>
//   </DashboardCard>
// );

// 전화 서비스 테이블 컴포넌트
const CallServiceTableCard = () => (
  <DashboardCard>
    <h3>전화 서비스</h3>
    <table style={{ width: "100%", borderCollapse: "collapse" }}>
      <thead>
        <tr>
          <th>번호</th>
          <th>제목</th>
        </tr>
      </thead>
      <tbody>
        {/* 실제 데이터로 매핑 */}
        {[1, 2, 3, 4].map((item, index) => (
          <tr key={index}>
            <td>{index + 1}</td>
            <td>전화 서비스 항목 {index + 1}</td>
          </tr>
        ))}
      </tbody>
    </table>
  </DashboardCard>
);

const Dashboard = () => {
  return (
    <MyPageLayout>
      <h1>대시보드</h1>
      <DashboardGrid>
        <PieChartCard />
        <BarChartCard />
        <ComplaintTableCard />
        <CallServiceTableCard />
      </DashboardGrid>
    </MyPageLayout>
  );
};
export default Dashboard;
