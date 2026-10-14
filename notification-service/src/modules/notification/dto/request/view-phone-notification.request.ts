import { IsInt, IsNotEmpty, IsOptional } from 'class-validator';

export class ViewPhoneNotificationRequest {
  @IsOptional()
  @IsInt()
  postId?: number; // Có thể xem ở trang cá nhân nên không bắt buộc

  @IsInt()
  @IsNotEmpty({ message: 'RecipientId không được để trống' })
  recipientId: number;
}