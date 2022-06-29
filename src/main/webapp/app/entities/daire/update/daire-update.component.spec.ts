import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DaireService } from '../service/daire.service';
import { IDaire, Daire } from '../daire.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IApartman } from 'app/entities/apartman/apartman.model';
import { ApartmanService } from 'app/entities/apartman/service/apartman.service';

import { DaireUpdateComponent } from './daire-update.component';

describe('Daire Management Update Component', () => {
  let comp: DaireUpdateComponent;
  let fixture: ComponentFixture<DaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let daireService: DaireService;
  let userService: UserService;
  let apartmanService: ApartmanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DaireUpdateComponent],
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
      .overrideTemplate(DaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    daireService = TestBed.inject(DaireService);
    userService = TestBed.inject(UserService);
    apartmanService = TestBed.inject(ApartmanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const daire: IDaire = { id: 'CBA' };
      const oturanBilgi: IUser = { id: 'e74c046f-4cca-4833-a206-c563f4d3f289' };
      daire.oturanBilgi = oturanBilgi;

      const userCollection: IUser[] = [{ id: '6278eec4-5d77-4c74-bdeb-97477c101a9b' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [oturanBilgi];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ daire });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Apartman query and add missing value', () => {
      const daire: IDaire = { id: 'CBA' };
      const apartmanid: IApartman = { id: 'a47bdc04-50be-4887-9ffa-7cf85e96948c' };
      daire.apartmanid = apartmanid;

      const apartmanCollection: IApartman[] = [{ id: 'e6fbd419-4b66-4cc3-b1f8-03600bab6c74' }];
      jest.spyOn(apartmanService, 'query').mockReturnValue(of(new HttpResponse({ body: apartmanCollection })));
      const additionalApartmen = [apartmanid];
      const expectedCollection: IApartman[] = [...additionalApartmen, ...apartmanCollection];
      jest.spyOn(apartmanService, 'addApartmanToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ daire });
      comp.ngOnInit();

      expect(apartmanService.query).toHaveBeenCalled();
      expect(apartmanService.addApartmanToCollectionIfMissing).toHaveBeenCalledWith(apartmanCollection, ...additionalApartmen);
      expect(comp.apartmenSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const daire: IDaire = { id: 'CBA' };
      const oturanBilgi: IUser = { id: '95d7a68f-7c29-4e05-8728-a39ecac0cc0d' };
      daire.oturanBilgi = oturanBilgi;
      const apartmanid: IApartman = { id: '9c21653f-0507-4a1d-924c-9de84cf4e0ae' };
      daire.apartmanid = apartmanid;

      activatedRoute.data = of({ daire });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(daire));
      expect(comp.usersSharedCollection).toContain(oturanBilgi);
      expect(comp.apartmenSharedCollection).toContain(apartmanid);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Daire>>();
      const daire = { id: 'ABC' };
      jest.spyOn(daireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ daire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: daire }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(daireService.update).toHaveBeenCalledWith(daire);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Daire>>();
      const daire = new Daire();
      jest.spyOn(daireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ daire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: daire }));
      saveSubject.complete();

      // THEN
      expect(daireService.create).toHaveBeenCalledWith(daire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Daire>>();
      const daire = { id: 'ABC' };
      jest.spyOn(daireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ daire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(daireService.update).toHaveBeenCalledWith(daire);
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

    describe('trackApartmanById', () => {
      it('Should return tracked Apartman primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackApartmanById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
