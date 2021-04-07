import { ICommentValues } from 'app/shared/model/comment-values.model';
import { IMostOfContent } from 'app/shared/model/most-of-content.model';
import { Social } from 'app/shared/model/enumerations/social.model';

export interface IComment {
  id?: number;
  content?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  social?: Social | null;
  owner?: string | null;
  commentValues?: ICommentValues[] | null;
  mostOfContent?: IMostOfContent | null;
}

export const defaultValue: Readonly<IComment> = {};
