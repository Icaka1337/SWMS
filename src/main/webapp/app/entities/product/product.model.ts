import dayjs from 'dayjs/esm';
import { ISupplier } from 'app/entities/supplier/supplier.model';

export interface IProduct {
  id: number;
  name?: string | null;
  sku?: string | null;
  description?: string | null;
  category?: string | null;
  unit?: string | null;
  barcode?: string | null;
  minStock?: number | null;
  maxStock?: number | null;
  reorderLevel?: number | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  supplier?: Pick<ISupplier, 'id' | 'name'> | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
