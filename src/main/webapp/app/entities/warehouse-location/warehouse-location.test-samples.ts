import { IWarehouseLocation, NewWarehouseLocation } from './warehouse-location.model';

export const sampleWithRequiredData: IWarehouseLocation = {
  id: 5947,
  name: 'impolite stigmatize',
};

export const sampleWithPartialData: IWarehouseLocation = {
  id: 23683,
  name: 'glittering since afore',
};

export const sampleWithFullData: IWarehouseLocation = {
  id: 3755,
  name: 'indeed outsource',
  section: 'always libel',
  capacity: 13428,
  description: 'milestone',
};

export const sampleWithNewData: NewWarehouseLocation = {
  name: 'from',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
