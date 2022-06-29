import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMesaj } from '../mesaj.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-mesaj-detail',
  templateUrl: './mesaj-detail.component.html',
})
export class MesajDetailComponent implements OnInit {
  mesaj: IMesaj | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mesaj }) => {
      this.mesaj = mesaj;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
