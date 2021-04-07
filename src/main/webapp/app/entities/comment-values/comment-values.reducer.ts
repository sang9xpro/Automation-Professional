import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICommentValues, defaultValue } from 'app/shared/model/comment-values.model';

export const ACTION_TYPES = {
  SEARCH_COMMENTVALUES: 'commentValues/SEARCH_COMMENTVALUES',
  FETCH_COMMENTVALUES_LIST: 'commentValues/FETCH_COMMENTVALUES_LIST',
  FETCH_COMMENTVALUES: 'commentValues/FETCH_COMMENTVALUES',
  CREATE_COMMENTVALUES: 'commentValues/CREATE_COMMENTVALUES',
  UPDATE_COMMENTVALUES: 'commentValues/UPDATE_COMMENTVALUES',
  PARTIAL_UPDATE_COMMENTVALUES: 'commentValues/PARTIAL_UPDATE_COMMENTVALUES',
  DELETE_COMMENTVALUES: 'commentValues/DELETE_COMMENTVALUES',
  RESET: 'commentValues/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICommentValues>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CommentValuesState = Readonly<typeof initialState>;

// Reducer

export default (state: CommentValuesState = initialState, action): CommentValuesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_COMMENTVALUES):
    case REQUEST(ACTION_TYPES.FETCH_COMMENTVALUES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COMMENTVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_COMMENTVALUES):
    case REQUEST(ACTION_TYPES.UPDATE_COMMENTVALUES):
    case REQUEST(ACTION_TYPES.DELETE_COMMENTVALUES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_COMMENTVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_COMMENTVALUES):
    case FAILURE(ACTION_TYPES.FETCH_COMMENTVALUES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COMMENTVALUES):
    case FAILURE(ACTION_TYPES.CREATE_COMMENTVALUES):
    case FAILURE(ACTION_TYPES.UPDATE_COMMENTVALUES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_COMMENTVALUES):
    case FAILURE(ACTION_TYPES.DELETE_COMMENTVALUES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_COMMENTVALUES):
    case SUCCESS(ACTION_TYPES.FETCH_COMMENTVALUES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_COMMENTVALUES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_COMMENTVALUES):
    case SUCCESS(ACTION_TYPES.UPDATE_COMMENTVALUES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_COMMENTVALUES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_COMMENTVALUES):
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

const apiUrl = 'api/comment-values';
const apiSearchUrl = 'api/_search/comment-values';

// Actions

export const getSearchEntities: ICrudSearchAction<ICommentValues> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_COMMENTVALUES,
  payload: axios.get<ICommentValues>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ICommentValues> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_COMMENTVALUES_LIST,
    payload: axios.get<ICommentValues>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICommentValues> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COMMENTVALUES,
    payload: axios.get<ICommentValues>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICommentValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COMMENTVALUES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICommentValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COMMENTVALUES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICommentValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_COMMENTVALUES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICommentValues> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COMMENTVALUES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
