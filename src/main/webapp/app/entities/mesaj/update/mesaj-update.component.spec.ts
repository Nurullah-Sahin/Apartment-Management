import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MesajService } from '../service/mesaj.service';
import { IMesaj, Mesaj } from '../mesaj.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { MesajUpdateComponent } from './mesaj-update.component';

describe('Mesaj Management Update Component', () => {
  let comp: MesajUpdateComponent;
  let fixture: ComponentFixture<MesajUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mesajService: MesajService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MesajUpdateComponent],
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
      .overrideTemplate(MesajUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MesajUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mesajService = TestBed.inject(MesajService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const mesaj: IMesaj = { id: 'CBA' };
      const sahib: IUser = { id: 'd06f7bf7-a326-41b2-a8e2-4f1ca177da47' };
      mesaj.sahib = sahib;

      const userCollection: IUser[] = [{ id: 'bbd96bce-6f2a-416b-a8bf-9c4871d777a3' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [sahib];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mesaj });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const mesaj: IMesaj = { id: 'CBA' };
      const sahib: IUser = { id: 'e0873bd3-c482-4a53-9d01-793470e64571' };
      mesaj.sahib = sahib;

      activatedRoute.data = of({ mesaj });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(mesaj));
      expect(comp.usersSharedCollection).toContain(sahib);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mesaj>>();
      const mesaj = { id: 'ABC' };
      jest.spyOn(mesajService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mesaj });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mesaj }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(mesajService.update).toHaveBeenCalledWith(mesaj);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mesaj>>();
      const mesaj = new Mesaj();
      jest.spyOn(mesajService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mesaj });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mesaj }));
      saveSubject.complete();

      // THEN
      expect(mesajService.create).toHaveBeenCalledWith(mesaj);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Mesaj>>();
      const mesaj = { id: 'ABC' };
      jest.spyOn(mesajService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mesaj });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mesajService.update).toHaveBeenCalledWith(mesaj);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
