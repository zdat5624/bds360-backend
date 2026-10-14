import { BadRequestException, ValidationPipe, ValidationPipeOptions } from '@nestjs/common';

export class CustomValidationPipe extends ValidationPipe {
  constructor(options?: ValidationPipeOptions) {
    super({
      whitelist: true,
      transform: true,
      exceptionFactory: (errors) => {
        const formattedErrors = errors.map((err) => ({
          field: err.property,
          message: Object.values(err.constraints || {})[0] || 'Dữ liệu không hợp lệ',
        }));
        return new BadRequestException(formattedErrors);
      },
      ...options, // Cho phép ghi đè option từ bên ngoài nếu cần
    });
  }
}