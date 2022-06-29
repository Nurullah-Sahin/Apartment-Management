import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MesajService } from '../service/mesaj.service';

import { MesajComponent } from './mesaj.component';

describe('Mesaj Management Component', () => {
  let comp: MesajComponent;
  let fixture: ComponentFixture<MesajComponent>;
  let service: MesajService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MesajComponent],
    })
      .overrideTemplate(MesajComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MesajComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MesajService);

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
    expect(comp.mesajs?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
