import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDaire, getDaireIdentifier } from '../daire.model';

export type EntityResponseType = HttpResponse<IDaire>;
export type EntityArrayResponseType = HttpResponse<IDaire[]>;

@Injectable({ providedIn: 'root' })
export class DaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/daires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(daire: IDaire): Observable<EntityResponseType> {
    return this.http.post<IDaire>(this.resourceUrl, daire, { observe: 'response' });
  }

  update(daire: IDaire): Observable<EntityResponseType> {
    return this.http.put<IDaire>(`${this.resourceUrl}/${getDaireIdentifier(daire) as string}`, daire, { observe: 'response' });
  }

  partialUpdate(daire: IDaire): Observable<EntityResponseType> {
    return this.http.patch<IDaire>(`${this.resourceUrl}/${getDaireIdentifier(daire) as string}`, daire, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IDaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDaireToCollectionIfMissing(daireCollection: IDaire[], ...dairesToCheck: (IDaire | null | undefined)[]): IDaire[] {
    const daires: IDaire[] = dairesToCheck.filter(isPresent);
    if (daires.length > 0) {
      const daireCollectionIdentifiers = daireCollection.map(daireItem => getDaireIdentifier(daireItem)!);
      const dairesToAdd = daires.filter(daireItem => {
        const daireIdentifier = getDaireIdentifier(daireItem);
        if (daireIdentifier == null || daireCollectionIdentifiers.includes(daireIdentifier)) {
          return false;
        }
        daireCollectionIdentifiers.push(daireIdentifier);
        return true;
      });
      return [...dairesToAdd, ...daireCollection];
    }
    return daireCollection;
  }
}
