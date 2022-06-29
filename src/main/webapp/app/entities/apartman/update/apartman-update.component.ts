import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IApartman, Apartman } from '../apartman.model';
import { ApartmanService } from '../service/apartman.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-apartman-update',
  templateUrl: './apartman-update.component.html',
})
export class ApartmanUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ad: [],
    katSayisi: [],
    daireSayisi: [],
    adres: [],
    doluDaireSayisi: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected apartmanService: ApartmanService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apartman }) => {
      this.updateForm(apartman);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('apartApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apartman = this.createFromForm();
    if (apartman.id !== undefined) {
      this.subscribeToSaveResponse(this.apartmanService.update(apartman));
    } else {
      this.subscribeToSaveResponse(this.apartmanService.create(apartman));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApartman>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(apartman: IApartman): void {
    this.editForm.patchValue({
      id: apartman.id,
      ad: apartman.ad,
      katSayisi: apartman.katSayisi,
      daireSayisi: apartman.daireSayisi,
      adres: apartman.adres,
      doluDaireSayisi: apartman.doluDaireSayisi,
    });
  }

  protected createFromForm(): IApartman {
    return {
      ...new Apartman(),
      id: this.editForm.get(['id'])!.value,
      ad: this.editForm.get(['ad'])!.value,
      katSayisi: this.editForm.get(['katSayisi'])!.value,
      daireSayisi: this.editForm.get(['daireSayisi'])!.value,
      adres: this.editForm.get(['adres'])!.value,
      doluDaireSayisi: this.editForm.get(['doluDaireSayisi'])!.value,
    };
  }
}
