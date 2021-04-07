import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISchedulerTaskDevice, defaultValue } from 'app/shared/model/scheduler-task-device.model';

export const ACTION_TYPES = {
  SEARCH_SCHEDULERTASKDEVICES: 'schedulerTaskDevice/SEARCH_SCHEDULERTASKDEVICES',
  FETCH_SCHEDULERTASKDEVICE_LIST: 'schedulerTaskDevice/FETCH_SCHEDULERTASKDEVICE_LIST',
  FETCH_SCHEDULERTASKDEVICE: 'schedulerTaskDevice/FETCH_SCHEDULERTASKDEVICE',
  CREATE_SCHEDULERTASKDEVICE: 'schedulerTaskDevice/CREATE_SCHEDULERTASKDEVICE',
  UPDATE_SCHEDULERTASKDEVICE: 'schedulerTaskDevice/UPDATE_SCHEDULERTASKDEVICE',
  PARTIAL_UPDATE_SCHEDULERTASKDEVICE: 'schedulerTaskDevice/PARTIAL_UPDATE_SCHEDULERTASKDEVICE',
  DELETE_SCHEDULERTASKDEVICE: 'schedulerTaskDevice/DELETE_SCHEDULERTASKDEVICE',
  RESET: 'schedulerTaskDevice/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISchedulerTaskDevice>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SchedulerTaskDeviceState = Readonly<typeof initialState>;

// Reducer

export default (state: SchedulerTaskDeviceState = initialState, action): SchedulerTaskDeviceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICES):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SCHEDULERTASKDEVICE):
    case REQUEST(ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICE):
    case REQUEST(ACTION_TYPES.DELETE_SCHEDULERTASKDEVICE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICES):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICE):
    case FAILURE(ACTION_TYPES.CREATE_SCHEDULERTASKDEVICE):
    case FAILURE(ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICE):
    case FAILURE(ACTION_TYPES.DELETE_SCHEDULERTASKDEVICE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICES):
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SCHEDULERTASKDEVICE):
    case SUCCESS(ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SCHEDULERTASKDEVICE):
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

const apiUrl = 'api/scheduler-task-devices';
const apiSearchUrl = 'api/_search/scheduler-task-devices';

// Actions

export const getSearchEntities: ICrudSearchAction<ISchedulerTaskDevice> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICES,
  payload: axios.get<ISchedulerTaskDevice>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ISchedulerTaskDevice> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERTASKDEVICE_LIST,
    payload: axios.get<ISchedulerTaskDevice>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISchedulerTaskDevice> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERTASKDEVICE,
    payload: axios.get<ISchedulerTaskDevice>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISchedulerTaskDevice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SCHEDULERTASKDEVICE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISchedulerTaskDevice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ISchedulerTaskDevice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISchedulerTaskDevice> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SCHEDULERTASKDEVICE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
