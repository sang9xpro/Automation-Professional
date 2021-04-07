import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAccountFields, defaultValue } from 'app/shared/model/account-fields.model';

export const ACTION_TYPES = {
  SEARCH_ACCOUNTFIELDS: 'accountFields/SEARCH_ACCOUNTFIELDS',
  FETCH_ACCOUNTFIELDS_LIST: 'accountFields/FETCH_ACCOUNTFIELDS_LIST',
  FETCH_ACCOUNTFIELDS: 'accountFields/FETCH_ACCOUNTFIELDS',
  CREATE_ACCOUNTFIELDS: 'accountFields/CREATE_ACCOUNTFIELDS',
  UPDATE_ACCOUNTFIELDS: 'accountFields/UPDATE_ACCOUNTFIELDS',
  PARTIAL_UPDATE_ACCOUNTFIELDS: 'accountFields/PARTIAL_UPDATE_ACCOUNTFIELDS',
  DELETE_ACCOUNTFIELDS: 'accountFields/DELETE_ACCOUNTFIELDS',
  RESET: 'accountFields/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAccountFields>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AccountFieldsState = Readonly<typeof initialState>;

// Reducer

export default (state: AccountFieldsState = initialState, action): AccountFieldsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ACCOUNTFIELDS):
    case REQUEST(ACTION_TYPES.FETCH_ACCOUNTFIELDS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACCOUNTFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ACCOUNTFIELDS):
    case REQUEST(ACTION_TYPES.UPDATE_ACCOUNTFIELDS):
    case REQUEST(ACTION_TYPES.DELETE_ACCOUNTFIELDS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ACCOUNTFIELDS):
    case FAILURE(ACTION_TYPES.FETCH_ACCOUNTFIELDS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACCOUNTFIELDS):
    case FAILURE(ACTION_TYPES.CREATE_ACCOUNTFIELDS):
    case FAILURE(ACTION_TYPES.UPDATE_ACCOUNTFIELDS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTFIELDS):
    case FAILURE(ACTION_TYPES.DELETE_ACCOUNTFIELDS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ACCOUNTFIELDS):
    case SUCCESS(ACTION_TYPES.FETCH_ACCOUNTFIELDS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACCOUNTFIELDS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACCOUNTFIELDS):
    case SUCCESS(ACTION_TYPES.UPDATE_ACCOUNTFIELDS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTFIELDS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACCOUNTFIELDS):
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

const apiUrl = 'api/account-fields';
const apiSearchUrl = 'api/_search/account-fields';

// Actions

export const getSearchEntities: ICrudSearchAction<IAccountFields> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ACCOUNTFIELDS,
  payload: axios.get<IAccountFields>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAccountFields> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACCOUNTFIELDS_LIST,
    payload: axios.get<IAccountFields>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAccountFields> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACCOUNTFIELDS,
    payload: axios.get<IAccountFields>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAccountFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACCOUNTFIELDS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAccountFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACCOUNTFIELDS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IAccountFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTFIELDS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAccountFields> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACCOUNTFIELDS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
