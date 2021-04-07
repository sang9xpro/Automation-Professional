import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IHistory, defaultValue } from 'app/shared/model/history.model';

export const ACTION_TYPES = {
  SEARCH_HISTORIES: 'history/SEARCH_HISTORIES',
  FETCH_HISTORY_LIST: 'history/FETCH_HISTORY_LIST',
  FETCH_HISTORY: 'history/FETCH_HISTORY',
  CREATE_HISTORY: 'history/CREATE_HISTORY',
  UPDATE_HISTORY: 'history/UPDATE_HISTORY',
  PARTIAL_UPDATE_HISTORY: 'history/PARTIAL_UPDATE_HISTORY',
  DELETE_HISTORY: 'history/DELETE_HISTORY',
  RESET: 'history/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHistory>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type HistoryState = Readonly<typeof initialState>;

// Reducer

export default (state: HistoryState = initialState, action): HistoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_HISTORIES):
    case REQUEST(ACTION_TYPES.FETCH_HISTORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_HISTORY):
    case REQUEST(ACTION_TYPES.UPDATE_HISTORY):
    case REQUEST(ACTION_TYPES.DELETE_HISTORY):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_HISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_HISTORIES):
    case FAILURE(ACTION_TYPES.FETCH_HISTORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HISTORY):
    case FAILURE(ACTION_TYPES.CREATE_HISTORY):
    case FAILURE(ACTION_TYPES.UPDATE_HISTORY):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_HISTORY):
    case FAILURE(ACTION_TYPES.DELETE_HISTORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_HISTORIES):
    case SUCCESS(ACTION_TYPES.FETCH_HISTORY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_HISTORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_HISTORY):
    case SUCCESS(ACTION_TYPES.UPDATE_HISTORY):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_HISTORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_HISTORY):
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

const apiUrl = 'api/histories';
const apiSearchUrl = 'api/_search/histories';

// Actions

export const getSearchEntities: ICrudSearchAction<IHistory> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_HISTORIES,
  payload: axios.get<IHistory>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IHistory> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORY_LIST,
    payload: axios.get<IHistory>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IHistory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORY,
    payload: axios.get<IHistory>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HISTORY,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HISTORY,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_HISTORY,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHistory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HISTORY,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
