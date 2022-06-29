import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DaireComponent } from '../list/daire.component';
import { DaireDetailComponent } from '../detail/daire-detail.component';
import { DaireUpdateComponent } from '../update/daire-update.component';
import { DaireRoutingResolveService } from './daire-routing-resolve.service';

const daireRoute: Routes = [
  {
    path: '',
    component: DaireComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DaireDetailComponent,
    resolve: {
      daire: DaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DaireUpdateComponent,
    resolve: {
      daire: DaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DaireUpdateComponent,
    resolve: {
      daire: DaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(daireRoute)],
  exports: [RouterModule],
})
export class DaireRoutingModule {}
