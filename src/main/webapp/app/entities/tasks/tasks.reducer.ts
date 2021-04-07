import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITasks, defaultValue } from 'app/shared/model/tasks.model';

export const ACTION_TYPES = {
  SEARCH_TASKS: 'tasks/SEARCH_TASKS',
  FETCH_TASKS_LIST: 'tasks/FETCH_TASKS_LIST',
  FETCH_TASKS: 'tasks/FETCH_TASKS',
  CREATE_TASKS: 'tasks/CREATE_TASKS',
  UPDATE_TASKS: 'tasks/UPDATE_TASKS',
  PARTIAL_UPDATE_TASKS: 'tasks/PARTIAL_UPDATE_TASKS',
  DELETE_TASKS: 'tasks/DELETE_TASKS',
  RESET: 'tasks/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITasks>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type TasksState = Readonly<typeof initialState>;

// Reducer

export default (state: TasksState = initialState, action): TasksState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_TASKS):
    case REQUEST(ACTION_TYPES.FETCH_TASKS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TASKS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TASKS):
    case REQUEST(ACTION_TYPES.UPDATE_TASKS):
    case REQUEST(ACTION_TYPES.DELETE_TASKS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_TASKS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_TASKS):
    case FAILURE(ACTION_TYPES.FETCH_TASKS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TASKS):
    case FAILURE(ACTION_TYPES.CREATE_TASKS):
    case FAILURE(ACTION_TYPES.UPDATE_TASKS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_TASKS):
    case FAILURE(ACTION_TYPES.DELETE_TASKS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_TASKS):
    case SUCCESS(ACTION_TYPES.FETCH_TASKS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_TASKS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TASKS):
    case SUCCESS(ACTION_TYPES.UPDATE_TASKS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_TASKS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TASKS):
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

const apiUrl = 'api/tasks';
const apiSearchUrl = 'api/_search/tasks';

// Actions

export const getSearchEntities: ICrudSearchAction<ITasks> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_TASKS,
  payload: axios.get<ITasks>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ITasks> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TASKS_LIST,
    payload: axios.get<ITasks>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ITasks> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TASKS,
    payload: axios.get<ITasks>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITasks> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TASKS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITasks> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TASKS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ITasks> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_TASKS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITasks> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TASKS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
