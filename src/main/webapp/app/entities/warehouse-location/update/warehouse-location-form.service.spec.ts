import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../warehouse-location.test-samples';

import { WarehouseLocationFormService } from './warehouse-location-form.service';

describe('WarehouseLocation Form Service', () => {
  let service: WarehouseLocationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WarehouseLocationFormService);
  });

  describe('Service methods', () => {
    describe('createWarehouseLocationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWarehouseLocationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            section: expect.any(Object),
            capacity: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing IWarehouseLocation should create a new form with FormGroup', () => {
        const formGroup = service.createWarehouseLocationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            section: expect.any(Object),
            capacity: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getWarehouseLocation', () => {
      it('should return NewWarehouseLocation for default WarehouseLocation initial value', () => {
        const formGroup = service.createWarehouseLocationFormGroup(sampleWithNewData);

        const warehouseLocation = service.getWarehouseLocation(formGroup) as any;

        expect(warehouseLocation).toMatchObject(sampleWithNewData);
      });

      it('should return NewWarehouseLocation for empty WarehouseLocation initial value', () => {
        const formGroup = service.createWarehouseLocationFormGroup();

        const warehouseLocation = service.getWarehouseLocation(formGroup) as any;

        expect(warehouseLocation).toMatchObject({});
      });

      it('should return IWarehouseLocation', () => {
        const formGroup = service.createWarehouseLocationFormGroup(sampleWithRequiredData);

        const warehouseLocation = service.getWarehouseLocation(formGroup) as any;

        expect(warehouseLocation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWarehouseLocation should not enable id FormControl', () => {
        const formGroup = service.createWarehouseLocationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWarehouseLocation should disable id FormControl', () => {
        const formGroup = service.createWarehouseLocationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
