import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDaire } from '../daire.model';

@Component({
  selector: 'jhi-daire-detail',
  templateUrl: './daire-detail.component.html',
})
export class DaireDetailComponent implements OnInit {
  daire: IDaire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ daire }) => {
      this.daire = daire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
