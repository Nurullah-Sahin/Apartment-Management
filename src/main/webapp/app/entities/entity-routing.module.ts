import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'apartman',
        data: { pageTitle: 'apartApp.apartman.home.title' },
        loadChildren: () => import('./apartman/apartman.module').then(m => m.ApartmanModule),
      },
      {
        path: 'daire',
        data: { pageTitle: 'apartApp.daire.home.title' },
        loadChildren: () => import('./daire/daire.module').then(m => m.DaireModule),
      },
      {
        path: 'mesaj',
        data: { pageTitle: 'apartApp.mesaj.home.title' },
        loadChildren: () => import('./mesaj/mesaj.module').then(m => m.MesajModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
