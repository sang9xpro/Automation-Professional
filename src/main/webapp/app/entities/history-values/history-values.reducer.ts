import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IHistoryValues, defaultValue } from 'app/shared/model/history-values.model';

export const ACTION_TYPES = {
  SEARCH_HISTORYVALUES: 'historyValues/SEARCH_HISTORYVALUES',
  FETCH_HISTORYVALUES_LIST: 'historyValues/FETCH_HISTORYVALUES_LIST',
  FETCH_HISTORYVALUES: 'historyValues/FETCH_HISTORYVALUES',
  CREATE_HISTORYVALUES: 'historyValues/CREATE_HISTORYVALUES',
  UPDATE_HISTORYVALUES: 'historyValues/UPDATE_HISTORYVALUES',
  PARTIAL_UPDATE_HISTORYVALUES: 'historyValues/PARTIAL_UPDATE_HISTORYVALUES',
  DELETE_HISTORYVALUES: 'historyValues/DELETE_HISTORYVALUES',
  RESET: 'historyValues/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHistoryValues>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type HistoryValuesState = Readonly<typeof initialState>;

// Reducer

export default (state: HistoryValuesState = initialState, action): HistoryValuesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_HISTORYVALUES):
    case REQUEST(ACTION_TYPES.FETCH_HISTORYVALUES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HISTORYVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_HISTORYVALUES):
    case REQUEST(ACTION_TYPES.UPDATE_HISTORYVALUES):
    case REQUEST(ACTION_TYPES.DELETE_HISTORYVALUES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_HISTORYVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_HISTORYVALUES):
    case FAILURE(ACTION_TYPES.FETCH_HISTORYVALUES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HISTORYVALUES):
    case FAILURE(ACTION_TYPES.CREATE_HISTORYVALUES):
    case FAILURE(ACTION_TYPES.UPDATE_HISTORYVALUES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_HISTORYVALUES):
    case FAILURE(ACTION_TYPES.DELETE_HISTORYVALUES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_HISTORYVALUES):
    case SUCCESS(ACTION_TYPES.FETCH_HISTORYVALUES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_HISTORYVALUES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_HISTORYVALUES):
    case SUCCESS(ACTION_TYPES.UPDATE_HISTORYVALUES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_HISTORYVALUES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_HISTORYVALUES):
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

const apiUrl = 'api/history-values';
const apiSearchUrl = 'api/_search/history-values';

// Actions

export const getSearchEntities: ICrudSearchAction<IHistoryValues> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_HISTORYVALUES,
  payload: axios.get<IHistoryValues>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IHistoryValues> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORYVALUES_LIST,
    payload: axios.get<IHistoryValues>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IHistoryValues> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORYVALUES,
    payload: axios.get<IHistoryValues>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IHistoryValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HISTORYVALUES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHistoryValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HISTORYVALUES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IHistoryValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_HISTORYVALUES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHistoryValues> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HISTORYVALUES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
