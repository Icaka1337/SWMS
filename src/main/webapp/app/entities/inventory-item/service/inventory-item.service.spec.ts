import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IInventoryItem } from '../inventory-item.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../inventory-item.test-samples';

import { InventoryItemService, RestInventoryItem } from './inventory-item.service';

const requireRestSample: RestInventoryItem = {
  ...sampleWithRequiredData,
  lastUpdated: sampleWithRequiredData.lastUpdated?.toJSON(),
};

describe('InventoryItem Service', () => {
  let service: InventoryItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IInventoryItem | IInventoryItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InventoryItemService);
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

    it('should create a InventoryItem', () => {
      const inventoryItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inventoryItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InventoryItem', () => {
      const inventoryItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inventoryItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InventoryItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InventoryItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InventoryItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInventoryItemToCollectionIfMissing', () => {
      it('should add a InventoryItem to an empty array', () => {
        const inventoryItem: IInventoryItem = sampleWithRequiredData;
        expectedResult = service.addInventoryItemToCollectionIfMissing([], inventoryItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inventoryItem);
      });

      it('should not add a InventoryItem to an array that contains it', () => {
        const inventoryItem: IInventoryItem = sampleWithRequiredData;
        const inventoryItemCollection: IInventoryItem[] = [
          {
            ...inventoryItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInventoryItemToCollectionIfMissing(inventoryItemCollection, inventoryItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InventoryItem to an array that doesn't contain it", () => {
        const inventoryItem: IInventoryItem = sampleWithRequiredData;
        const inventoryItemCollection: IInventoryItem[] = [sampleWithPartialData];
        expectedResult = service.addInventoryItemToCollectionIfMissing(inventoryItemCollection, inventoryItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventoryItem);
      });

      it('should add only unique InventoryItem to an array', () => {
        const inventoryItemArray: IInventoryItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inventoryItemCollection: IInventoryItem[] = [sampleWithRequiredData];
        expectedResult = service.addInventoryItemToCollectionIfMissing(inventoryItemCollection, ...inventoryItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inventoryItem: IInventoryItem = sampleWithRequiredData;
        const inventoryItem2: IInventoryItem = sampleWithPartialData;
        expectedResult = service.addInventoryItemToCollectionIfMissing([], inventoryItem, inventoryItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventoryItem);
        expect(expectedResult).toContain(inventoryItem2);
      });

      it('should accept null and undefined values', () => {
        const inventoryItem: IInventoryItem = sampleWithRequiredData;
        expectedResult = service.addInventoryItemToCollectionIfMissing([], null, inventoryItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inventoryItem);
      });

      it('should return initial array if no InventoryItem is added', () => {
        const inventoryItemCollection: IInventoryItem[] = [sampleWithRequiredData];
        expectedResult = service.addInventoryItemToCollectionIfMissing(inventoryItemCollection, undefined, null);
        expect(expectedResult).toEqual(inventoryItemCollection);
      });
    });

    describe('compareInventoryItem', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInventoryItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7462 };
        const entity2 = null;

        const compareResult1 = service.compareInventoryItem(entity1, entity2);
        const compareResult2 = service.compareInventoryItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7462 };
        const entity2 = { id: 4332 };

        const compareResult1 = service.compareInventoryItem(entity1, entity2);
        const compareResult2 = service.compareInventoryItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7462 };
        const entity2 = { id: 7462 };

        const compareResult1 = service.compareInventoryItem(entity1, entity2);
        const compareResult2 = service.compareInventoryItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
