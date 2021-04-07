import { ISchedulers } from 'app/shared/model/schedulers.model';
import { ISchedulerFields } from 'app/shared/model/scheduler-fields.model';

export interface ISchedulerValue {
  id?: number;
  value?: string | null;
  schedulers?: ISchedulers | null;
  schedulerFields?: ISchedulerFields | null;
}

export const defaultValue: Readonly<ISchedulerValue> = {};
