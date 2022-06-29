import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApartman } from '../apartman.model';
import { ApartmanService } from '../service/apartman.service';
import { ApartmanDeleteDialogComponent } from '../delete/apartman-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-apartman',
  templateUrl: './apartman.component.html',
})
export class ApartmanComponent implements OnInit {
  apartmen?: IApartman[];
  isLoading = false;

  constructor(protected apartmanService: ApartmanService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.apartmanService.query().subscribe({
      next: (res: HttpResponse<IApartman[]>) => {
        this.isLoading = false;
        this.apartmen = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IApartman): string {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(apartman: IApartman): void {
    const modalRef = this.modalService.open(ApartmanDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.apartman = apartman;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
