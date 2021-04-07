import { ISchedulerTaskDeviceValues } from 'app/shared/model/scheduler-task-device-values.model';

export interface ISchedulerTaskDeviceFields {
  id?: number;
  fieldName?: string | null;
  schedulerTaskDeviceValues?: ISchedulerTaskDeviceValues[] | null;
}

export const defaultValue: Readonly<ISchedulerTaskDeviceFields> = {};
