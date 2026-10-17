import { NotificationType } from '../../entity/notification.entity';

export class NotificationCountResponse {
  type: NotificationType;
  count: number;

  constructor(type: NotificationType, count: number) {
    this.type = type;
    this.count = count;
  }
}