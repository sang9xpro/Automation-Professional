import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISchedulerFields, defaultValue } from 'app/shared/model/scheduler-fields.model';

export const ACTION_TYPES = {
  SEARCH_SCHEDULERFIELDS: 'schedulerFields/SEARCH_SCHEDULERFIELDS',
  FETCH_SCHEDULERFIELDS_LIST: 'schedulerFields/FETCH_SCHEDULERFIELDS_LIST',
  FETCH_SCHEDULERFIELDS: 'schedulerFields/FETCH_SCHEDULERFIELDS',
  CREATE_SCHEDULERFIELDS: 'schedulerFields/CREATE_SCHEDULERFIELDS',
  UPDATE_SCHEDULERFIELDS: 'schedulerFields/UPDATE_SCHEDULERFIELDS',
  PARTIAL_UPDATE_SCHEDULERFIELDS: 'schedulerFields/PARTIAL_UPDATE_SCHEDULERFIELDS',
  DELETE_SCHEDULERFIELDS: 'schedulerFields/DELETE_SCHEDULERFIELDS',
  RESET: 'schedulerFields/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISchedulerFields>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SchedulerFieldsState = Readonly<typeof initialState>;

// Reducer

export default (state: SchedulerFieldsState = initialState, action): SchedulerFieldsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SCHEDULERFIELDS):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERFIELDS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SCHEDULERFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SCHEDULERFIELDS):
    case REQUEST(ACTION_TYPES.UPDATE_SCHEDULERFIELDS):
    case REQUEST(ACTION_TYPES.DELETE_SCHEDULERFIELDS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_SCHEDULERFIELDS):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERFIELDS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SCHEDULERFIELDS):
    case FAILURE(ACTION_TYPES.CREATE_SCHEDULERFIELDS):
    case FAILURE(ACTION_TYPES.UPDATE_SCHEDULERFIELDS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERFIELDS):
    case FAILURE(ACTION_TYPES.DELETE_SCHEDULERFIELDS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SCHEDULERFIELDS):
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERFIELDS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SCHEDULERFIELDS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SCHEDULERFIELDS):
    case SUCCESS(ACTION_TYPES.UPDATE_SCHEDULERFIELDS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERFIELDS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SCHEDULERFIELDS):
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

const apiUrl = 'api/scheduler-fields';
const apiSearchUrl = 'api/_search/scheduler-fields';

// Actions

export const getSearchEntities: ICrudSearchAction<ISchedulerFields> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SCHEDULERFIELDS,
  payload: axios.get<ISchedulerFields>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ISchedulerFields> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERFIELDS_LIST,
    payload: axios.get<ISchedulerFields>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISchedulerFields> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SCHEDULERFIELDS,
    payload: axios.get<ISchedulerFields>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISchedulerFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SCHEDULERFIELDS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISchedulerFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SCHEDULERFIELDS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ISchedulerFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_SCHEDULERFIELDS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISchedulerFields> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SCHEDULERFIELDS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
