import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true, // Giúp biến .env dùng được ở mọi nơi trong project
    }),
  ],
})
export class AppModule {}
