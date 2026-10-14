import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { GlobalExceptionFilter } from './common/filter/global-exception.filter';
import { TransformInterceptor } from './common/interceptor/transform.interceptor';
import { CustomValidationPipe } from './common/pipe/custom-validation.pipe';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  app.enableCors();

  // 1. Dùng Pipe Custom đã được đóng gói
  app.useGlobalPipes(new CustomValidationPipe());

  // 2. Chuẩn hóa Response & Exception Filter
  app.useGlobalInterceptors(new TransformInterceptor());
  app.useGlobalFilters(new GlobalExceptionFilter());

  const PORT = process.env.PORT || 3001;
  await app.listen(PORT);
  console.log(`🚀 Service is running on port: ${PORT}`);
}

void bootstrap();