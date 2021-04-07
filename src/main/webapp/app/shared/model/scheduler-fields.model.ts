import { ISchedulerValue } from 'app/shared/model/scheduler-value.model';

export interface ISchedulerFields {
  id?: number;
  fieldName?: string | null;
  schedulerValues?: ISchedulerValue[] | null;
}

export const defaultValue: Readonly<ISchedulerFields> = {};
