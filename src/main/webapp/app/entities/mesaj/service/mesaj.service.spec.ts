import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMesaj, Mesaj } from '../mesaj.model';

import { MesajService } from './mesaj.service';

describe('Mesaj Service', () => {
  let service: MesajService;
  let httpMock: HttpTestingController;
  let elemDefault: IMesaj;
  let expectedResult: IMesaj | IMesaj[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MesajService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 'AAAAAAA',
      mesajIcerik: 'AAAAAAA',
      aktif: false,
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

    it('should create a Mesaj', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Mesaj()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mesaj', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          mesajIcerik: 'BBBBBB',
          aktif: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mesaj', () => {
      const patchObject = Object.assign(
        {
          aktif: true,
        },
        new Mesaj()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mesaj', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          mesajIcerik: 'BBBBBB',
          aktif: true,
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

    it('should delete a Mesaj', () => {
      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMesajToCollectionIfMissing', () => {
      it('should add a Mesaj to an empty array', () => {
        const mesaj: IMesaj = { id: 'ABC' };
        expectedResult = service.addMesajToCollectionIfMissing([], mesaj);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mesaj);
      });

      it('should not add a Mesaj to an array that contains it', () => {
        const mesaj: IMesaj = { id: 'ABC' };
        const mesajCollection: IMesaj[] = [
          {
            ...mesaj,
          },
          { id: 'CBA' },
        ];
        expectedResult = service.addMesajToCollectionIfMissing(mesajCollection, mesaj);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mesaj to an array that doesn't contain it", () => {
        const mesaj: IMesaj = { id: 'ABC' };
        const mesajCollection: IMesaj[] = [{ id: 'CBA' }];
        expectedResult = service.addMesajToCollectionIfMissing(mesajCollection, mesaj);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mesaj);
      });

      it('should add only unique Mesaj to an array', () => {
        const mesajArray: IMesaj[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'f539b9b2-a7bf-4cf7-af19-935694325438' }];
        const mesajCollection: IMesaj[] = [{ id: 'ABC' }];
        expectedResult = service.addMesajToCollectionIfMissing(mesajCollection, ...mesajArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mesaj: IMesaj = { id: 'ABC' };
        const mesaj2: IMesaj = { id: 'CBA' };
        expectedResult = service.addMesajToCollectionIfMissing([], mesaj, mesaj2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mesaj);
        expect(expectedResult).toContain(mesaj2);
      });

      it('should accept null and undefined values', () => {
        const mesaj: IMesaj = { id: 'ABC' };
        expectedResult = service.addMesajToCollectionIfMissing([], null, mesaj, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mesaj);
      });

      it('should return initial array if no Mesaj is added', () => {
        const mesajCollection: IMesaj[] = [{ id: 'ABC' }];
        expectedResult = service.addMesajToCollectionIfMissing(mesajCollection, undefined, null);
        expect(expectedResult).toEqual(mesajCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
