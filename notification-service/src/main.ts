import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { GlobalExceptionFilter } from './common/filter/global-exception.filter';
import { TransformInterceptor } from './common/interceptor/transform.interceptor';
import { CustomValidationPipe } from './common/pipe/custom-validation.pipe';
import { envConfig } from './config/env.config';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);


  app.setGlobalPrefix('api/v1');

  app.useGlobalPipes(new CustomValidationPipe());
  app.useGlobalInterceptors(new TransformInterceptor());
  app.useGlobalFilters(new GlobalExceptionFilter());

  const PORT = envConfig.NOTIFICATION_PORT;
  await app.listen(PORT);
  console.log(`🚀 Notification Service is running on port: ${PORT}`);
}

void bootstrap();


// RabbitMQ