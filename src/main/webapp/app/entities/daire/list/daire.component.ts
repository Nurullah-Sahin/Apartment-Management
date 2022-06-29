import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDaire } from '../daire.model';
import { DaireService } from '../service/daire.service';
import { DaireDeleteDialogComponent } from '../delete/daire-delete-dialog.component';

@Component({
  selector: 'jhi-daire',
  templateUrl: './daire.component.html',
})
export class DaireComponent implements OnInit {
  daires?: IDaire[];
  isLoading = false;

  constructor(protected daireService: DaireService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.daireService.query().subscribe({
      next: (res: HttpResponse<IDaire[]>) => {
        this.isLoading = false;
        this.daires = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDaire): string {
    return item.id!;
  }

  delete(daire: IDaire): void {
    const modalRef = this.modalService.open(DaireDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.daire = daire;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
