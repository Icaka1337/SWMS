import dayjs from 'dayjs/esm';

import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 11737,
  name: 'hm sleepily',
  sku: 'yippee whoever',
  minStock: 16147,
  maxStock: 2897,
  reorderLevel: 30231,
};

export const sampleWithPartialData: IProduct = {
  id: 15347,
  name: 'silently honorable',
  sku: 'fooey ha divine',
  category: 'quickly',
  minStock: 2565,
  maxStock: 14792,
  reorderLevel: 32067,
};

export const sampleWithFullData: IProduct = {
  id: 4403,
  name: 'cafe',
  sku: 'along amongst',
  description: 'hm ugh',
  category: 'fatally',
  unit: 'headline incidentally hence',
  barcode: 'since',
  minStock: 24363,
  maxStock: 8811,
  reorderLevel: 8436,
  createdAt: dayjs('2025-11-25T12:09'),
  updatedAt: dayjs('2025-11-25T11:35'),
};

export const sampleWithNewData: NewProduct = {
  name: 'anti inject why',
  sku: 'anenst narrow apropos',
  minStock: 7532,
  maxStock: 30545,
  reorderLevel: 22971,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
