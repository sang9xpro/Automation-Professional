import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITaskFields, defaultValue } from 'app/shared/model/task-fields.model';

export const ACTION_TYPES = {
  SEARCH_TASKFIELDS: 'taskFields/SEARCH_TASKFIELDS',
  FETCH_TASKFIELDS_LIST: 'taskFields/FETCH_TASKFIELDS_LIST',
  FETCH_TASKFIELDS: 'taskFields/FETCH_TASKFIELDS',
  CREATE_TASKFIELDS: 'taskFields/CREATE_TASKFIELDS',
  UPDATE_TASKFIELDS: 'taskFields/UPDATE_TASKFIELDS',
  PARTIAL_UPDATE_TASKFIELDS: 'taskFields/PARTIAL_UPDATE_TASKFIELDS',
  DELETE_TASKFIELDS: 'taskFields/DELETE_TASKFIELDS',
  RESET: 'taskFields/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITaskFields>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type TaskFieldsState = Readonly<typeof initialState>;

// Reducer

export default (state: TaskFieldsState = initialState, action): TaskFieldsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_TASKFIELDS):
    case REQUEST(ACTION_TYPES.FETCH_TASKFIELDS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TASKFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TASKFIELDS):
    case REQUEST(ACTION_TYPES.UPDATE_TASKFIELDS):
    case REQUEST(ACTION_TYPES.DELETE_TASKFIELDS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_TASKFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_TASKFIELDS):
    case FAILURE(ACTION_TYPES.FETCH_TASKFIELDS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TASKFIELDS):
    case FAILURE(ACTION_TYPES.CREATE_TASKFIELDS):
    case FAILURE(ACTION_TYPES.UPDATE_TASKFIELDS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_TASKFIELDS):
    case FAILURE(ACTION_TYPES.DELETE_TASKFIELDS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_TASKFIELDS):
    case SUCCESS(ACTION_TYPES.FETCH_TASKFIELDS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_TASKFIELDS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TASKFIELDS):
    case SUCCESS(ACTION_TYPES.UPDATE_TASKFIELDS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_TASKFIELDS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TASKFIELDS):
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

const apiUrl = 'api/task-fields';
const apiSearchUrl = 'api/_search/task-fields';

// Actions

export const getSearchEntities: ICrudSearchAction<ITaskFields> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_TASKFIELDS,
  payload: axios.get<ITaskFields>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ITaskFields> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TASKFIELDS_LIST,
    payload: axios.get<ITaskFields>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ITaskFields> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TASKFIELDS,
    payload: axios.get<ITaskFields>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITaskFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TASKFIELDS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITaskFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TASKFIELDS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ITaskFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_TASKFIELDS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITaskFields> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TASKFIELDS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
