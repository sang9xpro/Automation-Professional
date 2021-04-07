import { ISchedulerTaskDevice } from 'app/shared/model/scheduler-task-device.model';
import { ISchedulerTaskDeviceFields } from 'app/shared/model/scheduler-task-device-fields.model';

export interface ISchedulerTaskDeviceValues {
  id?: number;
  value?: string | null;
  schedulerTaskDevice?: ISchedulerTaskDevice | null;
  schedulerTaskDeviceFields?: ISchedulerTaskDeviceFields | null;
}

export const defaultValue: Readonly<ISchedulerTaskDeviceValues> = {};
