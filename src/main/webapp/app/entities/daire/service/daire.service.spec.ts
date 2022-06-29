import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDaire, Daire } from '../daire.model';

import { DaireService } from './daire.service';

describe('Daire Service', () => {
  let service: DaireService;
  let httpMock: HttpTestingController;
  let elemDefault: IDaire;
  let expectedResult: IDaire | IDaire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DaireService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      no: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Daire', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Daire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Daire', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          no: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Daire', () => {
      const patchObject = Object.assign({}, new Daire());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Daire', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          no: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Daire', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDaireToCollectionIfMissing', () => {
      it('should add a Daire to an empty array', () => {
        const daire: IDaire = { id: 'ABC' };
        expectedResult = service.addDaireToCollectionIfMissing([], daire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(daire);
      });

      it('should not add a Daire to an array that contains it', () => {
        const daire: IDaire = { id: 'ABC' };
        const daireCollection: IDaire[] = [
          {
            ...daire,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addDaireToCollectionIfMissing(daireCollection, daire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Daire to an array that doesn't contain it", () => {
        const daire: IDaire = { id: 'ABC' };
        const daireCollection: IDaire[] = [{ id: 'CBA' }];
        expectedResult = service.addDaireToCollectionIfMissing(daireCollection, daire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(daire);
      });

      it('should add only unique Daire to an array', () => {
        const daireArray: IDaire[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'ca67d3c4-9a61-4cf1-b6cc-bcbce98c6f2d' }];
        const daireCollection: IDaire[] = [{ id: 'ABC' }];
        expectedResult = service.addDaireToCollectionIfMissing(daireCollection, ...daireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const daire: IDaire = { id: 'ABC' };
        const daire2: IDaire = { id: 'CBA' };
        expectedResult = service.addDaireToCollectionIfMissing([], daire, daire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(daire);
        expect(expectedResult).toContain(daire2);
      });

      it('should accept null and undefined values', () => {
        const daire: IDaire = { id: 'ABC' };
        expectedResult = service.addDaireToCollectionIfMissing([], null, daire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(daire);
      });

      it('should return initial array if no Daire is added', () => {
        const daireCollection: IDaire[] = [{ id: 'ABC' }];
        expectedResult = service.addDaireToCollectionIfMissing(daireCollection, undefined, null);
        expect(expectedResult).toEqual(daireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
