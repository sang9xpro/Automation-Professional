import { IFacebook } from 'app/shared/model/facebook.model';
import { IFacebookFields } from 'app/shared/model/facebook-fields.model';

export interface IFacebookValues {
  id?: number;
  value?: string | null;
  facebook?: IFacebook | null;
  facebookFields?: IFacebookFields | null;
}

export const defaultValue: Readonly<IFacebookValues> = {};
