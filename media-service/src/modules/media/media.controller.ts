import {
    Controller,
    Post,
    UploadedFiles,
    UseInterceptors,
} from '@nestjs/common';
import { FilesInterceptor } from '@nestjs/platform-express';
import 'multer';

import { AppException } from 'src/common/exception/app.exception';
import { ErrorCode } from 'src/common/exception/error-code.enum';
import { MediaService } from './media.service';

@Controller('media')
export class MediaController {
  constructor(private readonly mediaService: MediaService) {}

  @Post('upload/file')
  @UseInterceptors(FilesInterceptor('files'))
  async uploadFiles(@UploadedFiles() files: Express.Multer.File[]) {
    this.validateEmptyFiles(files);

    const fileUrls = await Promise.all(
      files.map((file) => this.mediaService.storeFile(file)),
    );

    return fileUrls; // TransformInterceptor sẽ tự động bọc mảng này vào { data: [...] }
  }

  @Post('upload/image')
  @UseInterceptors(FilesInterceptor('files'))
  async uploadImages(@UploadedFiles() files: Express.Multer.File[]) {
    this.validateEmptyFiles(files);

    const fileUrls = await Promise.all(
      files.map((file) => this.mediaService.storeImage(file)),
    );

    return fileUrls; // TransformInterceptor sẽ lo phần bọc lại
  }

  private validateEmptyFiles(files: Express.Multer.File[]) {
    if (!files || files.length === 0 || !files[0]) {
      // Dùng AppException và mã ErrorCode chuẩn của hệ thống
      throw new AppException(ErrorCode.INVALID_PARAMETER);
    }
  }
}