import { IDevices } from 'app/shared/model/devices.model';
import { IAccounts } from 'app/shared/model/accounts.model';
import { IFacebook } from 'app/shared/model/facebook.model';

export interface ICountry {
  id?: number;
  name?: string;
  code?: string;
  devices?: IDevices | null;
  accounts?: IAccounts | null;
  facebook?: IFacebook | null;
}

export const defaultValue: Readonly<ICountry> = {};
