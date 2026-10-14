import { IsEnum, IsInt, IsNotEmpty, IsString } from 'class-validator';
import { NotificationType } from '../../entity/notification.entity';

export class CreateNotificationRequest {
  @IsInt()
  @IsNotEmpty({ message: 'UserId không được để trống' })
  userId: number;

  @IsString()
  @IsNotEmpty({ message: 'Nội dung thông báo không được để trống' })
  message: string;

  @IsEnum(NotificationType, { message: 'Loại thông báo không hợp lệ' })
  type: NotificationType;
}