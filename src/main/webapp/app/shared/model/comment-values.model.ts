import { IComment } from 'app/shared/model/comment.model';
import { ICommentFields } from 'app/shared/model/comment-fields.model';

export interface ICommentValues {
  id?: number;
  value?: string | null;
  comment?: IComment | null;
  commentFields?: ICommentFields | null;
}

export const defaultValue: Readonly<ICommentValues> = {};
