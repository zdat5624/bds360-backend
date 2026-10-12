import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { MediaModule } from './modules/media/media.module';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true, // Giúp biến .env dùng được ở mọi nơi trong project
    }),
    MediaModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}