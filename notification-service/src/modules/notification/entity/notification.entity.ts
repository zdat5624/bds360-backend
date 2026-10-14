import {
  Column,
  CreateDateColumn,
  Entity,
  PrimaryGeneratedColumn,
} from 'typeorm';

export enum NotificationType {
  POST = 'POST',
  SYSTEM = 'SYSTEM',
  PAYMENT = 'PAYMENT',
}

@Entity('notifications')
export class Notification {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ type: 'mediumtext' })
  message!: string;

  @Column({ type: 'enum', enum: NotificationType })
  type!: NotificationType;

  @Column({ default: false })
  isRead!: boolean;

  @Column()
  userId!: number; // Soft-link tới Identity Service

  @CreateDateColumn({ type: 'timestamp' })
  createdAt!: Date;
}