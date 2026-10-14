import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { GlobalExceptionFilter } from './common/filter/global-exception.filter';
import { TransformInterceptor } from './common/interceptor/transform.interceptor';
import { CustomValidationPipe } from './common/pipe/custom-validation.pipe';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.enableCors();

  // 🌟 FIX: Đặt Global Prefix cho toàn bộ ứng dụng giống Spring Boot
  app.setGlobalPrefix('api/v1');

  app.useGlobalPipes(new CustomValidationPipe());
  app.useGlobalInterceptors(new TransformInterceptor());
  app.useGlobalFilters(new GlobalExceptionFilter());

  const PORT = process.env.PORT || 3002;
  await app.listen(PORT);
  console.log(`🚀 Notification Service is running on port: ${PORT}`);
}

void bootstrap();