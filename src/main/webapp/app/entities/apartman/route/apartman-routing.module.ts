import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApartmanComponent } from '../list/apartman.component';
import { ApartmanDetailComponent } from '../detail/apartman-detail.component';
import { ApartmanUpdateComponent } from '../update/apartman-update.component';
import { ApartmanRoutingResolveService } from './apartman-routing-resolve.service';

const apartmanRoute: Routes = [
  {
    path: '',
    component: ApartmanComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApartmanDetailComponent,
    resolve: {
      apartman: ApartmanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApartmanUpdateComponent,
    resolve: {
      apartman: ApartmanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApartmanUpdateComponent,
    resolve: {
      apartman: ApartmanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(apartmanRoute)],
  exports: [RouterModule],
})
export class ApartmanRoutingModule {}
