import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAIInsight } from '../ai-insight.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../ai-insight.test-samples';

import { AIInsightService, RestAIInsight } from './ai-insight.service';

const requireRestSample: RestAIInsight = {
  ...sampleWithRequiredData,
  generatedAt: sampleWithRequiredData.generatedAt?.toJSON(),
};

describe('AIInsight Service', () => {
  let service: AIInsightService;
  let httpMock: HttpTestingController;
  let expectedResult: IAIInsight | IAIInsight[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AIInsightService);
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

    it('should create a AIInsight', () => {
      const aIInsight = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(aIInsight).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AIInsight', () => {
      const aIInsight = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(aIInsight).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AIInsight', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AIInsight', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AIInsight', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAIInsightToCollectionIfMissing', () => {
      it('should add a AIInsight to an empty array', () => {
        const aIInsight: IAIInsight = sampleWithRequiredData;
        expectedResult = service.addAIInsightToCollectionIfMissing([], aIInsight);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aIInsight);
      });

      it('should not add a AIInsight to an array that contains it', () => {
        const aIInsight: IAIInsight = sampleWithRequiredData;
        const aIInsightCollection: IAIInsight[] = [
          {
            ...aIInsight,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAIInsightToCollectionIfMissing(aIInsightCollection, aIInsight);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AIInsight to an array that doesn't contain it", () => {
        const aIInsight: IAIInsight = sampleWithRequiredData;
        const aIInsightCollection: IAIInsight[] = [sampleWithPartialData];
        expectedResult = service.addAIInsightToCollectionIfMissing(aIInsightCollection, aIInsight);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aIInsight);
      });

      it('should add only unique AIInsight to an array', () => {
        const aIInsightArray: IAIInsight[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const aIInsightCollection: IAIInsight[] = [sampleWithRequiredData];
        expectedResult = service.addAIInsightToCollectionIfMissing(aIInsightCollection, ...aIInsightArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aIInsight: IAIInsight = sampleWithRequiredData;
        const aIInsight2: IAIInsight = sampleWithPartialData;
        expectedResult = service.addAIInsightToCollectionIfMissing([], aIInsight, aIInsight2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aIInsight);
        expect(expectedResult).toContain(aIInsight2);
      });

      it('should accept null and undefined values', () => {
        const aIInsight: IAIInsight = sampleWithRequiredData;
        expectedResult = service.addAIInsightToCollectionIfMissing([], null, aIInsight, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aIInsight);
      });

      it('should return initial array if no AIInsight is added', () => {
        const aIInsightCollection: IAIInsight[] = [sampleWithRequiredData];
        expectedResult = service.addAIInsightToCollectionIfMissing(aIInsightCollection, undefined, null);
        expect(expectedResult).toEqual(aIInsightCollection);
      });
    });

    describe('compareAIInsight', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAIInsight(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 695 };
        const entity2 = null;

        const compareResult1 = service.compareAIInsight(entity1, entity2);
        const compareResult2 = service.compareAIInsight(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 695 };
        const entity2 = { id: 28792 };

        const compareResult1 = service.compareAIInsight(entity1, entity2);
        const compareResult2 = service.compareAIInsight(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 695 };
        const entity2 = { id: 695 };

        const compareResult1 = service.compareAIInsight(entity1, entity2);
        const compareResult2 = service.compareAIInsight(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
