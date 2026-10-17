import { Injectable } from '@nestjs/common';
import { DataSource, Repository } from 'typeorm';
import { NotificationCountResponse } from './dto/response/notification-count.response';
import { Notification, NotificationType } from './entity/notification.entity';

@Injectable()
export class NotificationRepository extends Repository<Notification> {
    constructor(dataSource: DataSource) {
        // Khởi tạo Repository gốc của TypeORM
        super(Notification, dataSource.createEntityManager());
    }

    /**
     * Lấy số lượng thông báo chưa đọc, nhóm theo loại (POST, SYSTEM, PAYMENT)
     */
    async getUnreadCountsGroupByType(userId: number): Promise<NotificationCountResponse[]> {
        const results = await this.createQueryBuilder('n')
            .select('n.type', 'type')
            .addSelect('COUNT(n.id)', 'count')
            .where('n.userId = :userId', { userId })
            .andWhere('n.isRead = false')
            .groupBy('n.type')
            .getRawMany<{ type: NotificationType; count: string }>();

        const countsMap = new Map<NotificationType, number>(
            results.map((r) => [r.type, Number(r.count)]),
        );

        return Object.values(NotificationType).map(
            (t) => new NotificationCountResponse(t, countsMap.get(t) || 0),
        );
    }
}