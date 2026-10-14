import {
  Body,
  Controller,
  Delete,
  Get,
  Headers,
  Post,
  Put,
  Query,
} from '@nestjs/common';
import { AppException } from '../../common/exception/app.exception';
import { ErrorCode } from '../../common/exception/error-code.enum';
import { CreateNotificationRequest } from './dto/request/create-notification.request';
import { NotificationFilterRequest } from './dto/request/notification-filter.request';
import { ViewPhoneNotificationRequest } from './dto/request/view-phone-notification.request';
import { NotificationService } from './notification.service';

@Controller('notifications')
export class NotificationController {
  constructor(private readonly notificationService: NotificationService) {}

  private extractHeader(headers: Record<string, string | undefined>, key: string): string {
    const value = headers[key];
    if (!value && key === 'x-auth-user-id') {
      throw new AppException(ErrorCode.UNAUTHORIZED);
    }
    return value ? String(value) : '';
  }

  private extractUserId(headers: Record<string, string | undefined>): number {
    const userIdStr = this.extractHeader(headers, 'x-auth-user-id');
    const parsedId = parseInt(userIdStr, 10);
    if (isNaN(parsedId)) {
      throw new AppException(ErrorCode.UNAUTHORIZED);
    }
    return parsedId;
  }

  @Get()
  async getMyNotifications(
    @Headers() headers: Record<string, string | undefined>,
    @Query() filter: NotificationFilterRequest,
  ) {
    const userId = this.extractUserId(headers);
    const isReadBool =
      filter.isRead === 'true' ? true : filter.isRead === 'false' ? false : undefined;

    return this.notificationService.getUserNotifications(
      userId,
      filter.page,
      filter.size,
      filter.sortBy,         // 🌟 FIX: Truyền thêm sortBy
      filter.sortDirection,  // 🌟 FIX: Truyền thêm sortDirection
      isReadBool,
      filter.type,
    );
  }

  @Put('mark-as-read')
  async markAsRead(@Headers() headers: Record<string, string | undefined>, @Body() ids: number[]) {
    const userId = this.extractUserId(headers);
    await this.notificationService.markAsRead(userId, ids);
    return null;
  }

  @Put('mark-all-as-read')
  async markAllAsRead(@Headers() headers: Record<string, string | undefined>) {
    const userId = this.extractUserId(headers);
    await this.notificationService.markAllAsRead(userId);
    return null;
  }

  @Get('unread-counts')
  async getUnreadCounts(@Headers() headers: Record<string, string | undefined>) {
    const userId = this.extractUserId(headers);
    return this.notificationService.getUnreadCountsByUserId(userId);
  }

  @Delete()
  async deleteNotifications(@Headers() headers: Record<string, string | undefined>, @Body() ids: number[]) {
    const userId = this.extractUserId(headers);
    await this.notificationService.deleteNotifications(userId, ids);
    return null;
  }

  @Post()
  async createNotification(@Body() request: CreateNotificationRequest) {
    await this.notificationService.createNotification(
      request.userId,
      request.message,
      request.type,
    );
    return null;
  }

  @Post('view-phone')
  async notifyViewPhone(
    @Headers() headers: Record<string, string | undefined>,
    @Body() request: ViewPhoneNotificationRequest,
  ) {
    const currentUserRole = this.extractHeader(headers, 'x-auth-user-role');
    const currentUserName = this.extractHeader(headers, 'x-auth-user-name');
    const currentUserPhone = this.extractHeader(headers, 'x-auth-user-phone');

    await this.notificationService.handleViewPhoneNotification(
      currentUserRole,
      currentUserName,
      currentUserPhone,
      request,
    );
    return null;
  }
}