import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: 3793,
  type: 'dearly but aha',
  quantity: 24722,
  date: dayjs('2025-11-25T11:55'),
};

export const sampleWithPartialData: ITransaction = {
  id: 9396,
  type: 'low so meager',
  quantity: 16125,
  date: dayjs('2025-11-25T14:19'),
};

export const sampleWithFullData: ITransaction = {
  id: 20285,
  type: 'cloudy',
  quantity: 24229,
  date: dayjs('2025-11-25T02:56'),
  notes: 'boohoo',
};

export const sampleWithNewData: NewTransaction = {
  type: 'custom minty beneficial',
  quantity: 5642,
  date: dayjs('2025-11-25T15:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
