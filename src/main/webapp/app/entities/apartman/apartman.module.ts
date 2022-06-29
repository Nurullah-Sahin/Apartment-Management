import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApartmanComponent } from './list/apartman.component';
import { ApartmanDetailComponent } from './detail/apartman-detail.component';
import { ApartmanUpdateComponent } from './update/apartman-update.component';
import { ApartmanDeleteDialogComponent } from './delete/apartman-delete-dialog.component';
import { ApartmanRoutingModule } from './route/apartman-routing.module';

@NgModule({
  imports: [SharedModule, ApartmanRoutingModule],
  declarations: [ApartmanComponent, ApartmanDetailComponent, ApartmanUpdateComponent, ApartmanDeleteDialogComponent],
  entryComponents: [ApartmanDeleteDialogComponent],
})
export class ApartmanModule {}
