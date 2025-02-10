import { useState, useEffect } from 'react';
import { PieChart, Pie, Cell, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

const API_URL = "http://localhost:5000/api/complaints";  // 백엔드 API 주소

// 파이 차트 컴포넌트 (악성 민원 비율)
const ComplaintRatioChart = () => {
  const [data, setData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  const COLORS = ['#47597E', '#7FB5B5', '#FFE5B4', '#B4D4FF'];

  useEffect(() => {
    fetch(API_URL)
      .then(response => response.json())
      .then(data => {
        if (data.complaint_ratio) {
          setData(data.complaint_ratio);
        }
        setIsLoading(false);
      })
      .catch(error => {
        console.error("Error fetching complaint ratio data:", error);
        setIsLoading(false);
      });
  }, []);

  if (isLoading) return <p>Loading...</p>;

  return (
    <div className="chart-container">
      <PieChart width={300} height={300}>
        <Pie
          data={data}
          cx={150}
          cy={150}
          innerRadius={0}
          outerRadius={100}
          fill="#8884d8"
          paddingAngle={0}
          dataKey="value"
        >
          {data.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip />
        <Legend />
      </PieChart>
    </div>
  );
};

// 바 차트 컴포넌트 (월별 민원 건수)
const MonthlyComplaintChart = () => {
  const [data, setData] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    fetch(API_URL)
      .then(response => response.json())
      .then(data => {
        if (data.monthly_complaints) {
          setData(data.monthly_complaints);
        }
        setIsLoading(false);
      })
      .catch(error => {
        console.error("Error fetching monthly complaint data:", error);
        setIsLoading(false);
      });
  }, []);

  if (isLoading) return <p>Loading...</p>;

  return (
    <div className="chart-container">
      <BarChart width={400} height={300} data={data}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="year" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Bar dataKey="value1" fill="#47597E" />
        <Bar dataKey="value2" fill="#7FB5B5" />
        <Bar dataKey="value3" fill="#FFE5B4" />
      </BarChart>
    </div>
  );
};

export { ComplaintRatioChart, MonthlyComplaintChart };