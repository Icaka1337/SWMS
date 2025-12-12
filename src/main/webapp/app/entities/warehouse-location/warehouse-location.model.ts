export interface IWarehouseLocation {
  id: number;
  name?: string | null;
  section?: string | null;
  capacity?: number | null;
  description?: string | null;
}

export type NewWarehouseLocation = Omit<IWarehouseLocation, 'id'> & { id: null };
