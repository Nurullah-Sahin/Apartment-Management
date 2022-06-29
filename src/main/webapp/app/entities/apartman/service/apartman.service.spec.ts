import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApartman, Apartman } from '../apartman.model';

import { ApartmanService } from './apartman.service';

describe('Apartman Service', () => {
  let service: ApartmanService;
  let httpMock: HttpTestingController;
  let elemDefault: IApartman;
  let expectedResult: IApartman | IApartman[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApartmanService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      ad: 'AAAAAAA',
      katSayisi: 0,
      daireSayisi: 0,
      adres: 'AAAAAAA',
      doluDaireSayisi: 0,
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

    it('should create a Apartman', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Apartman()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Apartman', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          ad: 'BBBBBB',
          katSayisi: 1,
          daireSayisi: 1,
          adres: 'BBBBBB',
          doluDaireSayisi: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Apartman', () => {
      const patchObject = Object.assign({}, new Apartman());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Apartman', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          ad: 'BBBBBB',
          katSayisi: 1,
          daireSayisi: 1,
          adres: 'BBBBBB',
          doluDaireSayisi: 1,
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

    it('should delete a Apartman', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addApartmanToCollectionIfMissing', () => {
      it('should add a Apartman to an empty array', () => {
        const apartman: IApartman = { id: 'ABC' };
        expectedResult = service.addApartmanToCollectionIfMissing([], apartman);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apartman);
      });

      it('should not add a Apartman to an array that contains it', () => {
        const apartman: IApartman = { id: 'ABC' };
        const apartmanCollection: IApartman[] = [
          {
            ...apartman,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addApartmanToCollectionIfMissing(apartmanCollection, apartman);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Apartman to an array that doesn't contain it", () => {
        const apartman: IApartman = { id: 'ABC' };
        const apartmanCollection: IApartman[] = [{ id: 'CBA' }];
        expectedResult = service.addApartmanToCollectionIfMissing(apartmanCollection, apartman);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apartman);
      });

      it('should add only unique Apartman to an array', () => {
        const apartmanArray: IApartman[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: '702eed3c-33c2-4667-b4ee-f5a2ea5e0242' }];
        const apartmanCollection: IApartman[] = [{ id: 'ABC' }];
        expectedResult = service.addApartmanToCollectionIfMissing(apartmanCollection, ...apartmanArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const apartman: IApartman = { id: 'ABC' };
        const apartman2: IApartman = { id: 'CBA' };
        expectedResult = service.addApartmanToCollectionIfMissing([], apartman, apartman2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(apartman);
        expect(expectedResult).toContain(apartman2);
      });

      it('should accept null and undefined values', () => {
        const apartman: IApartman = { id: 'ABC' };
        expectedResult = service.addApartmanToCollectionIfMissing([], null, apartman, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(apartman);
      });

      it('should return initial array if no Apartman is added', () => {
        const apartmanCollection: IApartman[] = [{ id: 'ABC' }];
        expectedResult = service.addApartmanToCollectionIfMissing(apartmanCollection, undefined, null);
        expect(expectedResult).toEqual(apartmanCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
