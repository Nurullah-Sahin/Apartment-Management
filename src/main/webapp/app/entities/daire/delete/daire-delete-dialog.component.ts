import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDaire } from '../daire.model';
import { DaireService } from '../service/daire.service';

@Component({
  templateUrl: './daire-delete-dialog.component.html',
})
export class DaireDeleteDialogComponent {
  daire?: IDaire;

  constructor(protected daireService: DaireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.daireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
