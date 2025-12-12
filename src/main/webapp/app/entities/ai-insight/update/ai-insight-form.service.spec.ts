import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ai-insight.test-samples';

import { AIInsightFormService } from './ai-insight-form.service';

describe('AIInsight Form Service', () => {
  let service: AIInsightFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AIInsightFormService);
  });

  describe('Service methods', () => {
    describe('createAIInsightFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAIInsightFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            message: expect.any(Object),
            confidence: expect.any(Object),
            generatedAt: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });

      it('passing IAIInsight should create a new form with FormGroup', () => {
        const formGroup = service.createAIInsightFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            message: expect.any(Object),
            confidence: expect.any(Object),
            generatedAt: expect.any(Object),
            product: expect.any(Object),
          }),
        );
      });
    });

    describe('getAIInsight', () => {
      it('should return NewAIInsight for default AIInsight initial value', () => {
        const formGroup = service.createAIInsightFormGroup(sampleWithNewData);

        const aIInsight = service.getAIInsight(formGroup) as any;

        expect(aIInsight).toMatchObject(sampleWithNewData);
      });

      it('should return NewAIInsight for empty AIInsight initial value', () => {
        const formGroup = service.createAIInsightFormGroup();

        const aIInsight = service.getAIInsight(formGroup) as any;

        expect(aIInsight).toMatchObject({});
      });

      it('should return IAIInsight', () => {
        const formGroup = service.createAIInsightFormGroup(sampleWithRequiredData);

        const aIInsight = service.getAIInsight(formGroup) as any;

        expect(aIInsight).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAIInsight should not enable id FormControl', () => {
        const formGroup = service.createAIInsightFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAIInsight should disable id FormControl', () => {
        const formGroup = service.createAIInsightFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
