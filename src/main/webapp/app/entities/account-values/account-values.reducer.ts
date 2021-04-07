import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAccountValues, defaultValue } from 'app/shared/model/account-values.model';

export const ACTION_TYPES = {
  SEARCH_ACCOUNTVALUES: 'accountValues/SEARCH_ACCOUNTVALUES',
  FETCH_ACCOUNTVALUES_LIST: 'accountValues/FETCH_ACCOUNTVALUES_LIST',
  FETCH_ACCOUNTVALUES: 'accountValues/FETCH_ACCOUNTVALUES',
  CREATE_ACCOUNTVALUES: 'accountValues/CREATE_ACCOUNTVALUES',
  UPDATE_ACCOUNTVALUES: 'accountValues/UPDATE_ACCOUNTVALUES',
  PARTIAL_UPDATE_ACCOUNTVALUES: 'accountValues/PARTIAL_UPDATE_ACCOUNTVALUES',
  DELETE_ACCOUNTVALUES: 'accountValues/DELETE_ACCOUNTVALUES',
  RESET: 'accountValues/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAccountValues>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AccountValuesState = Readonly<typeof initialState>;

// Reducer

export default (state: AccountValuesState = initialState, action): AccountValuesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ACCOUNTVALUES):
    case REQUEST(ACTION_TYPES.FETCH_ACCOUNTVALUES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACCOUNTVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ACCOUNTVALUES):
    case REQUEST(ACTION_TYPES.UPDATE_ACCOUNTVALUES):
    case REQUEST(ACTION_TYPES.DELETE_ACCOUNTVALUES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ACCOUNTVALUES):
    case FAILURE(ACTION_TYPES.FETCH_ACCOUNTVALUES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACCOUNTVALUES):
    case FAILURE(ACTION_TYPES.CREATE_ACCOUNTVALUES):
    case FAILURE(ACTION_TYPES.UPDATE_ACCOUNTVALUES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTVALUES):
    case FAILURE(ACTION_TYPES.DELETE_ACCOUNTVALUES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ACCOUNTVALUES):
    case SUCCESS(ACTION_TYPES.FETCH_ACCOUNTVALUES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACCOUNTVALUES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACCOUNTVALUES):
    case SUCCESS(ACTION_TYPES.UPDATE_ACCOUNTVALUES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTVALUES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACCOUNTVALUES):
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

const apiUrl = 'api/account-values';
const apiSearchUrl = 'api/_search/account-values';

// Actions

export const getSearchEntities: ICrudSearchAction<IAccountValues> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ACCOUNTVALUES,
  payload: axios.get<IAccountValues>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAccountValues> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACCOUNTVALUES_LIST,
    payload: axios.get<IAccountValues>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAccountValues> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACCOUNTVALUES,
    payload: axios.get<IAccountValues>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAccountValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACCOUNTVALUES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAccountValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACCOUNTVALUES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IAccountValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTVALUES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAccountValues> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACCOUNTVALUES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
