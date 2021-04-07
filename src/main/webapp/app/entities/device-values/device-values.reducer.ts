import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDeviceValues, defaultValue } from 'app/shared/model/device-values.model';

export const ACTION_TYPES = {
  SEARCH_DEVICEVALUES: 'deviceValues/SEARCH_DEVICEVALUES',
  FETCH_DEVICEVALUES_LIST: 'deviceValues/FETCH_DEVICEVALUES_LIST',
  FETCH_DEVICEVALUES: 'deviceValues/FETCH_DEVICEVALUES',
  CREATE_DEVICEVALUES: 'deviceValues/CREATE_DEVICEVALUES',
  UPDATE_DEVICEVALUES: 'deviceValues/UPDATE_DEVICEVALUES',
  PARTIAL_UPDATE_DEVICEVALUES: 'deviceValues/PARTIAL_UPDATE_DEVICEVALUES',
  DELETE_DEVICEVALUES: 'deviceValues/DELETE_DEVICEVALUES',
  RESET: 'deviceValues/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDeviceValues>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type DeviceValuesState = Readonly<typeof initialState>;

// Reducer

export default (state: DeviceValuesState = initialState, action): DeviceValuesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_DEVICEVALUES):
    case REQUEST(ACTION_TYPES.FETCH_DEVICEVALUES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DEVICEVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_DEVICEVALUES):
    case REQUEST(ACTION_TYPES.UPDATE_DEVICEVALUES):
    case REQUEST(ACTION_TYPES.DELETE_DEVICEVALUES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_DEVICEVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_DEVICEVALUES):
    case FAILURE(ACTION_TYPES.FETCH_DEVICEVALUES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DEVICEVALUES):
    case FAILURE(ACTION_TYPES.CREATE_DEVICEVALUES):
    case FAILURE(ACTION_TYPES.UPDATE_DEVICEVALUES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_DEVICEVALUES):
    case FAILURE(ACTION_TYPES.DELETE_DEVICEVALUES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_DEVICEVALUES):
    case SUCCESS(ACTION_TYPES.FETCH_DEVICEVALUES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_DEVICEVALUES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_DEVICEVALUES):
    case SUCCESS(ACTION_TYPES.UPDATE_DEVICEVALUES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_DEVICEVALUES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_DEVICEVALUES):
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

const apiUrl = 'api/device-values';
const apiSearchUrl = 'api/_search/device-values';

// Actions

export const getSearchEntities: ICrudSearchAction<IDeviceValues> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_DEVICEVALUES,
  payload: axios.get<IDeviceValues>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IDeviceValues> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DEVICEVALUES_LIST,
    payload: axios.get<IDeviceValues>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IDeviceValues> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DEVICEVALUES,
    payload: axios.get<IDeviceValues>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IDeviceValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DEVICEVALUES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDeviceValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DEVICEVALUES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IDeviceValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_DEVICEVALUES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDeviceValues> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DEVICEVALUES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
