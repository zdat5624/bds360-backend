export class PageResponse<T> {
  currentPage: number;
  totalPages: number;
  pageSize: number;
  totalElements: number;
  content: T[];

  // Bổ sung constructor để khởi tạo các thuộc tính
  constructor(content: T[], totalElements: number, page: number, size: number) {
    this.currentPage = page;
    this.pageSize = size;
    this.totalPages = size > 0 ? Math.ceil(totalElements / size) : 0;
    this.totalElements = totalElements;
    this.content = content;
  }

  // Sửa lại hàm of để trả về một instance thực sự (new PageResponse)
  static of<T>(content: T[], totalElements: number, page: number, size: number): PageResponse<T> {
    return new PageResponse<T>(content, totalElements, page, size);
  }
}