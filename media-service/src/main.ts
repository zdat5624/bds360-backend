import { BadRequestException, ValidationPipe } from '@nestjs/common';
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { GlobalExceptionFilter } from './common/filter/global-exception.filter';
import { TransformInterceptor } from './common/interceptor/transform.interceptor';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.enableCors();

  // Cấu hình ValidationPipe tự chuẩn hóa mảng lỗi ngay từ đầu
  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      transform: true,
      exceptionFactory: (errors) => {
        const formattedErrors = errors.map((err) => ({
          field: err.property,
          message: Object.values(err.constraints || {})[0] || 'Dữ liệu không hợp lệ',
        }));
        return new BadRequestException(formattedErrors);
      },
    }),
  );

  app.setGlobalPrefix('api/v1');
  
  // Đăng ký Bắt lỗi toàn cục
  app.useGlobalFilters(new GlobalExceptionFilter());
  
  // Đăng ký Gói Response thành công toàn cục
  app.useGlobalInterceptors(new TransformInterceptor());

  const PORT = process.env.PORT || 3001;
  await app.listen(PORT);
  console.log(`🚀 Service is running on: http://localhost:${PORT}`);
}
void bootstrap();