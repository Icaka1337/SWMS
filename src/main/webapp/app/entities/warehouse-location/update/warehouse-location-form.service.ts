import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IWarehouseLocation, NewWarehouseLocation } from '../warehouse-location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWarehouseLocation for edit and NewWarehouseLocationFormGroupInput for create.
 */
type WarehouseLocationFormGroupInput = IWarehouseLocation | PartialWithRequiredKeyOf<NewWarehouseLocation>;

type WarehouseLocationFormDefaults = Pick<NewWarehouseLocation, 'id'>;

type WarehouseLocationFormGroupContent = {
  id: FormControl<IWarehouseLocation['id'] | NewWarehouseLocation['id']>;
  name: FormControl<IWarehouseLocation['name']>;
  section: FormControl<IWarehouseLocation['section']>;
  capacity: FormControl<IWarehouseLocation['capacity']>;
  description: FormControl<IWarehouseLocation['description']>;
};

export type WarehouseLocationFormGroup = FormGroup<WarehouseLocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WarehouseLocationFormService {
  createWarehouseLocationFormGroup(warehouseLocation: WarehouseLocationFormGroupInput = { id: null }): WarehouseLocationFormGroup {
    const warehouseLocationRawValue = {
      ...this.getFormDefaults(),
      ...warehouseLocation,
    };
    return new FormGroup<WarehouseLocationFormGroupContent>({
      id: new FormControl(
        { value: warehouseLocationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(warehouseLocationRawValue.name, {
        validators: [Validators.required],
      }),
      section: new FormControl(warehouseLocationRawValue.section),
      capacity: new FormControl(warehouseLocationRawValue.capacity),
      description: new FormControl(warehouseLocationRawValue.description),
    });
  }

  getWarehouseLocation(form: WarehouseLocationFormGroup): IWarehouseLocation | NewWarehouseLocation {
    return form.getRawValue() as IWarehouseLocation | NewWarehouseLocation;
  }

  resetForm(form: WarehouseLocationFormGroup, warehouseLocation: WarehouseLocationFormGroupInput): void {
    const warehouseLocationRawValue = { ...this.getFormDefaults(), ...warehouseLocation };
    form.reset(
      {
        ...warehouseLocationRawValue,
        id: { value: warehouseLocationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WarehouseLocationFormDefaults {
    return {
      id: null,
    };
  }
}
