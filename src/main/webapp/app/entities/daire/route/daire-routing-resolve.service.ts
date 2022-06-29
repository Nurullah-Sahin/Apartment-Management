import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDaire, Daire } from '../daire.model';
import { DaireService } from '../service/daire.service';

@Injectable({ providedIn: 'root' })
export class DaireRoutingResolveService implements Resolve<IDaire> {
  constructor(protected service: DaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDaire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((daire: HttpResponse<Daire>) => {
          if (daire.body) {
            return of(daire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Daire());
  }
}
