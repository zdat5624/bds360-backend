import { MailerService } from '@nestjs-modules/mailer';
import { Injectable, Logger } from '@nestjs/common';
import * as crypto from 'crypto';

@Injectable()
export class EmailService {
    private readonly logger = new Logger(EmailService.name);

    constructor(private readonly mailerService: MailerService) { }

    /**
     * Gửi email quên mật khẩu
     */
    async sendForgotPasswordEmail(to: string, username: string, resetCode: string): Promise<void> {
        try {
            await this.mailerService.sendMail({
                to: to,
                subject: 'Mã Xác Nhận Đặt Lại Mật Khẩu - BDS360',
                template: './forgot-password',
                context: {
                    username: username,
                    code: resetCode,
                },
            });
            this.logger.log(`>>> Đã gửi email reset password thành công tới: ${to}`);
        } catch (error) {
            // 🌟 FIX: Kiểm tra kiểu an toàn (Type narrowing) thay vì dùng any
            const errMessage = error instanceof Error ? error.message : String(error);
            this.logger.error(`>>> LỖI GỬI EMAIL tới ${to}: ${errMessage}`);
        }
    }

    /**
     * Gửi email nạp tiền thành công
     */
    async sendDepositSuccessEmail(to: string, username: string, amount: number, transactionTime: string): Promise<void> {
        try {
            // Format số tiền kiểu Việt Nam (vd: 50,000)
            const formattedAmount = new Intl.NumberFormat('vi-VN').format(amount);

            await this.mailerService.sendMail({
                to: to,
                subject: 'Nạp Tiền Thành Công - BDS360',
                template: './deposit-success',
                context: {
                    username: username,
                    amount: formattedAmount,
                    transactionTime: transactionTime,
                },
            });
            this.logger.log(`>>> Đã gửi email deposit success thành công tới: ${to}`);
        } catch (error) {
            // 🌟 FIX: Kiểm tra kiểu an toàn (Type narrowing) thay vì dùng any
            const errMessage = error instanceof Error ? error.message : String(error);
            this.logger.error(`>>> LỖI GỬI EMAIL tới ${to}: ${errMessage}`);
        }
    }

    /**
     * Sinh mã random 5 chữ số
     */
    generateResetCode(): string {
        const code = crypto.randomInt(10000, 99999);
        return code.toString();
    }
}