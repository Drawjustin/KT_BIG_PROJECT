import MyPageLayout from "../../_components/layout/MyPageLayout";
import styled from "styled-components";
import { ComplaintRatioChart, MonthlyComplaintChart } from "./charts";
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { complaintApi, mypageApi } from "../../api";
// 스타일 컴포넌트들
const DashboardCard = styled.div`
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
  position: relative;  // 추가
  min-height: 200px;  // 추가
`;

// 기존 스타일 컴포넌트들 유지...
const DashboardGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
`;
const ChartContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 280px;  // 높이 조정
  padding: 10px;  // 패딩 추가
`;
// 원형 차트 컴포넌트
const PieChartCard = () => (
  <DashboardCard>
    <h3>이번 달, 우리 부서 민원</h3>
    <ChartContainer>
      <ComplaintRatioChart />
    </ChartContainer>
  </DashboardCard>
);

// 라인 차트 컴포넌트
const LineChartCard = () => (
  <DashboardCard>
    <h3>이번 달, 우리 부서 민원 추이</h3>
    <ChartContainer>
      <MonthlyComplaintChart />
    </ChartContainer>
  </DashboardCard>
);
// 테이블 스타일 컴포넌트 추가
const DashboardTable = styled.table`
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;

  th, td {
    padding: 12px;
    text-align: left;
    border-bottom: 1px solid #edf2f7;
  }

  th {
    font-weight: 600;
    color: #4a5568;
    background-color: #f7fafc;
  }

  tbody tr {
    &:hover {
      background-color: #f8fafc;
    }
  }

  td {
    color: #2d3748;
  }

  a {
    color: #2b6cb0;
    text-decoration: none;
    &:hover {
      text-decoration: underline;
    }
  }
`;

const TableHeader = styled.h3`
  margin: 0 0 16px 0;
  color: #2d3748;
  font-size: 18px;
  font-weight: 600;
`;

const NoDataMessage = styled.td`
  text-align: center;
  padding: 24px;
  color: #718096;
`;
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
          size: 8,
          answered: true, // 답변 완료된 데이터만 요청
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

  if (loading) return (
    <DashboardCard>
      <TableHeader>오늘, 우리 부서 답변</TableHeader>
      <p style={{ 
        position: 'absolute', 
        top: '50%', 
        left: '50%', 
        transform: 'translate(-50%, -50%)'
      }}>
        로딩 중...
      </p>
    </DashboardCard>
  );
  return (
    <DashboardCard>
      <TableHeader>오늘, 우리 부서 답변</TableHeader>
      <DashboardTable>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>답변여부</th>
          </tr>
        </thead>
        <tbody>
          {pageData?.content
            ?.filter((item) => item.answered)
            .map((item, index) => (
              <tr key={`complaint-${item.complaintSeq}`}>
                <td style={{ width: "60px" }}>{index + 1}</td>
                <td>
                  <Link to={`/complaint/write/${item.complaintSeq}`}>
                    {item.title?.trim() || "제목 없음"}
                    {item.bad ? "⚠️ " : ""}
                  </Link>
                </td>
                <td style={{ width: "100px", color: "#48bb78" }}>답변완료</td>
              </tr>
            ))}
          {pageData?.content?.filter((item) => item.answered).length === 0 && (
            <tr>
              <NoDataMessage colSpan="3">
                답변 완료된 민원이 없습니다.
              </NoDataMessage>
            </tr>
          )}
        </tbody>
      </DashboardTable>
    </DashboardCard>
  );
};
const CallServiceTableCard = () => {

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const response = await mypageApi.getDailyCalls();
        setData(response.data);
      } catch (error) {
        console.error("데이터 가져오기 실패:", error);
        setError(error.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) return (
    <DashboardCard>
      <TableHeader>오늘, 우리 부서에 들어온 전화민원</TableHeader>
      <p style={{ 
        position: 'absolute', 
        top: '50%', 
        left: '50%', 
        transform: 'translate(-50%, -50%)'
      }}>
        로딩 중...
      </p>
    </DashboardCard>
  );
  return (
    <DashboardCard>
      <TableHeader>오늘, 우리 부서에 들어온 전화민원</TableHeader>
      <div className="max-h-96 overflow-y-auto">
        <DashboardTable>
          <thead className="sticky top-0 bg-white">
            <tr>
              <th className="w-16">번호</th>
              <th className="w-24">악성여부</th>
              <th>전화링크</th>
            </tr>
          </thead>
          <tbody>
            {data.length > 0 ? (
              data.map((item, index) => (
                <tr key={index}>
                  <td>{index + 1}</td>
                  <td>
                    {item.isComplain ? (
                      <span style={{ color: 'red', fontWeight: 'bold' }}>악성</span>
                    ) : (
                      <span style={{ color: '#2b6cb0', fontWeight: 'bold' }}>정상</span>
                    )}
                  </td>
                  <td>
                    {item.telecomFilePath ? (
                      <a 
                        href={item.telecomFilePath}
                        className="text-blue-600 hover:underline"
                        target="_blank"
                        rel="noopener noreferrer"
                      >
                        통화 파일 듣기
                      </a>
                    ) : (
                      <span className="text-gray-400">파일 없음</span>
                    )}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <NoDataMessage colSpan="3">
                  오늘 접수된 전화민원이 없습니다.
                </NoDataMessage>
              </tr>
            )}
          </tbody>
        </DashboardTable>
      </div>
    </DashboardCard>
  );
};

const Dashboard = () => {
  return (
    <MyPageLayout>
      <h1>대시보드</h1>
      <DashboardGrid>
        <PieChartCard />
        <LineChartCard />
        <ComplaintTableCard />
        <CallServiceTableCard />
      </DashboardGrid>
    </MyPageLayout>
  );
};
export default Dashboard;
