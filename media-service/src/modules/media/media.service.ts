import { Injectable, Logger, OnModuleInit } from '@nestjs/common';
import { v2 as cloudinary, UploadApiErrorResponse, UploadApiResponse } from 'cloudinary';
import 'multer';
import * as path from 'path';
import { Readable } from 'stream';
import { AppException } from '../../common/exception/app.exception';
import { ErrorCode } from '../../common/exception/error-code.enum';

@Injectable()
export class MediaService implements OnModuleInit {
  private readonly logger = new Logger(MediaService.name);

  private readonly ALLOWED_IMAGE_TYPES = [
    'image/jpeg', 'image/png', 'image/x-png', 'image/gif', 'image/webp',
    'image/avif', 'image/apng', 'image/svg+xml', 'image/heic', 
    'image/heic-sequence', 'image/heif', 'image/heif-sequence',
    'image/x-icon', 'image/vnd.microsoft.icon', 'image/ico', 'image/icon',
    'image/bmp', 'image/x-ms-bmp', 'image/tiff', 'image/vnd.adobe.photoshop',
  ];

  private readonly ALLOWED_IMAGE_EXTENSIONS = [
    '.jpg', '.jpeg', '.jpe', '.jfif', '.png', '.gif', '.webp',
    '.avif', '.apng', '.svg', '.heic', '.heif', '.ico', '.bmp',
    '.tif', '.tiff', '.psd',
  ];

  private readonly MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

  onModuleInit() {
    cloudinary.config({
      cloud_name: process.env.CLOUDINARY_CLOUD_NAME || 'dwdehgtoq',
      api_key: process.env.CLOUDINARY_API_KEY || '399594327697784',
      api_secret: process.env.CLOUDINARY_API_SECRET || 'Hb-lviqId-8UdQxUBp12RDOBLiY',
    });
    this.logger.log('>>> Đã khởi tạo Cloudinary SDK thành công!');
  }

  public async storeImage(file: Express.Multer.File): Promise<string> {
    this.validateFileSize(file);

    if (!this.isImageFile(file)) {
      // Lỗi 16002 - Sai định dạng ảnh
      throw new AppException(ErrorCode.FILE_FORMAT_INVALID);
    }

    return this.executeUpload(file, 'bds360/images');
  }

  public async storeFile(file: Express.Multer.File): Promise<string> {
    this.validateFileSize(file);
    return this.executeUpload(file, 'bds360/files');
  }

  private validateFileSize(file: Express.Multer.File) {
    if (file.size > this.MAX_FILE_SIZE) {
      // Lỗi 16001 - Vượt giới hạn kích thước
      throw new AppException(ErrorCode.FILE_TOO_LARGE);
    }
  }

  private isImageFile(file: Express.Multer.File): boolean {
    if (file.mimetype && this.ALLOWED_IMAGE_TYPES.includes(file.mimetype.toLowerCase())) {
      return true;
    }

    if (file.originalname) {
      const ext = path.extname(file.originalname).toLowerCase();
      return this.ALLOWED_IMAGE_EXTENSIONS.includes(ext);
    }

    return false;
  }

  private executeUpload(file: Express.Multer.File, folderName: string): Promise<string> {
    return new Promise((resolve, reject) => {
      const uploadStream = cloudinary.uploader.upload_stream(
        {
          folder: folderName,
          resource_type: 'auto',
        },
        (error: UploadApiErrorResponse | undefined, result: UploadApiResponse | undefined) => {
          if (error || !result) {
            this.logger.error(`>>> Lỗi upload file lên Cloudinary: ${error?.message || 'Lỗi không xác định'}`);
            // Lỗi 16003 - Cloudinary từ chối hoặc lỗi mạng nội bộ
            return reject(new AppException(ErrorCode.FILE_UPLOAD_FAILED));
          }
          resolve(result.secure_url);
        },
      );

      Readable.from(file.buffer).pipe(uploadStream);
    });
  }
}