import { applyDecorators, SetMetadata } from '@nestjs/common';
import { Role } from '../../common/constant/role.enum';

export const REQUIRE_LOGIN_KEY = 'requireLogin';
export const ROLES_KEY = 'roles';

// Tương đương @RequireLogin bên Spring Boot
export const RequireLogin = () => SetMetadata(REQUIRE_LOGIN_KEY, true);

// Tương đương @IsAdmin bên Spring Boot
export const IsAdmin = () =>
  applyDecorators(
    SetMetadata(REQUIRE_LOGIN_KEY, true),
    SetMetadata(ROLES_KEY, [Role.ADMIN]),
  );

// Tương đương @IsAdminOrModerator bên Spring Boot
export const IsAdminOrModerator = () =>
  applyDecorators(
    SetMetadata(REQUIRE_LOGIN_KEY, true),
    SetMetadata(ROLES_KEY, [Role.ADMIN, Role.MODERATOR]),
  );