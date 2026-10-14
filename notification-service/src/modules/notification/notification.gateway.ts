import { Logger } from '@nestjs/common';
import {
  OnGatewayConnection,
  OnGatewayDisconnect,
  WebSocketGateway,
  WebSocketServer,
} from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';

// Endpoint /ws giống hệt Spring Boot cũ
@WebSocketGateway({ path: '/ws', cors: { origin: '*' } })
export class NotificationGateway implements OnGatewayConnection, OnGatewayDisconnect {
  @WebSocketServer()
  server!: Server; // 🌟 FIX: Thêm dấu "!" để tắt lỗi khởi tạo

  private readonly logger = new Logger(NotificationGateway.name);

  handleConnection(client: Socket) {
    // FE sẽ gửi userId lên qua query (vd: /ws?userId=1)
    const userId = client.handshake.query.userId as string | undefined;
    if (userId) {
      // 🌟 FIX: Hàm join có thể trả về Promise/void tùy bản socket.io, ta ép void để ESLint không kêu ca
      void client.join(userId);
      this.logger.log(`Client kết nối: ${client.id} - Bắt đầu nghe User: ${userId}`);
    }
  }

  handleDisconnect(client: Socket) {
    this.logger.log(`Client ngắt kết nối: ${client.id}`);
  }
}