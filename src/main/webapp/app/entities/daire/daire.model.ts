import { IUser } from 'app/entities/user/user.model';
import { IApartman } from 'app/entities/apartman/apartman.model';

export interface IDaire {
  id?: string;
  no?: string | null;
  oturanBilgi?: IUser | null;
  apartmanid?: IApartman | null;
}

export class Daire implements IDaire {
  constructor(public id?: string, public no?: string | null, public oturanBilgi?: IUser | null, public apartmanid?: IApartman | null) {}
}

export function getDaireIdentifier(daire: IDaire): string | undefined {
  return daire.id;
}
