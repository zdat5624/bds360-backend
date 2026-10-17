import { Role } from '@/common/constant/role.enum';
import { PageResponse } from '@/common/dto/response/page.response';
import { UserContext } from '@/security/dto/user-context.dto';
import { Injectable, Logger } from '@nestjs/common';
import { FindOptionsWhere, In } from 'typeorm';
import { NotificationCountResponse } from './dto/response/notification-count.response';
import { NotificationResponse } from './dto/response/notification.response';
import { Notification, NotificationType } from './entity/notification.entity';
import { NotificationGateway } from './notification.gateway';
import { NotificationRepository } from './notification.repository';

@Injectable()
export class NotificationService {
  private readonly logger = new Logger(NotificationService.name);

  constructor(
    private readonly notificationRepo: NotificationRepository,
    private readonly notificationGateway: NotificationGateway,
  ) { }

  // 1. TẠO THÔNG BÁO
  async createNotification(userId: number, message: string, type: NotificationType) {
    const notification = this.notificationRepo.create({
      userId,
      message,
      type,
      isRead: false,
    });

    await this.notificationRepo.save(notification);
    this.logger.log(`>>> Đã tạo và đẩy thông báo thành công cho User ID: ${userId}`);

    // Đẩy WebSockets
    await this.pushUnreadCount(userId);
  }

  // 2. LẤY DANH SÁCH (Có phân trang và Sắp xếp động)
  async getUserNotifications(
    userId: number,
    page: number,
    size: number,
    sortBy: string,
    sortDirection: string,
    isRead?: boolean,
    type?: NotificationType,
  ): Promise<PageResponse<NotificationResponse>> {
    const skip = page * size;
    const whereCondition: FindOptionsWhere<Notification> = { userId };
    if (isRead !== undefined) whereCondition.isRead = isRead;
    if (type) whereCondition.type = type;

    const orderConfig: Record<string, 'ASC' | 'DESC'> = {};
    orderConfig[sortBy] = sortDirection.toUpperCase() as 'ASC' | 'DESC';

    const [content, totalElements] = await this.notificationRepo.findAndCount({
      where: whereCondition,
      order: orderConfig,
      skip,
      take: size,
    });

    // Map entity sang DTO Response
    const responseContent = content.map((item) => new NotificationResponse(item));

    return PageResponse.of(responseContent, totalElements, page, size);
  }

  // 3. ĐÁNH DẤU ĐÃ ĐỌC
  async markAsRead(userId: number, ids: number[]) {
    if (!ids || ids.length === 0) return;
    await this.notificationRepo.update(
      { id: In(ids), userId },
      { isRead: true },
    );
    await this.pushUnreadCount(userId);
  }

  async markAllAsRead(userId: number) {
    await this.notificationRepo.update(
      { userId, isRead: false },
      { isRead: true },
    );
    await this.pushUnreadCount(userId);
  }

  // 4. XÓA THÔNG BÁO
  async deleteNotifications(userId: number, ids: number[]) {
    if (!ids || ids.length === 0) return;
    await this.notificationRepo.delete({ id: In(ids), userId });
    await this.pushUnreadCount(userId);
  }

  // 5. HELPER: ĐẨY WEBSOCKET
  async getUnreadCountsByUserId(userId: number): Promise<NotificationCountResponse[]> {
    // Gọi thẳng sang Repository
    return this.notificationRepo.getUnreadCountsGroupByType(userId);
  }

  private async pushUnreadCount(userId: number) {
    const detailedCounts = await this.getUnreadCountsByUserId(userId);
    this.notificationGateway.server.to(userId.toString()).emit('/topic/notifications', detailedCounts);
    this.notificationGateway.server.to(userId.toString()).emit('/user/notifications', detailedCounts);
  }

  // 6. THÔNG BÁO XEM SỐ ĐIỆN THOẠI
  async handleViewPhoneNotification(
    currentUser: UserContext,
    request: { postId?: number; recipientId: number },
  ) {
    // Admin/Mod xem thì không cần thông báo làm phiền chủ tin
    if (currentUser.role === Role.ADMIN || currentUser.role === Role.MODERATOR) {
      return;
    }

    let message = '';

    if (request.postId) {
      message = `Người dùng '${currentUser.name} - ${currentUser.phone}' đã xem số điện thoại của tin đăng mã #'${request.postId}' của bạn.`;
    } else {
      message = `Người dùng '${currentUser.name} - ${currentUser.phone}' đã xem số điện thoại trên trang hồ sơ của bạn.`;
    }

    // Chống spam thông báo trùng lặp
    const exist = await this.notificationRepo.findOneBy({ message });
    if (exist) return;

    await this.createNotification(request.recipientId, message, NotificationType.POST);
  }
}