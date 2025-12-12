import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3366,
  name: 'slake splurge',
};

export const sampleWithPartialData: ICustomer = {
  id: 21246,
  name: 'lively traditionalism yellow',
};

export const sampleWithFullData: ICustomer = {
  id: 4149,
  name: 'where known',
  email: 'Lydia.Wilkinson79@gmail.com',
  phone: '(532) 302-8126 x3334',
};

export const sampleWithNewData: NewCustomer = {
  name: 'thyme inasmuch',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
