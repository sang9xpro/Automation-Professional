import dayjs from 'dayjs';
import { ICountry } from 'app/shared/model/country.model';
import { IDeviceValues } from 'app/shared/model/device-values.model';
import { ILoggers } from 'app/shared/model/loggers.model';
import { IAccounts } from 'app/shared/model/accounts.model';
import { ISchedulerTaskDevice } from 'app/shared/model/scheduler-task-device.model';
import { DeviceType } from 'app/shared/model/enumerations/device-type.model';
import { DeviceStatus } from 'app/shared/model/enumerations/device-status.model';

export interface IDevices {
  id?: number;
  ipAddress?: string | null;
  macAddress?: string | null;
  os?: string | null;
  deviceType?: DeviceType | null;
  status?: DeviceStatus | null;
  lastUpdate?: string | null;
  owner?: string | null;
  country?: ICountry | null;
  deviceValues?: IDeviceValues[] | null;
  loggers?: ILoggers[] | null;
  accounts?: IAccounts[] | null;
  schedulerTaskDevices?: ISchedulerTaskDevice[] | null;
}

export const defaultValue: Readonly<IDevices> = {};
