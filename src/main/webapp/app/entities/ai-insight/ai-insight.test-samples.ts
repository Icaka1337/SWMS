import dayjs from 'dayjs/esm';

import { IAIInsight, NewAIInsight } from './ai-insight.model';

export const sampleWithRequiredData: IAIInsight = {
  id: 14409,
  type: 'uh-huh than an',
  message: 'mockingly yowza',
  generatedAt: dayjs('2025-11-24T23:56'),
};

export const sampleWithPartialData: IAIInsight = {
  id: 26275,
  type: 'gray',
  message: 'shameful',
  confidence: 29001.98,
  generatedAt: dayjs('2025-11-25T14:57'),
};

export const sampleWithFullData: IAIInsight = {
  id: 968,
  type: 'apud dial',
  message: 'inferior',
  confidence: 7721.28,
  generatedAt: dayjs('2025-11-24T22:49'),
};

export const sampleWithNewData: NewAIInsight = {
  type: 'fully',
  message: 'drat',
  generatedAt: dayjs('2025-11-24T22:40'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
