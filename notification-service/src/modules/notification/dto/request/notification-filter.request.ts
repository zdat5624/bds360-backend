import { IsBooleanString, IsEnum, IsOptional } from 'class-validator';
import { BaseFilterRequest } from '../../../../common/dto/request/base-filter.request';
import { NotificationType } from '../../entity/notification.entity';

export class NotificationFilterRequest extends BaseFilterRequest {
  @IsOptional()
  @IsBooleanString() // Kiểm tra chuỗi 'true' hoặc 'false' từ query param
  isRead?: string;

  @IsOptional()
  @IsEnum(NotificationType, { message: 'Loại thông báo không hợp lệ' })
  type?: NotificationType;
}