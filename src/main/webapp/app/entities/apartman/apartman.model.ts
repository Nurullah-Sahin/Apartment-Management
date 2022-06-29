export interface IApartman {
  id?: string;
  ad?: string | null;
  katSayisi?: number | null;
  daireSayisi?: number | null;
  adres?: string | null;
  doluDaireSayisi?: number | null;
}

export class Apartman implements IApartman {
  constructor(
    public id?: string,
    public ad?: string | null,
    public katSayisi?: number | null,
    public daireSayisi?: number | null,
    public adres?: string | null,
    public doluDaireSayisi?: number | null
  ) {}
}

export function getApartmanIdentifier(apartman: IApartman): string | undefined {
  return apartman.id;
}
