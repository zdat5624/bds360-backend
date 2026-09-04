import { ValidationPipe } from '@nestjs/common';
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // Bật CORS để cho phép các service khác hoặc Gateway gọi vào
  app.enableCors();

  // Kích hoạt Validation tự động cho các DTO
  app.useGlobalPipes(new ValidationPipe({ whitelist: true, transform: true }));

  // Đặt tiền tố chung cho API (tùy chọn, ví dụ: /api/v1)
  app.setGlobalPrefix('api/v1');

  const PORT = process.env.PORT || 3001;
  await app.listen(PORT);
  console.log(`🚀 Service is running on: http://localhost:${PORT}`);
}
void bootstrap();
