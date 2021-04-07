import dayjs from 'dayjs';
import { ISchedulerValue } from 'app/shared/model/scheduler-value.model';
import { ISchedulerTaskDevice } from 'app/shared/model/scheduler-task-device.model';
import { SchedulerStatus } from 'app/shared/model/enumerations/scheduler-status.model';

export interface ISchedulers {
  id?: number;
  url?: string | null;
  title?: string | null;
  otherSource?: string | null;
  count?: number | null;
  lastUpdate?: string | null;
  owner?: string | null;
  status?: SchedulerStatus | null;
  schedulerValues?: ISchedulerValue[] | null;
  schedulerTaskDevices?: ISchedulerTaskDevice[] | null;
}

export const defaultValue: Readonly<ISchedulers> = {};
