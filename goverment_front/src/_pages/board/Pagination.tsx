// import React from 'react';

// interface PaginationProps {
//   totalItems: number; // 전체 데이터 수 (totalElements)
//   itemsPerPage: number; // 한 페이지에 표시할 데이터 수
//   currentPage: number; // 현재 페이지
//   onPageChange: (page: number) => void; // 페이지 변경 핸들러
//   totalPages: number; // 전체 페이지 수
// }
// /**페이지 이동 컴포넌트 */
// const Pagination: React.FC<PaginationProps> = ({
//   // totalItems,
//   // itemsPerPage,
//   currentPage,
//   onPageChange,
//   totalPages,
// }) => {
//   return (
//     <div className="pagination">
//       {/* 맨 처음 버튼 */}
//       <button
//         disabled={currentPage === 1}
//         onClick={() => onPageChange(1)} // 첫 페이지로 이동
//       >
//         «
//       </button>

//       {/* 이전 버튼 */}
//       <button
//         disabled={currentPage === 1}
//         onClick={() => onPageChange(currentPage - 1)}
//       >
//         이전
//       </button>

//       {/* 페이지 번호 버튼 */}
//       {Array.from({ length: totalPages }, (_, index) => index + 1).map((page) => (
//         <button
//           key={page}
//           onClick={() => onPageChange(page)}
//           className={page === currentPage ? 'active' : ''}
//         >
//           {page}
//         </button>
//       ))}

//       {/* 다음 버튼 */}
//       <button
//         disabled={currentPage === totalPages}
//         onClick={() => onPageChange(currentPage + 1)}
//       >
//         다음
//       </button>

//       {/* 맨 끝 버튼 */}
//       <button
//         disabled={currentPage === totalPages}
//         onClick={() => onPageChange(totalPages)} // 마지막 페이지로 이동
//       >
//         »
//       </button>
//     </div>
//   );
// };

// export default Pagination;



// components/Pagination.tsx
interface PaginationProps {
  currentPage: number;    // 1-based
  totalPages: number;
  onPageChange: (page: number) => void;
 }
 
 const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
 }) => {
  const getPageNumbers = () => {
    // 10개씩 페이지 번호를 보여주기 위한 계산
    const start = Math.floor((currentPage - 1) / 10) * 10 + 1;
    const end = Math.min(start + 9, totalPages);
    
    const range: number[] = [];
    for (let i = start; i <= end; i++) {
      range.push(i);
    }
    return range;
  };
 
  return (
    <div className="flex items-center justify-center gap-2 my-4">
      {/* 맨 처음으로 */}
      <button
        onClick={() => onPageChange(1)}
        disabled={currentPage === 1}
        className="px-3 py-1 rounded bg-gray-100 disabled:opacity-50"
        aria-label="맨 처음 페이지로 이동"
      >
        ≪
      </button>
      
      {/* 이전 */}
      <button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage === 1}
        className="px-3 py-1 rounded bg-gray-100 disabled:opacity-50"
        aria-label="이전 페이지로 이동"
      >
        ＜
      </button>
 
      {/* 페이지 번호들 */}
      {getPageNumbers().map((pageNum) => (
        <button
          key={pageNum}
          onClick={() => onPageChange(pageNum)}
          className={`px-3 py-1 rounded ${
            currentPage === pageNum 
              ? 'bg-blue-500 text-white' 
              : 'bg-gray-100 hover:bg-gray-200'
          }`}
        >
          {pageNum}
        </button>
      ))}
 
      {/* 다음 */}
      <button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage === totalPages}
        className="px-3 py-1 rounded bg-gray-100 disabled:opacity-50"
        aria-label="다음 페이지로 이동"
      >
        ＞
      </button>
 
      {/* 맨 마지막으로 */}
      <button
        onClick={() => onPageChange(totalPages)}
        disabled={currentPage === totalPages}
        className="px-3 py-1 rounded bg-gray-100 disabled:opacity-50"
        aria-label="맨 마지막 페이지로 이동"
      >
        ≫
      </button>
    </div>
  );
 };
 
 export default Pagination;