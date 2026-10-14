export class PageResponse<T> {
  currentPage: number;
  totalPages: number;
  pageSize: number;
  totalElements: number;
  content: T[];

  // Viết một hàm helper (Factory method) giống hệt cấu trúc của Java
  static of<T>(content: T[], totalElements: number, page: number, size: number): PageResponse<T> {
    return {
      currentPage: page,
      pageSize: size,
      totalPages: Math.ceil(totalElements / size),
      totalElements,
      content,
    };
  }
}