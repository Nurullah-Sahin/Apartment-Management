import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApartman } from '../apartman.model';
import { ApartmanService } from '../service/apartman.service';

@Component({
  templateUrl: './apartman-delete-dialog.component.html',
})
export class ApartmanDeleteDialogComponent {
  apartman?: IApartman;

  constructor(protected apartmanService: ApartmanService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.apartmanService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
