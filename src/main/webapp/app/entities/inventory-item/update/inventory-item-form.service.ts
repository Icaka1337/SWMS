import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInventoryItem, NewInventoryItem } from '../inventory-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInventoryItem for edit and NewInventoryItemFormGroupInput for create.
 */
type InventoryItemFormGroupInput = IInventoryItem | PartialWithRequiredKeyOf<NewInventoryItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInventoryItem | NewInventoryItem> = Omit<T, 'lastUpdated'> & {
  lastUpdated?: string | null;
};

type InventoryItemFormRawValue = FormValueOf<IInventoryItem>;

type NewInventoryItemFormRawValue = FormValueOf<NewInventoryItem>;

type InventoryItemFormDefaults = Pick<NewInventoryItem, 'id' | 'lastUpdated'>;

type InventoryItemFormGroupContent = {
  id: FormControl<InventoryItemFormRawValue['id'] | NewInventoryItem['id']>;
  quantity: FormControl<InventoryItemFormRawValue['quantity']>;
  lastUpdated: FormControl<InventoryItemFormRawValue['lastUpdated']>;
  product: FormControl<InventoryItemFormRawValue['product']>;
  location: FormControl<InventoryItemFormRawValue['location']>;
};

export type InventoryItemFormGroup = FormGroup<InventoryItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InventoryItemFormService {
  createInventoryItemFormGroup(inventoryItem: InventoryItemFormGroupInput = { id: null }): InventoryItemFormGroup {
    const inventoryItemRawValue = this.convertInventoryItemToInventoryItemRawValue({
      ...this.getFormDefaults(),
      ...inventoryItem,
    });
    return new FormGroup<InventoryItemFormGroupContent>({
      id: new FormControl(
        { value: inventoryItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantity: new FormControl(inventoryItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      lastUpdated: new FormControl(inventoryItemRawValue.lastUpdated),
      product: new FormControl(inventoryItemRawValue.product, {
        validators: [Validators.required],
      }),
      location: new FormControl(inventoryItemRawValue.location, {
        validators: [Validators.required],
      }),
    });
  }

  getInventoryItem(form: InventoryItemFormGroup): IInventoryItem | NewInventoryItem {
    return this.convertInventoryItemRawValueToInventoryItem(form.getRawValue() as InventoryItemFormRawValue | NewInventoryItemFormRawValue);
  }

  resetForm(form: InventoryItemFormGroup, inventoryItem: InventoryItemFormGroupInput): void {
    const inventoryItemRawValue = this.convertInventoryItemToInventoryItemRawValue({ ...this.getFormDefaults(), ...inventoryItem });
    form.reset(
      {
        ...inventoryItemRawValue,
        id: { value: inventoryItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InventoryItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastUpdated: currentTime,
    };
  }

  private convertInventoryItemRawValueToInventoryItem(
    rawInventoryItem: InventoryItemFormRawValue | NewInventoryItemFormRawValue,
  ): IInventoryItem | NewInventoryItem {
    return {
      ...rawInventoryItem,
      lastUpdated: dayjs(rawInventoryItem.lastUpdated, DATE_TIME_FORMAT),
    };
  }

  private convertInventoryItemToInventoryItemRawValue(
    inventoryItem: IInventoryItem | (Partial<NewInventoryItem> & InventoryItemFormDefaults),
  ): InventoryItemFormRawValue | PartialWithRequiredKeyOf<NewInventoryItemFormRawValue> {
    return {
      ...inventoryItem,
      lastUpdated: inventoryItem.lastUpdated ? inventoryItem.lastUpdated.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
