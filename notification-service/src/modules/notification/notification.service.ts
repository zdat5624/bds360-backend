import { Injectable, Logger } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { FindOptionsWhere, In, Repository } from 'typeorm';
import { Notification, NotificationType } from './entity/notification.entity';
import { NotificationGateway } from './notification.gateway';

@Injectable()
export class NotificationService {
  private readonly logger = new Logger(NotificationService.name);

  constructor(
    @InjectRepository(Notification)
    private notificationRepository: Repository<Notification>,
    private notificationGateway: NotificationGateway,
  ) {}

  // 1. TẠO THÔNG BÁO
  async createNotification(userId: number, message: string, type: NotificationType) {
    const notification = this.notificationRepository.create({
      userId,
      message,
      type,
      isRead: false,
    });

    await this.notificationRepository.save(notification);
    this.logger.log(`>>> Đã tạo và đẩy thông báo thành công cho User ID: ${userId}`);

    // Đẩy WebSockets
    await this.pushUnreadCount(userId);
  }

  // 2. LẤY DANH SÁCH (Có phân trang và Sắp xếp động)
  async getUserNotifications(
    userId: number,
    page: number,
    size: number,
    sortBy: string,        // 🌟 MỚI
    sortDirection: string, // 🌟 MỚI
    isRead?: boolean,
    type?: NotificationType
  ) {
    const skip = page * size;
    
    const whereCondition: FindOptionsWhere<Notification> = { userId };
    if (isRead !== undefined) whereCondition.isRead = isRead;
    if (type) whereCondition.type = type;

    // 🌟 FIX: Thay order cứng bằng order động cấu hình từ DTO
    const orderConfig: Record<string, 'ASC' | 'DESC'> = {};
    orderConfig[sortBy] = sortDirection.toUpperCase() as 'ASC' | 'DESC';

    const [content, totalElements] = await this.notificationRepository.findAndCount({
      where: whereCondition,
      order: orderConfig,
      skip,
      take: size,
    });

    return {
      currentPage: page,
      pageSize: size,
      totalPages: Math.ceil(totalElements / size),
      totalElements,
      content,
    };
  }

  // 3. ĐÁNH DẤU ĐÃ ĐỌC
  async markAsRead(userId: number, ids: number[]) {
    if (!ids || ids.length === 0) return;
    await this.notificationRepository.update(
      { id: In(ids), userId },
      { isRead: true },
    );
    await this.pushUnreadCount(userId);
  }

  async markAllAsRead(userId: number) {
    await this.notificationRepository.update(
      { userId, isRead: false },
      { isRead: true },
    );
    await this.pushUnreadCount(userId);
  }

  // 4. XÓA THÔNG BÁO
  async deleteNotifications(userId: number, ids: number[]) {
    if (!ids || ids.length === 0) return;
    await this.notificationRepository.delete({ id: In(ids), userId });
    await this.pushUnreadCount(userId);
  }

  // 5. HELPER: ĐẨY WEBSOCKET
  async getUnreadCountsByUserId(userId: number) {
    const queryBuilder = this.notificationRepository.createQueryBuilder('n');
    const results = await queryBuilder
      .select('n.type', 'type')
      .addSelect('COUNT(n.id)', 'count')
      .where('n.userId = :userId', { userId })
      .andWhere('n.isRead = false')
      .groupBy('n.type')
      .getRawMany<{ type: NotificationType; count: string }>();

    const countsMap = new Map<NotificationType, number>(
      results.map((r) => [r.type, Number(r.count)]),
    );

    return Object.values(NotificationType).map((t) => ({
      type: t,
      count: countsMap.get(t) || 0,
    }));
  }

  private async pushUnreadCount(userId: number) {
    const detailedCounts = await this.getUnreadCountsByUserId(userId);
    this.notificationGateway.server.to(userId.toString()).emit('/topic/notifications', detailedCounts);
    this.notificationGateway.server.to(userId.toString()).emit('/user/notifications', detailedCounts);
  }

  // 6. THÔNG BÁO XEM SỐ ĐIỆN THOẠI
  async handleViewPhoneNotification(
    currentUserRole: string,
    currentUserName: string,
    currentUserPhone: string,
    request: { postId?: number; recipientId: number },
  ) {
    // Admin/Mod xem thì không cần thông báo làm phiền chủ tin
    if (currentUserRole === 'ADMIN' || currentUserRole === 'MODERATOR') {
      return;
    }

    let message = '';

    if (request.postId) {
      // 🌟 Microservice Note: Khi gọi tới API này, mặc định hiểu là Post Service 
      // đã verify điều kiện VIP >= 2 trước đó. Không cần tự query DB Post ở đây nữa.
      message = `Người dùng '${currentUserName} - ${currentUserPhone}' đã xem số điện thoại của tin đăng mã #'${request.postId}' của bạn.`;
    } else {
      message = `Người dùng '${currentUserName} - ${currentUserPhone}' đã xem số điện thoại trên trang hồ sơ của bạn.`;
    }

    // Chống spam thông báo trùng lặp
    const exist = await this.notificationRepository.findOneBy({ message });
    if (exist) return;

    await this.createNotification(request.recipientId, message, NotificationType.POST);
  }
}