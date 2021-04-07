import { ITaskValues } from 'app/shared/model/task-values.model';

export interface ITaskFields {
  id?: number;
  fieldName?: string | null;
  taskValues?: ITaskValues[] | null;
}

export const defaultValue: Readonly<ITaskFields> = {};
