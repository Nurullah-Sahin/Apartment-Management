import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MesajComponent } from './list/mesaj.component';
import { MesajDetailComponent } from './detail/mesaj-detail.component';
import { MesajUpdateComponent } from './update/mesaj-update.component';
import { MesajDeleteDialogComponent } from './delete/mesaj-delete-dialog.component';
import { MesajRoutingModule } from './route/mesaj-routing.module';

@NgModule({
  imports: [SharedModule, MesajRoutingModule],
  declarations: [MesajComponent, MesajDetailComponent, MesajUpdateComponent, MesajDeleteDialogComponent],
  entryComponents: [MesajDeleteDialogComponent],
})
export class MesajModule {}
