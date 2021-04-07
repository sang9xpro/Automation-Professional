import { IDeviceValues } from 'app/shared/model/device-values.model';

export interface IDevicesFields {
  id?: number;
  fieldsName?: string | null;
  deviceValues?: IDeviceValues[] | null;
}

export const defaultValue: Readonly<IDevicesFields> = {};
