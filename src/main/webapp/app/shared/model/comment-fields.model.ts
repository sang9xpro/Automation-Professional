import { ICommentValues } from 'app/shared/model/comment-values.model';

export interface ICommentFields {
  id?: number;
  fieldName?: string | null;
  commentValues?: ICommentValues[] | null;
}

export const defaultValue: Readonly<ICommentFields> = {};
