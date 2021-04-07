import { IAccountValues } from 'app/shared/model/account-values.model';

export interface IAccountFields {
  id?: number;
  fieldName?: string | null;
  accountValues?: IAccountValues[] | null;
}

export const defaultValue: Readonly<IAccountFields> = {};
