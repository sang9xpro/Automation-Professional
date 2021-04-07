import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAccounts, defaultValue } from 'app/shared/model/accounts.model';

export const ACTION_TYPES = {
  SEARCH_ACCOUNTS: 'accounts/SEARCH_ACCOUNTS',
  FETCH_ACCOUNTS_LIST: 'accounts/FETCH_ACCOUNTS_LIST',
  FETCH_ACCOUNTS: 'accounts/FETCH_ACCOUNTS',
  CREATE_ACCOUNTS: 'accounts/CREATE_ACCOUNTS',
  UPDATE_ACCOUNTS: 'accounts/UPDATE_ACCOUNTS',
  PARTIAL_UPDATE_ACCOUNTS: 'accounts/PARTIAL_UPDATE_ACCOUNTS',
  DELETE_ACCOUNTS: 'accounts/DELETE_ACCOUNTS',
  RESET: 'accounts/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAccounts>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AccountsState = Readonly<typeof initialState>;

// Reducer

export default (state: AccountsState = initialState, action): AccountsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ACCOUNTS):
    case REQUEST(ACTION_TYPES.FETCH_ACCOUNTS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACCOUNTS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ACCOUNTS):
    case REQUEST(ACTION_TYPES.UPDATE_ACCOUNTS):
    case REQUEST(ACTION_TYPES.DELETE_ACCOUNTS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ACCOUNTS):
    case FAILURE(ACTION_TYPES.FETCH_ACCOUNTS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACCOUNTS):
    case FAILURE(ACTION_TYPES.CREATE_ACCOUNTS):
    case FAILURE(ACTION_TYPES.UPDATE_ACCOUNTS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTS):
    case FAILURE(ACTION_TYPES.DELETE_ACCOUNTS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ACCOUNTS):
    case SUCCESS(ACTION_TYPES.FETCH_ACCOUNTS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACCOUNTS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACCOUNTS):
    case SUCCESS(ACTION_TYPES.UPDATE_ACCOUNTS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACCOUNTS):
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

const apiUrl = 'api/accounts';
const apiSearchUrl = 'api/_search/accounts';

// Actions

export const getSearchEntities: ICrudSearchAction<IAccounts> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ACCOUNTS,
  payload: axios.get<IAccounts>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAccounts> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACCOUNTS_LIST,
    payload: axios.get<IAccounts>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAccounts> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACCOUNTS,
    payload: axios.get<IAccounts>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAccounts> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACCOUNTS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAccounts> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACCOUNTS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IAccounts> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_ACCOUNTS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAccounts> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACCOUNTS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
