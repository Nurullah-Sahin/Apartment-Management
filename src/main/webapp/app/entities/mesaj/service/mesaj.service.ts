import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMesaj, getMesajIdentifier } from '../mesaj.model';

export type EntityResponseType = HttpResponse<IMesaj>;
export type EntityArrayResponseType = HttpResponse<IMesaj[]>;

@Injectable({ providedIn: 'root' })
export class MesajService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mesajs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mesaj: IMesaj): Observable<EntityResponseType> {
    return this.http.post<IMesaj>(this.resourceUrl, mesaj, { observe: 'response' });
  }

  update(mesaj: IMesaj): Observable<EntityResponseType> {
    return this.http.put<IMesaj>(`${this.resourceUrl}/${getMesajIdentifier(mesaj) as string}`, mesaj, { observe: 'response' });
  }

  partialUpdate(mesaj: IMesaj): Observable<EntityResponseType> {
    return this.http.patch<IMesaj>(`${this.resourceUrl}/${getMesajIdentifier(mesaj) as string}`, mesaj, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IMesaj>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMesaj[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMesajToCollectionIfMissing(mesajCollection: IMesaj[], ...mesajsToCheck: (IMesaj | null | undefined)[]): IMesaj[] {
    const mesajs: IMesaj[] = mesajsToCheck.filter(isPresent);
    if (mesajs.length > 0) {
      const mesajCollectionIdentifiers = mesajCollection.map(mesajItem => getMesajIdentifier(mesajItem)!);
      const mesajsToAdd = mesajs.filter(mesajItem => {
        const mesajIdentifier = getMesajIdentifier(mesajItem);
        if (mesajIdentifier == null || mesajCollectionIdentifiers.includes(mesajIdentifier)) {
          return false;
        }
        mesajCollectionIdentifiers.push(mesajIdentifier);
        return true;
      });
      return [...mesajsToAdd, ...mesajCollection];
    }
    return mesajCollection;
  }
}
