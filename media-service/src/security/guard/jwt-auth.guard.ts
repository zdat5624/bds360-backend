import { ExecutionContext, Injectable, Logger } from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { AuthGuard } from '@nestjs/passport';
import { Role } from '../../common/constant/role.enum';
import { AppException } from '../../common/exception/app.exception';
import { ErrorCode } from '../../common/exception/error-code.enum';
import { REQUIRE_LOGIN_KEY, ROLES_KEY } from '../decorator/auth.decorator';
import { UserContext } from '../dto/user-context.dto';



@Injectable()
export class JwtAuthGuard extends AuthGuard('jwt') {
  private readonly logger = new Logger(JwtAuthGuard.name);

  constructor(private readonly reflector: Reflector) {
    super();
  }


  async canActivate(context: ExecutionContext): Promise<boolean> {

    // Bỏ qua HTTP Guard đối với luồng WebSocket (/ws)
    if (context.getType() !== 'http') {
      return true;
    }

    // Kích hoạt Passport JwtStrategy chạy và sau đó gọi xuống hàm handleRequest bên dưới
    return (await super.canActivate(context)) as boolean;
  }

  handleRequest<TUser = UserContext | null>(
    err: unknown,
    user: UserContext | false | null,
    info: Error | undefined,
    context: ExecutionContext,
  ): TUser {
    const isRequireLogin = this.reflector.getAllAndOverride<boolean>(
      REQUIRE_LOGIN_KEY,
      [context.getHandler(), context.getClass()],
    );

    const requiredRoles = this.reflector.getAllAndOverride<Role[]>(ROLES_KEY, [
      context.getHandler(),
      context.getClass(),
    ]);

    const needAuth = isRequireLogin || (requiredRoles && requiredRoles.length > 0);
    const validUser = user || null;

    if (info) {
      this.logger.debug(`>>> JWT Info: ${info.message}`);
    }

    // 1. Kiểm tra đăng nhập (@RequireLogin) -> Tương đương CustomAuthenticationEntryPoint (401)
    if (err || !validUser) {
      if (needAuth) {
        throw new AppException(ErrorCode.UNAUTHORIZED);
      }
      // Nếu API public (không gắn @RequireLogin) thì vẫn cho qua và trả về null
      return null as TUser;
    }

    // 2. Kiểm tra phân quyền (@IsAdmin / @IsAdminOrModerator) -> Tương đương CustomAccessDeniedHandler (403)
    if (requiredRoles && requiredRoles.length > 0) {
      const hasRole = requiredRoles.includes(validUser.role);
      if (!hasRole) {
        throw new AppException(ErrorCode.FORBIDDEN);
      }
    }

    return validUser as TUser;
  }
}