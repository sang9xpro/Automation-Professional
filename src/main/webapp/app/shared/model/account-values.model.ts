import { IAccounts } from 'app/shared/model/accounts.model';
import { IAccountFields } from 'app/shared/model/account-fields.model';

export interface IAccountValues {
  id?: number;
  value?: string | null;
  accounts?: IAccounts | null;
  accountFields?: IAccountFields | null;
}

export const defaultValue: Readonly<IAccountValues> = {};
