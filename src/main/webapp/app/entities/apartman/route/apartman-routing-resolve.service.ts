import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApartman, Apartman } from '../apartman.model';
import { ApartmanService } from '../service/apartman.service';

@Injectable({ providedIn: 'root' })
export class ApartmanRoutingResolveService implements Resolve<IApartman> {
  constructor(protected service: ApartmanService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApartman> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((apartman: HttpResponse<Apartman>) => {
          if (apartman.body) {
            return of(apartman.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Apartman());
  }
}
