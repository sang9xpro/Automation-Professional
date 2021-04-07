import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILoggersValues, defaultValue } from 'app/shared/model/loggers-values.model';

export const ACTION_TYPES = {
  SEARCH_LOGGERSVALUES: 'loggersValues/SEARCH_LOGGERSVALUES',
  FETCH_LOGGERSVALUES_LIST: 'loggersValues/FETCH_LOGGERSVALUES_LIST',
  FETCH_LOGGERSVALUES: 'loggersValues/FETCH_LOGGERSVALUES',
  CREATE_LOGGERSVALUES: 'loggersValues/CREATE_LOGGERSVALUES',
  UPDATE_LOGGERSVALUES: 'loggersValues/UPDATE_LOGGERSVALUES',
  PARTIAL_UPDATE_LOGGERSVALUES: 'loggersValues/PARTIAL_UPDATE_LOGGERSVALUES',
  DELETE_LOGGERSVALUES: 'loggersValues/DELETE_LOGGERSVALUES',
  RESET: 'loggersValues/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILoggersValues>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type LoggersValuesState = Readonly<typeof initialState>;

// Reducer

export default (state: LoggersValuesState = initialState, action): LoggersValuesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LOGGERSVALUES):
    case REQUEST(ACTION_TYPES.FETCH_LOGGERSVALUES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LOGGERSVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_LOGGERSVALUES):
    case REQUEST(ACTION_TYPES.UPDATE_LOGGERSVALUES):
    case REQUEST(ACTION_TYPES.DELETE_LOGGERSVALUES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_LOGGERSVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_LOGGERSVALUES):
    case FAILURE(ACTION_TYPES.FETCH_LOGGERSVALUES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LOGGERSVALUES):
    case FAILURE(ACTION_TYPES.CREATE_LOGGERSVALUES):
    case FAILURE(ACTION_TYPES.UPDATE_LOGGERSVALUES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_LOGGERSVALUES):
    case FAILURE(ACTION_TYPES.DELETE_LOGGERSVALUES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_LOGGERSVALUES):
    case SUCCESS(ACTION_TYPES.FETCH_LOGGERSVALUES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOGGERSVALUES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_LOGGERSVALUES):
    case SUCCESS(ACTION_TYPES.UPDATE_LOGGERSVALUES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_LOGGERSVALUES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_LOGGERSVALUES):
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

const apiUrl = 'api/loggers-values';
const apiSearchUrl = 'api/_search/loggers-values';

// Actions

export const getSearchEntities: ICrudSearchAction<ILoggersValues> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_LOGGERSVALUES,
  payload: axios.get<ILoggersValues>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ILoggersValues> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LOGGERSVALUES_LIST,
    payload: axios.get<ILoggersValues>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ILoggersValues> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LOGGERSVALUES,
    payload: axios.get<ILoggersValues>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ILoggersValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOGGERSVALUES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILoggersValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LOGGERSVALUES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ILoggersValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_LOGGERSVALUES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILoggersValues> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOGGERSVALUES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
