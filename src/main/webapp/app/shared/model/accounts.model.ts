import dayjs from 'dayjs';
import { ICountry } from 'app/shared/model/country.model';
import { IAccountValues } from 'app/shared/model/account-values.model';
import { IDevices } from 'app/shared/model/devices.model';
import { Social } from 'app/shared/model/enumerations/social.model';

export interface IAccounts {
  id?: number;
  userName?: string | null;
  password?: string | null;
  type?: Social | null;
  urlLogin?: string | null;
  profileFirefox?: string | null;
  profileChrome?: string | null;
  lastUpdate?: string | null;
  owner?: string | null;
  actived?: number | null;
  country?: ICountry | null;
  accountValues?: IAccountValues[] | null;
  devices?: IDevices[] | null;
}

export const defaultValue: Readonly<IAccounts> = {};
