import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ApartmanService } from '../service/apartman.service';

import { ApartmanComponent } from './apartman.component';

describe('Apartman Management Component', () => {
  let comp: ApartmanComponent;
  let fixture: ComponentFixture<ApartmanComponent>;
  let service: ApartmanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ApartmanComponent],
    })
      .overrideTemplate(ApartmanComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApartmanComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ApartmanService);

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
    expect(comp.apartmen?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
