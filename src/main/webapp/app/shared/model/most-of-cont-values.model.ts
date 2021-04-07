import { IMostOfContent } from 'app/shared/model/most-of-content.model';
import { IMostOfContFields } from 'app/shared/model/most-of-cont-fields.model';

export interface IMostOfContValues {
  id?: number;
  value?: string | null;
  mostOfContent?: IMostOfContent | null;
  mostOfContFields?: IMostOfContFields | null;
}

export const defaultValue: Readonly<IMostOfContValues> = {};
