import { ISupplier, NewSupplier } from './supplier.model';

export const sampleWithRequiredData: ISupplier = {
  id: 22717,
  name: 'menacing',
};

export const sampleWithPartialData: ISupplier = {
  id: 9114,
  name: 'papa',
  email: 'Gustave53@yahoo.com',
};

export const sampleWithFullData: ISupplier = {
  id: 3840,
  name: 'cheerfully near',
  contactInfo: 'providence ick scholarship',
  reliabilityScore: 7689.49,
  email: 'Camryn.Mills@hotmail.com',
  phone: '1-657-993-2132 x204',
};

export const sampleWithNewData: NewSupplier = {
  name: 'snack',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
