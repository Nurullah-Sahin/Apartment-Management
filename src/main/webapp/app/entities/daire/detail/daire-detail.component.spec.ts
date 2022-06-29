import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DaireDetailComponent } from './daire-detail.component';

describe('Daire Management Detail Component', () => {
  let comp: DaireDetailComponent;
  let fixture: ComponentFixture<DaireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DaireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ daire: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(DaireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DaireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load daire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.daire).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
