import { IDevices } from 'app/shared/model/devices.model';
import { IDevicesFields } from 'app/shared/model/devices-fields.model';

export interface IDeviceValues {
  id?: number;
  value?: string | null;
  devices?: IDevices | null;
  devicesFields?: IDevicesFields | null;
}

export const defaultValue: Readonly<IDeviceValues> = {};
