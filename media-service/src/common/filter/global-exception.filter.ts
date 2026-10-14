import {
  ArgumentsHost,
  Catch,
  ExceptionFilter,
  HttpException,
  HttpStatus,
  Logger,
} from '@nestjs/common';
import { Response } from 'express';
import { AppException } from '../exception/app.exception';
import { ErrorCode } from '../exception/error-code.enum';

interface FieldError {
  field: string;
  message: string;
}

@Catch()
export class GlobalExceptionFilter implements ExceptionFilter {
  private readonly logger = new Logger(GlobalExceptionFilter.name);

  catch(exception: unknown, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();

    // 1. Luồng Nghiệp vụ (Dev chủ động throw AppException)
    if (exception instanceof AppException) {
      const err = exception.errorCode;
      return response.status(err.status).json({
        code: err.code,
        message: err.message,
      });
    }

    // 2. Luồng Framework / Hệ thống (NestJS Exceptions)
    if (exception instanceof HttpException) {
      const status = exception.getStatus();
      const res = exception.getResponse();

      if (typeof res === 'object' && res !== null) {
        const resObj = res as Record<string, unknown>;

        // Hứng mảng lỗi ValidationPipe
        if (Array.isArray(resObj.message) && resObj.message.length > 0) {
          const firstItem = resObj.message[0] as Record<string, unknown>;

          if (firstItem && typeof firstItem === 'object' && 'field' in firstItem) {
            return response.status(HttpStatus.BAD_REQUEST).json({
              code: ErrorCode.VALIDATION_ERROR.code,
              message: ErrorCode.VALIDATION_ERROR.message,
              validationErrors: resObj.message as FieldError[],
            });
          }
        }
      }

      // Xử lý message lỗi chung
      let message = exception.message;
      if (typeof res === 'object' && res !== null && 'message' in res) {
        const resObj = res as Record<string, unknown>;
        if (typeof resObj.message === 'string') {
          message = resObj.message;
        } else if (Array.isArray(resObj.message) && typeof resObj.message[0] === 'string') {
          message = resObj.message[0];
        }
      } else if (typeof res === 'string') {
        message = res;
      }

      // 🌟 Dùng trực tiếp mã số để trị dứt điểm ESLint
      let code = ErrorCode.INVALID_PARAMETER.code;
      if (status === 401) {
        code = ErrorCode.UNAUTHORIZED.code;
      } else if (status === 403) {
        code = ErrorCode.FORBIDDEN.code;
      } else if (status === 404) {
        code = ErrorCode.API_NOT_FOUND.code;
      } else if (status === 413) { 
        code = ErrorCode.FILE_TOO_LARGE.code; // 🌟 Bổ sung dòng này
      } else if (status === 415) {
        code = ErrorCode.FILE_FORMAT_INVALID.code; // 🌟 Bổ sung cho an toàn
      } else if (status >= 500) {
        code = ErrorCode.INTERNAL_ERROR.code;
      }

      return response.status(status).json({
        code,
        message,
      });
    }

    // 3. Luồng Crash / Unhandled Error (Lỗi 500)
    if (exception instanceof Error) {
      this.logger.error(`Unhandled Exception: ${exception.message}`, exception.stack);
    } else {
      this.logger.error('Unhandled Unknown Exception:', exception);
    }

    return response.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
      code: ErrorCode.INTERNAL_ERROR.code,
      message: ErrorCode.INTERNAL_ERROR.message,
    });
  }
}