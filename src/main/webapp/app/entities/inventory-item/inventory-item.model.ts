import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';
import { IWarehouseLocation } from 'app/entities/warehouse-location/warehouse-location.model';

export interface IInventoryItem {
  id: number;
  quantity?: number | null;
  lastUpdated?: dayjs.Dayjs | null;
  product?: Pick<IProduct, 'id' | 'name'> | null;
  location?: Pick<IWarehouseLocation, 'id' | 'name'> | null;
}

export type NewInventoryItem = Omit<IInventoryItem, 'id'> & { id: null };
