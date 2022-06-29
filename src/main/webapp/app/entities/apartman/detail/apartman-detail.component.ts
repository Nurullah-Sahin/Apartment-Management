import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApartman } from '../apartman.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-apartman-detail',
  templateUrl: './apartman-detail.component.html',
})
export class ApartmanDetailComponent implements OnInit {
  apartman: IApartman | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apartman }) => {
      this.apartman = apartman;
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
