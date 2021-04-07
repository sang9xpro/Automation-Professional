import { IFacebookValues } from 'app/shared/model/facebook-values.model';

export interface IFacebookFields {
  id?: number;
  fieldName?: string | null;
  facebookValues?: IFacebookValues[] | null;
}

export const defaultValue: Readonly<IFacebookFields> = {};
