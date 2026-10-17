import { Body, Controller, Post } from '@nestjs/common';
import { EmailService } from './email.service';

@Controller('email')
export class EmailController {
    constructor(private readonly emailService: EmailService) { }

    @Post('test-forgot-password')
    async testForgotPassword(
        @Body('to') to: string,
        @Body('username') username: string,
    ) {
        // Tự động sinh mã 5 số
        const code = this.emailService.generateResetCode();

        await this.emailService.sendForgotPasswordEmail(to, username, code);
        return { message: 'Đã đưa yêu cầu gửi email quên mật khẩu vào hàng đợi', code };
    }

    @Post('test-deposit-success')
    async testDepositSuccess(
        @Body('to') to: string,
        @Body('username') username: string,
        @Body('amount') amount: number,
    ) {
        // Lấy thời gian hiện tại format theo chuẩn Việt Nam
        const transactionTime = new Date().toLocaleString('vi-VN', { timeZone: 'Asia/Ho_Chi_Minh' });

        await this.emailService.sendDepositSuccessEmail(to, username, amount, transactionTime);
        return { message: 'Đã đưa yêu cầu gửi email nạp tiền vào hàng đợi', transactionTime };
    }
}