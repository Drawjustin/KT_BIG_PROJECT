import React from 'react';

interface PaginationProps {
  totalItems: number; // 전체 데이터 수 (totalElements)
  itemsPerPage: number; // 한 페이지에 표시할 데이터 수
  currentPage: number; // 현재 페이지
  onPageChange: (page: number) => void; // 페이지 변경 핸들러
  totalPages: number; // 전체 페이지 수
}
/**페이지 이동 컴포넌트 */
const Pagination: React.FC<PaginationProps> = ({
  // totalItems,
  // itemsPerPage,
  currentPage,
  onPageChange,
  totalPages,
}) => {
  return (
    <div className="pagination">
      {/* 맨 처음 버튼 */}
      <button
        disabled={currentPage === 1}
        onClick={() => onPageChange(1)} // 첫 페이지로 이동
      >
        «
      </button>

      {/* 이전 버튼 */}
      <button
        disabled={currentPage === 1}
        onClick={() => onPageChange(currentPage - 1)}
      >
        이전
      </button>

      {/* 페이지 번호 버튼 */}
      {Array.from({ length: totalPages }, (_, index) => index + 1).map((page) => (
        <button
          key={page}
          onClick={() => onPageChange(page)}
          className={page === currentPage ? 'active' : ''}
        >
          {page}
        </button>
      ))}

      {/* 다음 버튼 */}
      <button
        disabled={currentPage === totalPages}
        onClick={() => onPageChange(currentPage + 1)}
      >
        다음
      </button>

      {/* 맨 끝 버튼 */}
      <button
        disabled={currentPage === totalPages}
        onClick={() => onPageChange(totalPages)} // 마지막 페이지로 이동
      >
        »
      </button>
    </div>
  );
};

export default Pagination;