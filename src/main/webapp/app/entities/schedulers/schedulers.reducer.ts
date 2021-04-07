import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISchedulers, defaultValue } from 'app/shared/model/schedulers.model';

export const ACTION_TYPES = {
  SEARCH_SCHEDULERS: 'schedulers/SEARCH_SCHEDULERS',
  FETCH_SCHEDULERS_LIST: 'schedulers/FETCH_SCHEDULERS_LIST',
  FETCH_SCHEDULERS: 'schedulers/FETCH_SCHEDULERS',
  CREATE_SCHEDULERS: 'schedulers/CREATE_SCHEDULERS',
  UPDATE_SCHEDULERS: 'schedulers/UPDATE_SCHEDULERS',
  PARTIAL_UPDATE_SCHEDULERS: 'schedulers/PARTIAL_UPDATE_SCHEDULERS',
  DELETE_SCHEDULERS: 'schedulers/DELETE_SCHEDULERS',
  RESET: 'schedulers/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISchedulers>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SchedulersState = Readonly<typeof initialState>;

// Reducer

export default (state: SchedulersState = initialState, action): SchedulersState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SCHEDULERS):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SCHEDULERS):
    case REQUEST(ACTION_TYPES.UPDATE_SCHEDULERS):
    case REQUEST(ACTION_TYPES.DELETE_SCHEDULERS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_SCHEDULERS):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERS):
    case FAILURE(ACTION_TYPES.CREATE_SCHEDULERS):
    case FAILURE(ACTION_TYPES.UPDATE_SCHEDULERS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERS):
    case FAILURE(ACTION_TYPES.DELETE_SCHEDULERS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SCHEDULERS):
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SCHEDULERS):
    case SUCCESS(ACTION_TYPES.UPDATE_SCHEDULERS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SCHEDULERS):
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

const apiUrl = 'api/schedulers';
const apiSearchUrl = 'api/_search/schedulers';

// Actions

export const getSearchEntities: ICrudSearchAction<ISchedulers> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SCHEDULERS,
  payload: axios.get<ISchedulers>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ISchedulers> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERS_LIST,
    payload: axios.get<ISchedulers>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISchedulers> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERS,
    payload: axios.get<ISchedulers>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISchedulers> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SCHEDULERS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISchedulers> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SCHEDULERS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ISchedulers> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISchedulers> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SCHEDULERS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
