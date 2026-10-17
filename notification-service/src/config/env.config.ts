import { config } from 'dotenv';
import { z } from 'zod';

config({ path: ['.env', '../.env'] });

export const envSchema = z.object({
    NODE_ENV: z.enum(['development', 'production', 'test']).default('development'),

    // 🌟 Khớp chính xác tên biến trong file .env
    NOTIFICATION_PORT: z.coerce.number().int().positive().default(3002),

    DB_HOST: z.string().min(1).default('localhost'),
    DB_PORT: z.coerce.number().int().positive().default(3306),
    DB_USERNAME: z.string().min(1).default('root'),
    DB_PASSWORD: z.string().min(1).default('root'),
    DB_DATABASE: z.string().min(1).default('bds360_notification_db'),

    JWT_SECRET: z.string().min(1, 'JWT_SECRET không được để trống'),

    MAIL_USERNAME: z.string().email('MAIL_USERNAME phải là email hợp lệ'),
    MAIL_PASSWORD: z.string().min(1, 'MAIL_PASSWORD không được để trống'),
});

export type EnvConfig = z.infer<typeof envSchema>;

function validateEnv(): EnvConfig {
    // 🌟 Đưa thẳng process.env cho Zod
    const parsed = envSchema.safeParse(process.env);

    if (!parsed.success) {
        console.error('❌ Lỗi cấu hình biến môi trường Notification Service (.env):', parsed.error.format());
        process.exit(1);
    }

    return parsed.data;
}

export const envConfig = validateEnv();