import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISchedulerTaskDeviceFields, defaultValue } from 'app/shared/model/scheduler-task-device-fields.model';

export const ACTION_TYPES = {
  SEARCH_SCHEDULERTASKDEVICEFIELDS: 'schedulerTaskDeviceFields/SEARCH_SCHEDULERTASKDEVICEFIELDS',
  FETCH_SCHEDULERTASKDEVICEFIELDS_LIST: 'schedulerTaskDeviceFields/FETCH_SCHEDULERTASKDEVICEFIELDS_LIST',
  FETCH_SCHEDULERTASKDEVICEFIELDS: 'schedulerTaskDeviceFields/FETCH_SCHEDULERTASKDEVICEFIELDS',
  CREATE_SCHEDULERTASKDEVICEFIELDS: 'schedulerTaskDeviceFields/CREATE_SCHEDULERTASKDEVICEFIELDS',
  UPDATE_SCHEDULERTASKDEVICEFIELDS: 'schedulerTaskDeviceFields/UPDATE_SCHEDULERTASKDEVICEFIELDS',
  PARTIAL_UPDATE_SCHEDULERTASKDEVICEFIELDS: 'schedulerTaskDeviceFields/PARTIAL_UPDATE_SCHEDULERTASKDEVICEFIELDS',
  DELETE_SCHEDULERTASKDEVICEFIELDS: 'schedulerTaskDeviceFields/DELETE_SCHEDULERTASKDEVICEFIELDS',
  RESET: 'schedulerTaskDeviceFields/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISchedulerTaskDeviceFields>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SchedulerTaskDeviceFieldsState = Readonly<typeof initialState>;

// Reducer

export default (state: SchedulerTaskDeviceFieldsState = initialState, action): SchedulerTaskDeviceFieldsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICEFIELDS):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEFIELDS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SCHEDULERTASKDEVICEFIELDS):
    case REQUEST(ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICEFIELDS):
    case REQUEST(ACTION_TYPES.DELETE_SCHEDULERTASKDEVICEFIELDS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICEFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICEFIELDS):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEFIELDS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEFIELDS):
    case FAILURE(ACTION_TYPES.CREATE_SCHEDULERTASKDEVICEFIELDS):
    case FAILURE(ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICEFIELDS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICEFIELDS):
    case FAILURE(ACTION_TYPES.DELETE_SCHEDULERTASKDEVICEFIELDS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICEFIELDS):
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEFIELDS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEFIELDS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SCHEDULERTASKDEVICEFIELDS):
    case SUCCESS(ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICEFIELDS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICEFIELDS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SCHEDULERTASKDEVICEFIELDS):
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

const apiUrl = 'api/scheduler-task-device-fields';
const apiSearchUrl = 'api/_search/scheduler-task-device-fields';

// Actions

export const getSearchEntities: ICrudSearchAction<ISchedulerTaskDeviceFields> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SCHEDULERTASKDEVICEFIELDS,
  payload: axios.get<ISchedulerTaskDeviceFields>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ISchedulerTaskDeviceFields> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEFIELDS_LIST,
    payload: axios.get<ISchedulerTaskDeviceFields>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISchedulerTaskDeviceFields> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERTASKDEVICEFIELDS,
    payload: axios.get<ISchedulerTaskDeviceFields>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISchedulerTaskDeviceFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SCHEDULERTASKDEVICEFIELDS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISchedulerTaskDeviceFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SCHEDULERTASKDEVICEFIELDS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ISchedulerTaskDeviceFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERTASKDEVICEFIELDS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISchedulerTaskDeviceFields> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SCHEDULERTASKDEVICEFIELDS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
