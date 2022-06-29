import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMesaj } from '../mesaj.model';
import { MesajService } from '../service/mesaj.service';
import { MesajDeleteDialogComponent } from '../delete/mesaj-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-mesaj',
  templateUrl: './mesaj.component.html',
})
export class MesajComponent implements OnInit {
  mesajs?: IMesaj[];
  isLoading = false;

  constructor(protected mesajService: MesajService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.mesajService.query().subscribe({
      next: (res: HttpResponse<IMesaj[]>) => {
        this.isLoading = false;
        this.mesajs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMesaj): string {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(mesaj: IMesaj): void {
    const modalRef = this.modalService.open(MesajDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mesaj = mesaj;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
