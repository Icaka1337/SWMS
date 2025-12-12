export interface ISupplier {
  id: number;
  name?: string | null;
  contactInfo?: string | null;
  reliabilityScore?: number | null;
  email?: string | null;
  phone?: string | null;
}

export type NewSupplier = Omit<ISupplier, 'id'> & { id: null };
