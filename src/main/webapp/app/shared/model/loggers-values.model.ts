import { ILoggers } from 'app/shared/model/loggers.model';
import { ILoggersFields } from 'app/shared/model/loggers-fields.model';

export interface ILoggersValues {
  id?: number;
  value?: string | null;
  loggers?: ILoggers | null;
  loggersFields?: ILoggersFields | null;
}

export const defaultValue: Readonly<ILoggersValues> = {};
