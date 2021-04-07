import dayjs from 'dayjs';
import { IHistoryValues } from 'app/shared/model/history-values.model';

export interface IHistory {
  id?: number;
  url?: string | null;
  taskId?: number | null;
  deviceId?: number | null;
  accountId?: number | null;
  lastUpdate?: string | null;
  historyValues?: IHistoryValues[] | null;
}

export const defaultValue: Readonly<IHistory> = {};
