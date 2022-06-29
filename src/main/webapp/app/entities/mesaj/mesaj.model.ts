import { IUser } from 'app/entities/user/user.model';

export interface IMesaj {
  id?: string;
  mesajIcerik?: string | null;
  aktif?: boolean | null;
  sahib?: IUser | null;
}

export class Mesaj implements IMesaj {
  constructor(public id?: string, public mesajIcerik?: string | null, public aktif?: boolean | null, public sahib?: IUser | null) {
    this.aktif = this.aktif ?? false;
  }
}

export function getMesajIdentifier(mesaj: IMesaj): string | undefined {
  return mesaj.id;
}
