import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IHistoryFields, defaultValue } from 'app/shared/model/history-fields.model';

export const ACTION_TYPES = {
  SEARCH_HISTORYFIELDS: 'historyFields/SEARCH_HISTORYFIELDS',
  FETCH_HISTORYFIELDS_LIST: 'historyFields/FETCH_HISTORYFIELDS_LIST',
  FETCH_HISTORYFIELDS: 'historyFields/FETCH_HISTORYFIELDS',
  CREATE_HISTORYFIELDS: 'historyFields/CREATE_HISTORYFIELDS',
  UPDATE_HISTORYFIELDS: 'historyFields/UPDATE_HISTORYFIELDS',
  PARTIAL_UPDATE_HISTORYFIELDS: 'historyFields/PARTIAL_UPDATE_HISTORYFIELDS',
  DELETE_HISTORYFIELDS: 'historyFields/DELETE_HISTORYFIELDS',
  RESET: 'historyFields/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHistoryFields>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type HistoryFieldsState = Readonly<typeof initialState>;

// Reducer

export default (state: HistoryFieldsState = initialState, action): HistoryFieldsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_HISTORYFIELDS):
    case REQUEST(ACTION_TYPES.FETCH_HISTORYFIELDS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HISTORYFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_HISTORYFIELDS):
    case REQUEST(ACTION_TYPES.UPDATE_HISTORYFIELDS):
    case REQUEST(ACTION_TYPES.DELETE_HISTORYFIELDS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_HISTORYFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_HISTORYFIELDS):
    case FAILURE(ACTION_TYPES.FETCH_HISTORYFIELDS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HISTORYFIELDS):
    case FAILURE(ACTION_TYPES.CREATE_HISTORYFIELDS):
    case FAILURE(ACTION_TYPES.UPDATE_HISTORYFIELDS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_HISTORYFIELDS):
    case FAILURE(ACTION_TYPES.DELETE_HISTORYFIELDS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_HISTORYFIELDS):
    case SUCCESS(ACTION_TYPES.FETCH_HISTORYFIELDS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_HISTORYFIELDS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_HISTORYFIELDS):
    case SUCCESS(ACTION_TYPES.UPDATE_HISTORYFIELDS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_HISTORYFIELDS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_HISTORYFIELDS):
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

const apiUrl = 'api/history-fields';
const apiSearchUrl = 'api/_search/history-fields';

// Actions

export const getSearchEntities: ICrudSearchAction<IHistoryFields> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_HISTORYFIELDS,
  payload: axios.get<IHistoryFields>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IHistoryFields> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORYFIELDS_LIST,
    payload: axios.get<IHistoryFields>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IHistoryFields> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORYFIELDS,
    payload: axios.get<IHistoryFields>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IHistoryFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HISTORYFIELDS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHistoryFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HISTORYFIELDS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IHistoryFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_HISTORYFIELDS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHistoryFields> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HISTORYFIELDS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
