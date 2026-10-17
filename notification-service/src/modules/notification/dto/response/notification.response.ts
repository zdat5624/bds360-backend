import { Notification, NotificationType } from '../../entity/notification.entity';

export class NotificationResponse {
  id: number;
  message: string;
  type: NotificationType;
  isRead: boolean;
  createdAt: Date;

  // Mapper tích hợp thẳng trong constructor cho tiện lợi
  constructor(entity: Notification) {
    this.id = entity.id;
    this.message = entity.message;
    this.type = entity.type;
    this.isRead = entity.isRead;
    this.createdAt = entity.createdAt;
  }
}