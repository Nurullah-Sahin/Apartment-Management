import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDaire, Daire } from '../daire.model';
import { DaireService } from '../service/daire.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IApartman } from 'app/entities/apartman/apartman.model';
import { ApartmanService } from 'app/entities/apartman/service/apartman.service';

@Component({
  selector: 'jhi-daire-update',
  templateUrl: './daire-update.component.html',
})
export class DaireUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  apartmenSharedCollection: IApartman[] = [];

  editForm = this.fb.group({
    id: [],
    no: [],
    oturanBilgi: [],
    apartmanid: [],
  });

  constructor(
    protected daireService: DaireService,
    protected userService: UserService,
    protected apartmanService: ApartmanService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ daire }) => {
      this.updateForm(daire);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const daire = this.createFromForm();
    if (daire.id !== undefined) {
      this.subscribeToSaveResponse(this.daireService.update(daire));
    } else {
      this.subscribeToSaveResponse(this.daireService.create(daire));
    }
  }

  trackUserById(_index: number, item: IUser): string {
    return item.id!;
  }

  trackApartmanById(_index: number, item: IApartman): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDaire>>): void {
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

  protected updateForm(daire: IDaire): void {
    this.editForm.patchValue({
      id: daire.id,
      no: daire.no,
      oturanBilgi: daire.oturanBilgi,
      apartmanid: daire.apartmanid,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, daire.oturanBilgi);
    this.apartmenSharedCollection = this.apartmanService.addApartmanToCollectionIfMissing(this.apartmenSharedCollection, daire.apartmanid);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('oturanBilgi')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.apartmanService
      .query()
      .pipe(map((res: HttpResponse<IApartman[]>) => res.body ?? []))
      .pipe(
        map((apartmen: IApartman[]) =>
          this.apartmanService.addApartmanToCollectionIfMissing(apartmen, this.editForm.get('apartmanid')!.value)
        )
      )
      .subscribe((apartmen: IApartman[]) => (this.apartmenSharedCollection = apartmen));
  }

  protected createFromForm(): IDaire {
    return {
      ...new Daire(),
      id: this.editForm.get(['id'])!.value,
      no: this.editForm.get(['no'])!.value,
      oturanBilgi: this.editForm.get(['oturanBilgi'])!.value,
      apartmanid: this.editForm.get(['apartmanid'])!.value,
    };
  }
}
