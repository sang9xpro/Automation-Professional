import { ILoggersValues } from 'app/shared/model/loggers-values.model';

export interface ILoggersFields {
  id?: number;
  fieldName?: string | null;
  loggersValues?: ILoggersValues[] | null;
}

export const defaultValue: Readonly<ILoggersFields> = {};
