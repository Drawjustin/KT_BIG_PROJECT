/**
 * charts.js - 대시보드의 데이터 시각화를 위한 차트 컴포넌트들
 *
 * @module charts
 * @requires recharts - 차트 라이브러리
 * @requires react - React 프레임워크
 */

import { useState, useEffect } from "react";
import {
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import { mypageApi } from "../../api";

/**
 * 악성 민원 비율을 파이 차트로 표시하는 컴포넌트
 *
 * @component
 * @returns {JSX.Element} 원형 차트 컴포넌트
 */
const ComplaintRatioChart = () => {
  // 차트 데이터와 로딩 상태 관리
  const [data, setData] = useState([]);
  // const [isLoading, setIsLoading] = useState(true);

  // 차트에 사용될 색상 팔레트 정의
  // const COLORS = ["#82ca9d", "#8884d8"];
  const COLORS = ["#F44336", "#2b6cb0"]; // 악성 민원: 빨강, 기타 민원: 초록

  // const COLORS = ['#47597E', '#7FB5B5', '#FFE5B4', '#B4D4FF'];

  // 데이터 fetch 함수
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await mypageApi.getComplaintSum();
        const { totalComplaints, maliciousComplaints } = response.data;

        // 데이터 업데이트
        setData([
          { name: "악성 민원", value: maliciousComplaints },
          { name: "일반 민원", value: totalComplaints - maliciousComplaints },
        ]);
      } catch (error) {
        console.error("데이터 로드 실패:", error);
      }
    };

    fetchData();
  }, []);

  return (
    <ResponsiveContainer width="100%" height={300}>
      <PieChart>
        <Pie
          data={data}
          dataKey="value"
          nameKey="name"
          outerRadius={100}
          fill="#8884d8"
          label
        >
          {data.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip />
        <Legend /> {/* 범례 추가 */}
      </PieChart>
    </ResponsiveContainer>
  );
};
/**
 * MonthlyComplaintChart.jsx
 * 전체 민원과 악성 민원의 월별 추이를 보여주는 라인 차트 컴포넌트
 */
/**
 * 월별 민원 추이 차트 컴포넌트
 * @returns {JSX.Element} 라인 차트 컴포넌트
 */
const MonthlyComplaintChart = () => {
  // 차트 데이터 상태 관리
  const [data, setData] = useState([]);

  /**
   * 컴포넌트 마운트 시 데이터 불러오기
   */
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await mypageApi.getDailyComplaint();
        console.log("API Response:", response);

        const responseData = Array.isArray(response.data)
          ? response.data
          : Array.isArray(response.data.content)
          ? response.data.content
          : [];

        const formattedData = responseData.map((item) => ({
          date: item.date,
          "전체 민원": item.totalComplaints,
          "악성 민원": item.maliciousComplaints,
        }));

        setData(formattedData);
      } catch (error) {
        console.error("데이터 로드 실패:", error);
      }
    };

    fetchData();
  }, []);

  return (
    <ResponsiveContainer width="100%" height={250}>
      {" "}
      {/* height 조정 */}
      <LineChart
        data={data}
        margin={{
          top: 10, // 상단 여백 축소
          right: 10, // 우측 여백 축소
          left: 10, // 좌측 여백 축소
          bottom: 20, // 하단 여백 (범례를 위한 공간)
        }}
      >
        {/* 격자 표시 */}
        <CartesianGrid strokeDasharray="3 3" />

        {/* X축 설정 - 날짜 표시 */}
        <XAxis
          dataKey="date"
          padding={{ left: 30, right: 30 }}
          tickFormatter={(date) => new Date(date).getDate()}
        />

        {/* Y축 설정 - 건수 표시 */}
        <YAxis />

        {/* 툴팁 - 마우스 오버 시 상세정보 표시 */}
        {/* <Tooltip /> */}
        <Tooltip
          labelFormatter={(label) => {
            const date = new Date(label);
            return `${date.getFullYear()}-${
              date.getMonth() + 1
            }-${date.getDate()}`;
          }}
        />

        {/* 범례 표시 */}
        <Legend />

        {/* 전체 민원 라인 */}
        <Line
          type="monotone" // 부드러운 곡선으로 표시
          dataKey="전체 민원"
          stroke="#2b6cb0" // blue계열
          strokeWidth={2} // 선 굵기
          dot={{ r: 5 }} // 데이터 포인트 크기
          activeDot={{ r: 8 }} // 활성화된 데이터 포인트 크기
        />

        {/* 악성 민원 라인 */}
        <Line
          type="monotone" // 부드러운 곡선으로 표시
          dataKey="악성 민원"
          stroke="#F44336" // red 계열
          strokeWidth={2} // 선 굵기
          dot={{ r: 5 }} // 데이터 포인트 크기
          activeDot={{ r: 8 }} // 활성화된 데이터 포인트 크기
        />
      </LineChart>
    </ResponsiveContainer>
  );
};

export { ComplaintRatioChart, MonthlyComplaintChart };
