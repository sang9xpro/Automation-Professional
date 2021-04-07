import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISchedulerTaskDeviceValues, defaultValue } from 'app/shared/model/scheduler-task-device-values.model';

export const ACTION_TYPES = {
  SEARCH_SCHEDULERTASKDEVICEVALUES: 'schedulerTaskDeviceValues/SEARCH_SCHEDULERTASKDEVICEVALUES',
  FETCH_SCHEDULERTASKDEVICEVALUES_LIST: 'schedulerTaskDeviceValues/FETCH_SCHEDULERTASKDEVICEVALUES_LIST',
  FETCH_SCHEDULERTASKDEVICEVALUES: 'schedulerTaskDeviceValues/FETCH_SCHEDULERTASKDEVICEVALUES',
  CREATE_SCHEDULERTASKDEVICEVALUES: 'schedulerTaskDeviceValues/CREATE_SCHEDULERTASKDEVICEVALUES',
  UPDATE_SCHEDULERTASKDEVICEVALUES: 'schedulerTaskDeviceValues/UPDATE_SCHEDULERTASKDEVICEVALUES',
  PARTIAL_UPDATE_SCHEDULERTASKDEVICEVALUES: 'schedulerTaskDeviceValues/PARTIAL_UPDATE_SCHEDULERTASKDEVICEVALUES',
  DELETE_SCHEDULERTASKDEVICEVALUES: 'schedulerTaskDeviceValues/DELETE_SCHEDULERTASKDEVICEVALUES',
  RESET: 'schedulerTaskDeviceValues/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISchedulerTaskDeviceValues>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SchedulerTaskDeviceValuesState = Readonly<typeof initialState>;

// Reducer

export default (state: SchedulerTaskDeviceValuesState = initialState, action): SchedulerTaskDeviceValuesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICEVALUES):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEVALUES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SCHEDULERTASKDEVICEVALUES):
    case REQUEST(ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICEVALUES):
    case REQUEST(ACTION_TYPES.DELETE_SCHEDULERTASKDEVICEVALUES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICEVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICEVALUES):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEVALUES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEVALUES):
    case FAILURE(ACTION_TYPES.CREATE_SCHEDULERTASKDEVICEVALUES):
    case FAILURE(ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICEVALUES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICEVALUES):
    case FAILURE(ACTION_TYPES.DELETE_SCHEDULERTASKDEVICEVALUES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICEVALUES):
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEVALUES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEVALUES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SCHEDULERTASKDEVICEVALUES):
    case SUCCESS(ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICEVALUES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICEVALUES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SCHEDULERTASKDEVICEVALUES):
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

const apiUrl = 'api/scheduler-task-device-values';
const apiSearchUrl = 'api/_search/scheduler-task-device-values';

// Actions

export const getSearchEntities: ICrudSearchAction<ISchedulerTaskDeviceValues> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICEVALUES,
  payload: axios.get<ISchedulerTaskDeviceValues>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ISchedulerTaskDeviceValues> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEVALUES_LIST,
    payload: axios.get<ISchedulerTaskDeviceValues>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISchedulerTaskDeviceValues> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEVALUES,
    payload: axios.get<ISchedulerTaskDeviceValues>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISchedulerTaskDeviceValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SCHEDULERTASKDEVICEVALUES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISchedulerTaskDeviceValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICEVALUES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ISchedulerTaskDeviceValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICEVALUES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISchedulerTaskDeviceValues> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SCHEDULERTASKDEVICEVALUES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
