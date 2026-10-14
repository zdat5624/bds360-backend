export class ErrorCodeDetail {
  constructor(
    public readonly code: number,
    public readonly status: number,
    public readonly message: string,
  ) {}
}

export const ErrorCode = {
  // 10. COMMON & SYSTEM
  INTERNAL_ERROR: new ErrorCodeDetail(10001, 500, 'Sự cố hệ thống máy chủ.'),
  VALIDATION_ERROR: new ErrorCodeDetail(10002, 400, 'Dữ liệu đầu vào không hợp lệ.'),
  INVALID_PARAMETER: new ErrorCodeDetail(10003, 400, 'Tham số truyền vào thiếu hoặc không hợp lệ.'),
  UNAUTHORIZED: new ErrorCodeDetail(10004, 401, 'Chưa xác thực hoặc token không hợp lệ.'),
  FORBIDDEN: new ErrorCodeDetail(10005, 403, 'Bạn không có quyền truy cập vào tài nguyên này.'),
  API_NOT_FOUND: new ErrorCodeDetail(10006, 404, 'Đường dẫn API không tồn tại trên hệ thống.'), 
  MISSING_FILE: new ErrorCodeDetail(10010, 400, 'Request không chứa file hoặc sai định dạng multipart/form-data.'),

  // 16. MEDIA
  FILE_TOO_LARGE: new ErrorCodeDetail(16001, 413, 'Kích thước file vượt quá mức cho phép.'),
  FILE_FORMAT_INVALID: new ErrorCodeDetail(16002, 415, 'Định dạng file không được hỗ trợ.'),
  FILE_UPLOAD_FAILED: new ErrorCodeDetail(16003, 500, 'Lỗi hệ thống trong quá trình lưu trữ file.'),
};