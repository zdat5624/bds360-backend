import { config } from 'dotenv';
import { z } from 'zod';

// Lùi ra 1 cấp để tìm file .env tổng
config({ path: ['.env', '../.env'] });

export const envSchema = z.object({
    NODE_ENV: z.enum(['development', 'production', 'test']).default('development'),

    // 🌟 Khớp chính xác tên biến trong file .env
    MEDIA_PORT: z.coerce.number().int().positive().default(3001),

    JWT_SECRET: z.string().min(1, 'JWT_SECRET không được để trống'),

    CLOUDINARY_CLOUD_NAME: z.string().min(1, 'CLOUDINARY_CLOUD_NAME không được để trống'),
    CLOUDINARY_API_KEY: z.string().min(1, 'CLOUDINARY_API_KEY không được để trống'),
    CLOUDINARY_API_SECRET: z.string().min(1, 'CLOUDINARY_API_SECRET không được để trống'),
});

export type EnvConfig = z.infer<typeof envSchema>;

function validateEnv(): EnvConfig {
    // 🌟 Đưa thẳng process.env cho Zod kiểm tra, không cần rawEnv nữa
    const parsed = envSchema.safeParse(process.env);

    if (!parsed.success) {
        console.error('❌ Lỗi cấu hình biến môi trường Media Service (.env):', parsed.error.format());
        process.exit(1);
    }

    return parsed.data;
}

export const envConfig = validateEnv();