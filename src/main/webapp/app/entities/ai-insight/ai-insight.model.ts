import dayjs from 'dayjs/esm';
import { IProduct } from 'app/entities/product/product.model';

export interface IAIInsight {
  id: number;
  type?: string | null;
  message?: string | null;
  confidence?: number | null;
  generatedAt?: dayjs.Dayjs | null;
  product?: Pick<IProduct, 'id'> | null;
}

export type NewAIInsight = Omit<IAIInsight, 'id'> & { id: null };
