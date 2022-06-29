import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DaireComponent } from './list/daire.component';
import { DaireDetailComponent } from './detail/daire-detail.component';
import { DaireUpdateComponent } from './update/daire-update.component';
import { DaireDeleteDialogComponent } from './delete/daire-delete-dialog.component';
import { DaireRoutingModule } from './route/daire-routing.module';

@NgModule({
  imports: [SharedModule, DaireRoutingModule],
  declarations: [DaireComponent, DaireDetailComponent, DaireUpdateComponent, DaireDeleteDialogComponent],
  entryComponents: [DaireDeleteDialogComponent],
})
export class DaireModule {}
