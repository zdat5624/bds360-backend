import { MailerModule } from '@nestjs-modules/mailer';
import { HandlebarsAdapter } from '@nestjs-modules/mailer/dist/adapters/handlebars.adapter';
import { Module } from '@nestjs/common';
import { join } from 'path';
import { envConfig } from '../../config/env.config';
import { EmailController } from './email.controller';
import { EmailService } from './email.service';

@Module({
    imports: [
        MailerModule.forRoot({
            transport: {
                host: 'smtp.gmail.com',
                port: 587,
                secure: false, // true for 465, false for other ports
                auth: {
                    user: envConfig.MAIL_USERNAME,
                    pass: envConfig.MAIL_PASSWORD,
                },
            },
            defaults: {
                from: `"BDS360 No-Reply" <${envConfig.MAIL_USERNAME}>`,
            },
            template: {
                // Chỉ định thư mục chứa file .hbs
                dir: join(__dirname, 'templates'),
                adapter: new HandlebarsAdapter(),
                options: {
                    strict: true,
                },
            },
        }),
    ],
    controllers: [EmailController],
    providers: [EmailService],
    exports: [EmailService], // Export để các module khác (Identity, Payment) có thể xài
})
export class EmailModule { }