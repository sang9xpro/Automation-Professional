import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICommentFields, defaultValue } from 'app/shared/model/comment-fields.model';

export const ACTION_TYPES = {
  SEARCH_COMMENTFIELDS: 'commentFields/SEARCH_COMMENTFIELDS',
  FETCH_COMMENTFIELDS_LIST: 'commentFields/FETCH_COMMENTFIELDS_LIST',
  FETCH_COMMENTFIELDS: 'commentFields/FETCH_COMMENTFIELDS',
  CREATE_COMMENTFIELDS: 'commentFields/CREATE_COMMENTFIELDS',
  UPDATE_COMMENTFIELDS: 'commentFields/UPDATE_COMMENTFIELDS',
  PARTIAL_UPDATE_COMMENTFIELDS: 'commentFields/PARTIAL_UPDATE_COMMENTFIELDS',
  DELETE_COMMENTFIELDS: 'commentFields/DELETE_COMMENTFIELDS',
  RESET: 'commentFields/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICommentFields>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CommentFieldsState = Readonly<typeof initialState>;

// Reducer

export default (state: CommentFieldsState = initialState, action): CommentFieldsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_COMMENTFIELDS):
    case REQUEST(ACTION_TYPES.FETCH_COMMENTFIELDS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COMMENTFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_COMMENTFIELDS):
    case REQUEST(ACTION_TYPES.UPDATE_COMMENTFIELDS):
    case REQUEST(ACTION_TYPES.DELETE_COMMENTFIELDS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_COMMENTFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_COMMENTFIELDS):
    case FAILURE(ACTION_TYPES.FETCH_COMMENTFIELDS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COMMENTFIELDS):
    case FAILURE(ACTION_TYPES.CREATE_COMMENTFIELDS):
    case FAILURE(ACTION_TYPES.UPDATE_COMMENTFIELDS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_COMMENTFIELDS):
    case FAILURE(ACTION_TYPES.DELETE_COMMENTFIELDS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_COMMENTFIELDS):
    case SUCCESS(ACTION_TYPES.FETCH_COMMENTFIELDS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_COMMENTFIELDS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_COMMENTFIELDS):
    case SUCCESS(ACTION_TYPES.UPDATE_COMMENTFIELDS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_COMMENTFIELDS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_COMMENTFIELDS):
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

const apiUrl = 'api/comment-fields';
const apiSearchUrl = 'api/_search/comment-fields';

// Actions

export const getSearchEntities: ICrudSearchAction<ICommentFields> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_COMMENTFIELDS,
  payload: axios.get<ICommentFields>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ICommentFields> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_COMMENTFIELDS_LIST,
    payload: axios.get<ICommentFields>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICommentFields> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COMMENTFIELDS,
    payload: axios.get<ICommentFields>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICommentFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COMMENTFIELDS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICommentFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COMMENTFIELDS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICommentFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_COMMENTFIELDS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICommentFields> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COMMENTFIELDS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
