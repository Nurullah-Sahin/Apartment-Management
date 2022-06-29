import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ApartmanService } from '../service/apartman.service';
import { IApartman, Apartman } from '../apartman.model';

import { ApartmanUpdateComponent } from './apartman-update.component';

describe('Apartman Management Update Component', () => {
  let comp: ApartmanUpdateComponent;
  let fixture: ComponentFixture<ApartmanUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let apartmanService: ApartmanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ApartmanUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ApartmanUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ApartmanUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    apartmanService = TestBed.inject(ApartmanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const apartman: IApartman = { id: 'CBA' };

      activatedRoute.data = of({ apartman });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(apartman));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apartman>>();
      const apartman = { id: 'ABC' };
      jest.spyOn(apartmanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apartman });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apartman }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(apartmanService.update).toHaveBeenCalledWith(apartman);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apartman>>();
      const apartman = new Apartman();
      jest.spyOn(apartmanService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apartman });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: apartman }));
      saveSubject.complete();

      // THEN
      expect(apartmanService.create).toHaveBeenCalledWith(apartman);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Apartman>>();
      const apartman = { id: 'ABC' };
      jest.spyOn(apartmanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ apartman });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(apartmanService.update).toHaveBeenCalledWith(apartman);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
