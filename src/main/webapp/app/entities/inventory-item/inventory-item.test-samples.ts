import dayjs from 'dayjs/esm';

import { IInventoryItem, NewInventoryItem } from './inventory-item.model';

export const sampleWithRequiredData: IInventoryItem = {
  id: 19345,
  quantity: 30136,
};

export const sampleWithPartialData: IInventoryItem = {
  id: 18484,
  quantity: 28009,
  lastUpdated: dayjs('2025-11-25T09:43'),
};

export const sampleWithFullData: IInventoryItem = {
  id: 20465,
  quantity: 16479,
  lastUpdated: dayjs('2025-11-25T08:22'),
};

export const sampleWithNewData: NewInventoryItem = {
  quantity: 3812,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
