import { ICountry } from 'app/shared/model/country.model';
import { IFacebookValues } from 'app/shared/model/facebook-values.model';
import { IMostOfContent } from 'app/shared/model/most-of-content.model';
import { FbType } from 'app/shared/model/enumerations/fb-type.model';

export interface IFacebook {
  id?: number;
  name?: string | null;
  url?: string | null;
  idOnFb?: string | null;
  type?: FbType | null;
  country?: ICountry | null;
  facebookValues?: IFacebookValues[] | null;
  mostOfContents?: IMostOfContent[] | null;
}

export const defaultValue: Readonly<IFacebook> = {};
