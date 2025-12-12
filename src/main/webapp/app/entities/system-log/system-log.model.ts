import dayjs from 'dayjs/esm';

export interface ISystemLog {
  id: number;
  username?: string | null;
  action?: string | null;
  entityName?: string | null;
  details?: string | null;
  timestamp?: dayjs.Dayjs | null;
}

export type NewSystemLog = Omit<ISystemLog, 'id'> & { id: null };
