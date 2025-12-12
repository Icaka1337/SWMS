import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IWarehouseLocation } from '../warehouse-location.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../warehouse-location.test-samples';

import { WarehouseLocationService } from './warehouse-location.service';

const requireRestSample: IWarehouseLocation = {
  ...sampleWithRequiredData,
};

describe('WarehouseLocation Service', () => {
  let service: WarehouseLocationService;
  let httpMock: HttpTestingController;
  let expectedResult: IWarehouseLocation | IWarehouseLocation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WarehouseLocationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a WarehouseLocation', () => {
      const warehouseLocation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(warehouseLocation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WarehouseLocation', () => {
      const warehouseLocation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(warehouseLocation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WarehouseLocation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WarehouseLocation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WarehouseLocation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWarehouseLocationToCollectionIfMissing', () => {
      it('should add a WarehouseLocation to an empty array', () => {
        const warehouseLocation: IWarehouseLocation = sampleWithRequiredData;
        expectedResult = service.addWarehouseLocationToCollectionIfMissing([], warehouseLocation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(warehouseLocation);
      });

      it('should not add a WarehouseLocation to an array that contains it', () => {
        const warehouseLocation: IWarehouseLocation = sampleWithRequiredData;
        const warehouseLocationCollection: IWarehouseLocation[] = [
          {
            ...warehouseLocation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWarehouseLocationToCollectionIfMissing(warehouseLocationCollection, warehouseLocation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WarehouseLocation to an array that doesn't contain it", () => {
        const warehouseLocation: IWarehouseLocation = sampleWithRequiredData;
        const warehouseLocationCollection: IWarehouseLocation[] = [sampleWithPartialData];
        expectedResult = service.addWarehouseLocationToCollectionIfMissing(warehouseLocationCollection, warehouseLocation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(warehouseLocation);
      });

      it('should add only unique WarehouseLocation to an array', () => {
        const warehouseLocationArray: IWarehouseLocation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const warehouseLocationCollection: IWarehouseLocation[] = [sampleWithRequiredData];
        expectedResult = service.addWarehouseLocationToCollectionIfMissing(warehouseLocationCollection, ...warehouseLocationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const warehouseLocation: IWarehouseLocation = sampleWithRequiredData;
        const warehouseLocation2: IWarehouseLocation = sampleWithPartialData;
        expectedResult = service.addWarehouseLocationToCollectionIfMissing([], warehouseLocation, warehouseLocation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(warehouseLocation);
        expect(expectedResult).toContain(warehouseLocation2);
      });

      it('should accept null and undefined values', () => {
        const warehouseLocation: IWarehouseLocation = sampleWithRequiredData;
        expectedResult = service.addWarehouseLocationToCollectionIfMissing([], null, warehouseLocation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(warehouseLocation);
      });

      it('should return initial array if no WarehouseLocation is added', () => {
        const warehouseLocationCollection: IWarehouseLocation[] = [sampleWithRequiredData];
        expectedResult = service.addWarehouseLocationToCollectionIfMissing(warehouseLocationCollection, undefined, null);
        expect(expectedResult).toEqual(warehouseLocationCollection);
      });
    });

    describe('compareWarehouseLocation', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWarehouseLocation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22623 };
        const entity2 = null;

        const compareResult1 = service.compareWarehouseLocation(entity1, entity2);
        const compareResult2 = service.compareWarehouseLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22623 };
        const entity2 = { id: 1483 };

        const compareResult1 = service.compareWarehouseLocation(entity1, entity2);
        const compareResult2 = service.compareWarehouseLocation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 22623 };
        const entity2 = { id: 22623 };

        const compareResult1 = service.compareWarehouseLocation(entity1, entity2);
        const compareResult2 = service.compareWarehouseLocation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
