import dayjs from 'dayjs';
import { ISchedulerTaskDeviceValues } from 'app/shared/model/scheduler-task-device-values.model';
import { ISchedulers } from 'app/shared/model/schedulers.model';
import { ITasks } from 'app/shared/model/tasks.model';
import { IDevices } from 'app/shared/model/devices.model';
import { SchedulerStatus } from 'app/shared/model/enumerations/scheduler-status.model';

export interface ISchedulerTaskDevice {
  id?: number;
  startTime?: string | null;
  endTime?: string | null;
  countFrom?: number | null;
  countTo?: number | null;
  point?: number | null;
  lastUpdate?: string | null;
  owner?: string | null;
  status?: SchedulerStatus | null;
  schedulerTaskDeviceValues?: ISchedulerTaskDeviceValues[] | null;
  schedulers?: ISchedulers | null;
  tasks?: ITasks | null;
  devices?: IDevices[] | null;
}

export const defaultValue: Readonly<ISchedulerTaskDevice> = {};
