import { CurrentUser } from '@/security/decorator/current-user.decorator';
import {
  Body,
  Controller,
  Delete,
  Get,
  Logger,
  Post,
  Put,
  Query,
} from '@nestjs/common';
import { PageResponse } from '../../common/dto/response/page.response';
import { RequireLogin } from '../../security/decorator/auth.decorator';
import { UserContext } from '../../security/dto/user-context.dto';
import { CreateNotificationRequest } from './dto/request/create-notification.request';
import { NotificationFilterRequest } from './dto/request/notification-filter.request';
import { ViewPhoneNotificationRequest } from './dto/request/view-phone-notification.request';
import { NotificationCountResponse } from './dto/response/notification-count.response';
import { NotificationResponse } from './dto/response/notification.response';
import { NotificationService } from './notification.service';

@Controller('notifications')
export class NotificationController {
  constructor(private readonly notificationService: NotificationService) { }

  @Get()
  @RequireLogin()
  async getMyNotifications(
    @CurrentUser() user: UserContext,
    @Query() filter: NotificationFilterRequest,
  ): Promise<PageResponse<NotificationResponse>> {
    const isReadBool =
      filter.isRead === 'true' ? true : filter.isRead === 'false' ? false : undefined;

    return this.notificationService.getUserNotifications(
      user.id,
      filter.page,
      filter.size,
      filter.sortBy,
      filter.sortDirection,
      isReadBool,
      filter.type,
    );
  }

  @Put('mark-as-read')
  @RequireLogin()
  async markAsRead(
    @CurrentUser() user: UserContext,
    @Body() ids: number[],
  ): Promise<null> {
    await this.notificationService.markAsRead(user.id, ids);
    return null;
  }

  @Put('mark-all-as-read')
  @RequireLogin()
  async markAllAsRead(@CurrentUser() user: UserContext): Promise<null> {
    await this.notificationService.markAllAsRead(user.id);
    return null;
  }

  @Get('unread-counts')
  @RequireLogin()
  async getUnreadCounts(
    @CurrentUser() user: UserContext,
  ): Promise<NotificationCountResponse[]> {
    return this.notificationService.getUnreadCountsByUserId(user.id);
  }

  @Delete()
  @RequireLogin()
  async deleteNotifications(
    @CurrentUser() user: UserContext,
    @Body() ids: number[],
  ): Promise<null> {
    await this.notificationService.deleteNotifications(user.id, ids);
    return null;
  }

  @Post()
  @RequireLogin()
  async createNotification(
    @Body() request: CreateNotificationRequest,
  ): Promise<null> {
    await this.notificationService.createNotification(
      request.userId,
      request.message,
      request.type,
    );
    return null;
  }

  @Post('view-phone')
  @RequireLogin()
  async notifyViewPhone(
    @CurrentUser() user: UserContext,
    @Body() request: ViewPhoneNotificationRequest,
  ): Promise<null> {
    await this.notificationService.handleViewPhoneNotification(user, request);
    return null;
  }


  @Post('test-create')
  async testCreateNotification(
    @Body() request: CreateNotificationRequest,
    @CurrentUser() user: UserContext,
  ): Promise<null> {

    Logger.log(`>>>User ${user.id} - ${user.email} - ${user.role} is creating a notification for user ${request.userId}`);

    await this.notificationService.createNotification(
      request.userId,
      request.message,
      request.type,
    );
    return null;
  }
}