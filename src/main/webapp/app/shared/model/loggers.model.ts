import dayjs from 'dayjs';
import { ILoggersValues } from 'app/shared/model/loggers-values.model';
import { IDevices } from 'app/shared/model/devices.model';

export interface ILoggers {
  id?: number;
  status?: string | null;
  logDetailContentType?: string | null;
  logDetail?: string | null;
  lastUpdate?: string | null;
  loggersValues?: ILoggersValues[] | null;
  devices?: IDevices | null;
}

export const defaultValue: Readonly<ILoggers> = {};
