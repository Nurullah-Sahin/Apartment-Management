import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApartman, getApartmanIdentifier } from '../apartman.model';

export type EntityResponseType = HttpResponse<IApartman>;
export type EntityArrayResponseType = HttpResponse<IApartman[]>;

@Injectable({ providedIn: 'root' })
export class ApartmanService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apartmen');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(apartman: IApartman): Observable<EntityResponseType> {
    return this.http.post<IApartman>(this.resourceUrl, apartman, { observe: 'response' });
  }

  update(apartman: IApartman): Observable<EntityResponseType> {
    return this.http.put<IApartman>(`${this.resourceUrl}/${getApartmanIdentifier(apartman) as string}`, apartman, { observe: 'response' });
  }

  partialUpdate(apartman: IApartman): Observable<EntityResponseType> {
    return this.http.patch<IApartman>(`${this.resourceUrl}/${getApartmanIdentifier(apartman) as string}`, apartman, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IApartman>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApartman[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApartmanToCollectionIfMissing(apartmanCollection: IApartman[], ...apartmenToCheck: (IApartman | null | undefined)[]): IApartman[] {
    const apartmen: IApartman[] = apartmenToCheck.filter(isPresent);
    if (apartmen.length > 0) {
      const apartmanCollectionIdentifiers = apartmanCollection.map(apartmanItem => getApartmanIdentifier(apartmanItem)!);
      const apartmenToAdd = apartmen.filter(apartmanItem => {
        const apartmanIdentifier = getApartmanIdentifier(apartmanItem);
        if (apartmanIdentifier == null || apartmanCollectionIdentifiers.includes(apartmanIdentifier)) {
          return false;
        }
        apartmanCollectionIdentifiers.push(apartmanIdentifier);
        return true;
      });
      return [...apartmenToAdd, ...apartmanCollection];
    }
    return apartmanCollection;
  }
}
