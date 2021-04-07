import { ITasks } from 'app/shared/model/tasks.model';
import { ITaskFields } from 'app/shared/model/task-fields.model';

export interface ITaskValues {
  id?: number;
  value?: string | null;
  tasks?: ITasks | null;
  taskFields?: ITaskFields | null;
}

export const defaultValue: Readonly<ITaskValues> = {};
