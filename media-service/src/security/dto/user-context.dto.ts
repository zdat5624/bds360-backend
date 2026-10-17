import { Role } from "@/common/constant/role.enum";

export class UserContext {
  id: number;
  email: string;
  name: string;
  phone: string;
  role: Role;

  constructor(id: number, email: string, name: string, phone: string, role: Role) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.phone = phone;
    this.role = role;
  }
}