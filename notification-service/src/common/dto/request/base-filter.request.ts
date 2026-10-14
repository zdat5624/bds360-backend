import { Type } from 'class-transformer';
import { IsInt, IsOptional, IsString, Max, Min } from 'class-validator';

export class BaseFilterRequest {
  @IsOptional()
  @Type(() => Number) // Ép kiểu string từ URL query sang number
  @IsInt({ message: 'Trang (page) phải là số nguyên' })
  @Min(0, { message: 'Trang (page) không được nhỏ hơn 0' })
  page: number = 0;

  @IsOptional()
  @Type(() => Number)
  @IsInt({ message: 'Kích thước trang (size) phải là số nguyên' })
  @Min(1, { message: 'Kích thước trang (size) phải lớn hơn 0' })
  @Max(100, { message: 'Kích thước trang (size) tối đa là 100' })
  size: number = 10;

  @IsOptional()
  @IsString()
  sortBy: string = 'id';

  @IsOptional()
  @IsString()
  sortDirection: 'ASC' | 'DESC' = 'DESC'; // TypeScript xịn hơn Java ở chỗ có union type
}