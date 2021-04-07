import { IMostOfContValues } from 'app/shared/model/most-of-cont-values.model';

export interface IMostOfContFields {
  id?: number;
  fieldName?: string | null;
  mostOfContValues?: IMostOfContValues[] | null;
}

export const defaultValue: Readonly<IMostOfContFields> = {};
