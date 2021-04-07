import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISchedulerValue, defaultValue } from 'app/shared/model/scheduler-value.model';

export const ACTION_TYPES = {
  SEARCH_SCHEDULERVALUES: 'schedulerValue/SEARCH_SCHEDULERVALUES',
  FETCH_SCHEDULERVALUE_LIST: 'schedulerValue/FETCH_SCHEDULERVALUE_LIST',
  FETCH_SCHEDULERVALUE: 'schedulerValue/FETCH_SCHEDULERVALUE',
  CREATE_SCHEDULERVALUE: 'schedulerValue/CREATE_SCHEDULERVALUE',
  UPDATE_SCHEDULERVALUE: 'schedulerValue/UPDATE_SCHEDULERVALUE',
  PARTIAL_UPDATE_SCHEDULERVALUE: 'schedulerValue/PARTIAL_UPDATE_SCHEDULERVALUE',
  DELETE_SCHEDULERVALUE: 'schedulerValue/DELETE_SCHEDULERVALUE',
  RESET: 'schedulerValue/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISchedulerValue>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SchedulerValueState = Readonly<typeof initialState>;

// Reducer

export default (state: SchedulerValueState = initialState, action): SchedulerValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SCHEDULERVALUES):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SCHEDULERVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_SCHEDULERVALUE):
    case REQUEST(ACTION_TYPES.DELETE_SCHEDULERVALUE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_SCHEDULERVALUES):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERVALUE):
    case FAILURE(ACTION_TYPES.CREATE_SCHEDULERVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_SCHEDULERVALUE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERVALUE):
    case FAILURE(ACTION_TYPES.DELETE_SCHEDULERVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SCHEDULERVALUES):
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SCHEDULERVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_SCHEDULERVALUE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SCHEDULERVALUE):
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

const apiUrl = 'api/scheduler-values';
const apiSearchUrl = 'api/_search/scheduler-values';

// Actions

export const getSearchEntities: ICrudSearchAction<ISchedulerValue> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SCHEDULERVALUES,
  payload: axios.get<ISchedulerValue>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ISchedulerValue> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERVALUE_LIST,
    payload: axios.get<ISchedulerValue>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISchedulerValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERVALUE,
    payload: axios.get<ISchedulerValue>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISchedulerValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SCHEDULERVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISchedulerValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SCHEDULERVALUE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ISchedulerValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERVALUE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISchedulerValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SCHEDULERVALUE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
