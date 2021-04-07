import dayjs from 'dayjs';
import { IMostOfContValues } from 'app/shared/model/most-of-cont-values.model';
import { IComment } from 'app/shared/model/comment.model';
import { IFacebook } from 'app/shared/model/facebook.model';

export interface IMostOfContent {
  id?: number;
  urlOriginal?: string | null;
  contentText?: string | null;
  postTime?: string | null;
  numberLike?: number | null;
  numberComment?: number | null;
  mostOfContValues?: IMostOfContValues[] | null;
  comments?: IComment[] | null;
  facebook?: IFacebook | null;
}

export const defaultValue: Readonly<IMostOfContent> = {};
