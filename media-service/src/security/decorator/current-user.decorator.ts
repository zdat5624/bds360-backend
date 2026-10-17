import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import { UserContext } from '../dto/user-context.dto';
import { AuthenticatedRequest } from '../interface/authenticated-request.interface';


export const CurrentUser = createParamDecorator(
  (_data: unknown, ctx: ExecutionContext): UserContext | null => {
    const request = ctx.switchToHttp().getRequest<AuthenticatedRequest>();
    return request.user ?? null;
  },
);