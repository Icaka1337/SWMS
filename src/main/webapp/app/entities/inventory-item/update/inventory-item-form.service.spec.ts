import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../inventory-item.test-samples';

import { InventoryItemFormService } from './inventory-item-form.service';

describe('InventoryItem Form Service', () => {
  let service: InventoryItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InventoryItemFormService);
  });

  describe('Service methods', () => {
    describe('createInventoryItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInventoryItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            lastUpdated: expect.any(Object),
            product: expect.any(Object),
            location: expect.any(Object),
          }),
        );
      });

      it('passing IInventoryItem should create a new form with FormGroup', () => {
        const formGroup = service.createInventoryItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantity: expect.any(Object),
            lastUpdated: expect.any(Object),
            product: expect.any(Object),
            location: expect.any(Object),
          }),
        );
      });
    });

    describe('getInventoryItem', () => {
      it('should return NewInventoryItem for default InventoryItem initial value', () => {
        const formGroup = service.createInventoryItemFormGroup(sampleWithNewData);

        const inventoryItem = service.getInventoryItem(formGroup) as any;

        expect(inventoryItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewInventoryItem for empty InventoryItem initial value', () => {
        const formGroup = service.createInventoryItemFormGroup();

        const inventoryItem = service.getInventoryItem(formGroup) as any;

        expect(inventoryItem).toMatchObject({});
      });

      it('should return IInventoryItem', () => {
        const formGroup = service.createInventoryItemFormGroup(sampleWithRequiredData);

        const inventoryItem = service.getInventoryItem(formGroup) as any;

        expect(inventoryItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInventoryItem should not enable id FormControl', () => {
        const formGroup = service.createInventoryItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInventoryItem should disable id FormControl', () => {
        const formGroup = service.createInventoryItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
