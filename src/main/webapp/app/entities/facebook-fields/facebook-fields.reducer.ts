import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFacebookFields, defaultValue } from 'app/shared/model/facebook-fields.model';

export const ACTION_TYPES = {
  SEARCH_FACEBOOKFIELDS: 'facebookFields/SEARCH_FACEBOOKFIELDS',
  FETCH_FACEBOOKFIELDS_LIST: 'facebookFields/FETCH_FACEBOOKFIELDS_LIST',
  FETCH_FACEBOOKFIELDS: 'facebookFields/FETCH_FACEBOOKFIELDS',
  CREATE_FACEBOOKFIELDS: 'facebookFields/CREATE_FACEBOOKFIELDS',
  UPDATE_FACEBOOKFIELDS: 'facebookFields/UPDATE_FACEBOOKFIELDS',
  PARTIAL_UPDATE_FACEBOOKFIELDS: 'facebookFields/PARTIAL_UPDATE_FACEBOOKFIELDS',
  DELETE_FACEBOOKFIELDS: 'facebookFields/DELETE_FACEBOOKFIELDS',
  RESET: 'facebookFields/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFacebookFields>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FacebookFieldsState = Readonly<typeof initialState>;

// Reducer

export default (state: FacebookFieldsState = initialState, action): FacebookFieldsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_FACEBOOKFIELDS):
    case REQUEST(ACTION_TYPES.FETCH_FACEBOOKFIELDS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FACEBOOKFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FACEBOOKFIELDS):
    case REQUEST(ACTION_TYPES.UPDATE_FACEBOOKFIELDS):
    case REQUEST(ACTION_TYPES.DELETE_FACEBOOKFIELDS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_FACEBOOKFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_FACEBOOKFIELDS):
    case FAILURE(ACTION_TYPES.FETCH_FACEBOOKFIELDS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FACEBOOKFIELDS):
    case FAILURE(ACTION_TYPES.CREATE_FACEBOOKFIELDS):
    case FAILURE(ACTION_TYPES.UPDATE_FACEBOOKFIELDS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_FACEBOOKFIELDS):
    case FAILURE(ACTION_TYPES.DELETE_FACEBOOKFIELDS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_FACEBOOKFIELDS):
    case SUCCESS(ACTION_TYPES.FETCH_FACEBOOKFIELDS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACEBOOKFIELDS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FACEBOOKFIELDS):
    case SUCCESS(ACTION_TYPES.UPDATE_FACEBOOKFIELDS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_FACEBOOKFIELDS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FACEBOOKFIELDS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/facebook-fields';
const apiSearchUrl = 'api/_search/facebook-fields';

// Actions

export const getSearchEntities: ICrudSearchAction<IFacebookFields> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_FACEBOOKFIELDS,
  payload: axios.get<IFacebookFields>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IFacebookFields> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FACEBOOKFIELDS_LIST,
    payload: axios.get<IFacebookFields>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFacebookFields> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FACEBOOKFIELDS,
    payload: axios.get<IFacebookFields>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFacebookFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FACEBOOKFIELDS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFacebookFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FACEBOOKFIELDS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IFacebookFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_FACEBOOKFIELDS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFacebookFields> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FACEBOOKFIELDS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
