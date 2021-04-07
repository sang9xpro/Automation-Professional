import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFacebookValues, defaultValue } from 'app/shared/model/facebook-values.model';

export const ACTION_TYPES = {
  SEARCH_FACEBOOKVALUES: 'facebookValues/SEARCH_FACEBOOKVALUES',
  FETCH_FACEBOOKVALUES_LIST: 'facebookValues/FETCH_FACEBOOKVALUES_LIST',
  FETCH_FACEBOOKVALUES: 'facebookValues/FETCH_FACEBOOKVALUES',
  CREATE_FACEBOOKVALUES: 'facebookValues/CREATE_FACEBOOKVALUES',
  UPDATE_FACEBOOKVALUES: 'facebookValues/UPDATE_FACEBOOKVALUES',
  PARTIAL_UPDATE_FACEBOOKVALUES: 'facebookValues/PARTIAL_UPDATE_FACEBOOKVALUES',
  DELETE_FACEBOOKVALUES: 'facebookValues/DELETE_FACEBOOKVALUES',
  RESET: 'facebookValues/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFacebookValues>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type FacebookValuesState = Readonly<typeof initialState>;

// Reducer

export default (state: FacebookValuesState = initialState, action): FacebookValuesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_FACEBOOKVALUES):
    case REQUEST(ACTION_TYPES.FETCH_FACEBOOKVALUES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FACEBOOKVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FACEBOOKVALUES):
    case REQUEST(ACTION_TYPES.UPDATE_FACEBOOKVALUES):
    case REQUEST(ACTION_TYPES.DELETE_FACEBOOKVALUES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_FACEBOOKVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_FACEBOOKVALUES):
    case FAILURE(ACTION_TYPES.FETCH_FACEBOOKVALUES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FACEBOOKVALUES):
    case FAILURE(ACTION_TYPES.CREATE_FACEBOOKVALUES):
    case FAILURE(ACTION_TYPES.UPDATE_FACEBOOKVALUES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_FACEBOOKVALUES):
    case FAILURE(ACTION_TYPES.DELETE_FACEBOOKVALUES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_FACEBOOKVALUES):
    case SUCCESS(ACTION_TYPES.FETCH_FACEBOOKVALUES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_FACEBOOKVALUES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FACEBOOKVALUES):
    case SUCCESS(ACTION_TYPES.UPDATE_FACEBOOKVALUES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_FACEBOOKVALUES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FACEBOOKVALUES):
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

const apiUrl = 'api/facebook-values';
const apiSearchUrl = 'api/_search/facebook-values';

// Actions

export const getSearchEntities: ICrudSearchAction<IFacebookValues> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_FACEBOOKVALUES,
  payload: axios.get<IFacebookValues>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IFacebookValues> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FACEBOOKVALUES_LIST,
    payload: axios.get<IFacebookValues>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IFacebookValues> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FACEBOOKVALUES,
    payload: axios.get<IFacebookValues>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFacebookValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FACEBOOKVALUES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFacebookValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FACEBOOKVALUES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IFacebookValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_FACEBOOKVALUES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFacebookValues> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FACEBOOKVALUES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
