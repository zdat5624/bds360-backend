import { Request } from 'express';
import { UserContext } from '../dto/user-context.dto';

export interface AuthenticatedRequest extends Request {
    user?: UserContext;
}