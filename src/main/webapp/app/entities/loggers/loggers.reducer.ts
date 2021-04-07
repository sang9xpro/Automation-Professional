import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILoggers, defaultValue } from 'app/shared/model/loggers.model';

export const ACTION_TYPES = {
  SEARCH_LOGGERS: 'loggers/SEARCH_LOGGERS',
  FETCH_LOGGERS_LIST: 'loggers/FETCH_LOGGERS_LIST',
  FETCH_LOGGERS: 'loggers/FETCH_LOGGERS',
  CREATE_LOGGERS: 'loggers/CREATE_LOGGERS',
  UPDATE_LOGGERS: 'loggers/UPDATE_LOGGERS',
  PARTIAL_UPDATE_LOGGERS: 'loggers/PARTIAL_UPDATE_LOGGERS',
  DELETE_LOGGERS: 'loggers/DELETE_LOGGERS',
  SET_BLOB: 'loggers/SET_BLOB',
  RESET: 'loggers/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILoggers>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type LoggersState = Readonly<typeof initialState>;

// Reducer

export default (state: LoggersState = initialState, action): LoggersState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_LOGGERS):
    case REQUEST(ACTION_TYPES.FETCH_LOGGERS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LOGGERS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_LOGGERS):
    case REQUEST(ACTION_TYPES.UPDATE_LOGGERS):
    case REQUEST(ACTION_TYPES.DELETE_LOGGERS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_LOGGERS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_LOGGERS):
    case FAILURE(ACTION_TYPES.FETCH_LOGGERS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LOGGERS):
    case FAILURE(ACTION_TYPES.CREATE_LOGGERS):
    case FAILURE(ACTION_TYPES.UPDATE_LOGGERS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_LOGGERS):
    case FAILURE(ACTION_TYPES.DELETE_LOGGERS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_LOGGERS):
    case SUCCESS(ACTION_TYPES.FETCH_LOGGERS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOGGERS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_LOGGERS):
    case SUCCESS(ACTION_TYPES.UPDATE_LOGGERS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_LOGGERS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_LOGGERS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/loggers';
const apiSearchUrl = 'api/_search/loggers';

// Actions

export const getSearchEntities: ICrudSearchAction<ILoggers> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_LOGGERS,
  payload: axios.get<ILoggers>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ILoggers> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LOGGERS_LIST,
    payload: axios.get<ILoggers>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ILoggers> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LOGGERS,
    payload: axios.get<ILoggers>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ILoggers> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOGGERS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILoggers> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LOGGERS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ILoggers> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_LOGGERS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILoggers> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOGGERS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
