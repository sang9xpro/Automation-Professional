import { IHistoryValues } from 'app/shared/model/history-values.model';

export interface IHistoryFields {
  id?: number;
  fieldName?: string | null;
  historyValues?: IHistoryValues[] | null;
}

export const defaultValue: Readonly<IHistoryFields> = {};
