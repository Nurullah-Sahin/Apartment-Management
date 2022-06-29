import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DaireService } from '../service/daire.service';

import { DaireComponent } from './daire.component';

describe('Daire Management Component', () => {
  let comp: DaireComponent;
  let fixture: ComponentFixture<DaireComponent>;
  let service: DaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DaireComponent],
    })
      .overrideTemplate(DaireComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DaireComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DaireService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.daires?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
