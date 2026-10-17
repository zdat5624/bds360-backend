import { Injectable } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';
import { Role } from '../../common/constant/role.enum';
import { envConfig } from '../../config/env.config'; // 🌟 Sửa đúng đường dẫn file
import { UserContext } from '../dto/user-context.dto';

export interface JwtPayload {
    sub?: string;
    userId?: number;
    userEmail?: string;
    userName?: string;
    userPhone?: string;
    roles?: string[];
    iat?: number;
    exp?: number;
}

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy, 'jwt') {
    // 🌟 Không cần inject ConfigService nữa, dùng trực tiếp envConfig
    constructor() {
        const secretBuffer = Buffer.from(envConfig.JWT_SECRET, 'base64');

        super({
            jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
            ignoreExpiration: false,
            secretOrKey: secretBuffer,
            algorithms: ['HS512'],
        });
    }

    /**
      * Passport tự động gọi hàm này sau khi verify chữ ký và hạn dùng của JWT thành công.
      * Kết quả trả về sẽ được gắn vào request.user
      */
    validate(jwt: JwtPayload): UserContext {
        const id = jwt.userId !== undefined ? Number(jwt.userId) : 0;
        const email = jwt.userEmail ?? jwt.sub ?? '';
        const name = jwt.userName ?? '';
        const phone = jwt.userPhone ?? '';

        let userRole: Role = Role.USER;
        if (Array.isArray(jwt.roles) && jwt.roles.length > 0) {
            let roleString = jwt.roles[0];
            if (roleString.startsWith('ROLE_')) {
                roleString = roleString.substring(5);
            }
            if (roleString in Role) {
                userRole = Role[roleString as keyof typeof Role];
            }
        }

        return new UserContext(id, email, name, phone, userRole);
    }
}

