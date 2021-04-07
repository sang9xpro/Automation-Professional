import { IHistory } from 'app/shared/model/history.model';
import { IHistoryFields } from 'app/shared/model/history-fields.model';

export interface IHistoryValues {
  id?: number;
  value?: string | null;
  history?: IHistory | null;
  historyFields?: IHistoryFields | null;
}

export const defaultValue: Readonly<IHistoryValues> = {};
