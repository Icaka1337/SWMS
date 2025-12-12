import dayjs from 'dayjs/esm';

import { ISystemLog, NewSystemLog } from './system-log.model';

export const sampleWithRequiredData: ISystemLog = {
  id: 19280,
  timestamp: dayjs('2025-11-25T08:17'),
};

export const sampleWithPartialData: ISystemLog = {
  id: 20859,
  username: 'now reword',
  action: 'pocket-watch given',
  entityName: 'yippee',
  timestamp: dayjs('2025-11-25T10:39'),
};

export const sampleWithFullData: ISystemLog = {
  id: 6872,
  username: 'behind after',
  action: 'corny ugh ravel',
  entityName: 'yum',
  details: 'silt',
  timestamp: dayjs('2025-11-25T12:21'),
};

export const sampleWithNewData: NewSystemLog = {
  timestamp: dayjs('2025-11-25T08:36'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
