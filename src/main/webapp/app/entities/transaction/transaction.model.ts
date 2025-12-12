import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';
import { IWarehouseLocation } from 'app/entities/warehouse-location/warehouse-location.model';

export interface ITransaction {
  id: number;
  type?: string | null;
  quantity?: number | null;
  date?: dayjs.Dayjs | null;
  notes?: string | null;
  product?: Pick<IProduct, 'id'> | null;
  sourceLocation?: Pick<IWarehouseLocation, 'id'> | null;
  targetLocation?: Pick<IWarehouseLocation, 'id'> | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
