import { Injectable, Logger, OnApplicationBootstrap } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import * as fs from 'fs';
import * as path from 'path';
import { Repository } from 'typeorm';
import { Notification, NotificationType } from '../modules/notification/entity/notification.entity';

@Injectable()
export class NotificationSeeder implements OnApplicationBootstrap {
  private readonly logger = new Logger(NotificationSeeder.name);

  constructor(
    @InjectRepository(Notification)
    private readonly notificationRepository: Repository<Notification>,
  ) {}

  async onApplicationBootstrap() {
    this.logger.log('>>> START INIT NOTIFICATION DATABASE');

    const count = await this.notificationRepository.count();

    if (count > 0) {
      this.logger.log(">>> SKIP! INIT TABLE 'notifications': ALREADY HAVE DATA...");
    } else {
      await this.initSampleNotifications();
    }

    this.logger.log('>>> END INIT NOTIFICATION DATABASE');
  }

  private async initSampleNotifications() {
    this.logger.log('>>> Bắt đầu tạo 1000 thông báo...');
    const localNotifications: Notification[] = [];
    const totalNotifications = 1000;
    const batchSize = 200; // Lưu từng mẻ 200 bản ghi để tối ưu hiệu suất Node.js

    // 1. Đọc và lấy danh sách User hợp lệ (Từ file CSV)
    const validUsers = this.getValidUsersFromCsv();
    if (validUsers.length === 0) {
      this.logger.error('>>> KHÔNG TÌM THẤY USER NÀO TỪ CSV. HỦY SEEDING!');
      return;
    }

    const now = new Date();

    // 2. Vòng lặp tạo 1000 thông báo
    for (let i = 1; i <= totalNotifications; i++) {
      // ⚡ Chọn ngẫu nhiên 1 user
      const randomUser = validUsers[Math.floor(Math.random() * validUsers.length)];
      
      const notificationTypeIndex = Math.floor(Math.random() * 8); // Random từ 0 -> 7 (Giữ nguyên 8 case)
      let message = '';
      let type: NotificationType = NotificationType.SYSTEM; // Khởi tạo mặc định để linter không báo lỗi

      // ⚡ Giả lập dữ liệu Post & Transaction vì không còn truy cập được DB của chúng
      const randomPostId = Math.floor(Math.random() * 100) + 1;
      const randomAmount = (Math.floor(Math.random() * 95) + 5) * 10000; // Số tiền từ 50,000 -> 1,000,000

      switch (notificationTypeIndex) {
        case 0: // POST: Người dùng xem tin đăng
          message = `Người dùng '${randomUser.name} - ${randomUser.phone}' đã xem số điện thoại của tin đăng mã '${randomPostId}' của bạn.`;
          type = NotificationType.POST;
          break;

        case 1: // POST: Tin đăng được chấp nhận
          message = `Tin đăng mã '${randomPostId}' của bạn đã được kiểm duyệt viên chấp nhận.`;
          type = NotificationType.POST;
          break;

        case 2: // POST: Tin đăng bị từ chối
          message = `Tin đăng mã '${randomPostId}' của bạn đã bị từ chối do vi phạm điều khoản.`;
          type = NotificationType.POST;
          break;

        case 3: // TRANSACTION: Nạp tiền thành công
          message = `Giao dịch thành công. Tài khoản của bạn được cộng ${randomAmount.toLocaleString('vi-VN')} VNĐ.`;
          type = NotificationType.PAYMENT; // Spring Boot cũ gọi là TRANSACTION, NestJS gọi là PAYMENT
          break;

        case 4: // TRANSACTION: Trừ tiền
          message = `Thanh toán thành công. Tài khoản của bạn bị trừ ${randomAmount.toLocaleString('vi-VN')} VNĐ cho dịch vụ đăng tin mới.`;
          type = NotificationType.PAYMENT;
          break;

        case 5: // SYSTEM_ALERT: Cảnh báo sắp hết hạn VIP
          message = `Gói VIP của bạn sẽ hết hạn trong vòng 3 ngày tới. Vui lòng gia hạn để duy trì quyền lợi.`;
          type = NotificationType.SYSTEM; // Spring Boot cũ: SYSTEM_ALERT
          break;

        case 6: // SYSTEM_ALERT: Bảo trì hệ thống
          message = `Hệ thống BDS360 sẽ bảo trì định kỳ từ 00:00 đến 02:00 sáng mai. Xin lỗi vì sự bất tiện này!`;
          type = NotificationType.SYSTEM;
          break;

        case 7: // POST: Tin đăng bị khóa do vi phạm
          message = `Tin đăng mã '${randomPostId}' của bạn đã bị khóa do vi phạm chính sách của hệ thống.`;
          type = NotificationType.POST;
          break;
      }

      // Tạo đối tượng Notification
      const notification = this.notificationRepository.create({
        userId: randomUser.id,
        message: message,
        type: type,
        isRead: Math.random() > 0.5, // boolean ngẫu nhiên
      });

      // Tạo thời gian lùi lại ngẫu nhiên tối đa 30 ngày (30 * 24 * 60 * 60 * 1000 ms)
      const randomMillis = Math.floor(Math.random() * (30 * 24 * 60 * 60 * 1000));
      notification.createdAt = new Date(now.getTime() - randomMillis);

      localNotifications.push(notification);

      // 3. Batch Insert: Lưu từng mẻ để tránh tràn RAM
      if (localNotifications.length >= batchSize) {
        await this.notificationRepository.save(localNotifications);
        this.logger.log(`>>> Đã lưu thành công ${i} / ${totalNotifications} thông báo.`);
        localNotifications.length = 0; // Xóa mảng để chứa mẻ mới
      }
    }

    // Lưu nốt số thông báo còn sót lại
    if (localNotifications.length > 0) {
      await this.notificationRepository.save(localNotifications);
      this.logger.log(`>>> Đã lưu thành công mẻ thông báo cuối cùng.`);
    }

    this.logger.log(">>> INIT DATA TABLE 'notifications': SUCCESS");
  }

  /**
   * Đọc file CSV và lấy danh sách User hợp lệ (Bỏ qua Admin)
   */
  private getValidUsersFromCsv(): { id: number; name: string; phone: string }[] {
    const validUsers: { id: number; name: string; phone: string }[] = [];
    const csvPath = path.join(process.cwd(), 'src', 'bootstrap', 'data', 'users.csv');

    if (!fs.existsSync(csvPath)) {
      return [];
    }

    const csvData = fs.readFileSync(csvPath, 'utf8');
    const lines = csvData.split('\n');

    for (let i = 1; i < lines.length; i++) {
      const line = lines[i].trim();
      if (!line) continue;

      // Regex tách chuỗi giống Java
      const cols = line.split(/,(?=(?:(?:[^"]*"){2})*[^"]*$)/);
      const cleanCols = cols.map((col) => col.replace(/^"|"$/g, '').trim());

      const idStr = cleanCols[0];
      const name = cleanCols[9] || 'Người dùng ẩn danh';
      const phone = cleanCols[11] || '09xxxxxxxx';
      const role = cleanCols[12] || '';

      const userId = idStr ? parseInt(idStr, 10) : i;

      // Bỏ qua admin
      if (role === 'ADMIN') continue;

      validUsers.push({ id: userId, name, phone });
    }

    return validUsers;
  }
}