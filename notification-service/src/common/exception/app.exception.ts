import { HttpException } from '@nestjs/common';
import { ErrorCodeDetail } from './error-code.enum';

export class AppException extends HttpException {
  constructor(public readonly errorCode: ErrorCodeDetail) {
    super(errorCode.message, errorCode.status);
  }
}