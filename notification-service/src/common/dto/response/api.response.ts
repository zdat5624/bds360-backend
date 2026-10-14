export interface FieldErrorDetail {
  field: string;
  message: string;
}

export interface ApiResponse<T> {
  code: number;
  message: string;
  data?: T; // Dùng optional (?) vì có API chỉ trả về null
  validationErrors?: FieldErrorDetail[];
}