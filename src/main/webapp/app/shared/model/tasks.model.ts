import { ITaskValues } from 'app/shared/model/task-values.model';
import { ISchedulerTaskDevice } from 'app/shared/model/scheduler-task-device.model';
import { Social } from 'app/shared/model/enumerations/social.model';
import { TaskType } from 'app/shared/model/enumerations/task-type.model';

export interface ITasks {
  id?: number;
  taskName?: string | null;
  description?: string | null;
  source?: string | null;
  price?: number | null;
  social?: Social | null;
  type?: TaskType | null;
  taskValues?: ITaskValues[] | null;
  schedulerTaskDevices?: ISchedulerTaskDevice[] | null;
}

export const defaultValue: Readonly<ITasks> = {};
