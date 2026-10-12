import {
    CallHandler,
    ExecutionContext,
    Injectable,
    NestInterceptor,
} from '@nestjs/common';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface ResponseFormat<T> {
  code: number;
  message: string;
  data: T;
}

@Injectable()
export class TransformInterceptor<T> implements NestInterceptor<T, ResponseFormat<T>> {
  intercept(context: ExecutionContext, next: CallHandler): Observable<ResponseFormat<T>> {
    return next.handle().pipe(
      // Ép kiểu (data: T) để tránh bị coi là 'any'
      map((data: T) => ({
        code: 10000,
        message: 'Thành công',
        data,
      })),
    );
  }
}