import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMostOfContFields, defaultValue } from 'app/shared/model/most-of-cont-fields.model';

export const ACTION_TYPES = {
  SEARCH_MOSTOFCONTFIELDS: 'mostOfContFields/SEARCH_MOSTOFCONTFIELDS',
  FETCH_MOSTOFCONTFIELDS_LIST: 'mostOfContFields/FETCH_MOSTOFCONTFIELDS_LIST',
  FETCH_MOSTOFCONTFIELDS: 'mostOfContFields/FETCH_MOSTOFCONTFIELDS',
  CREATE_MOSTOFCONTFIELDS: 'mostOfContFields/CREATE_MOSTOFCONTFIELDS',
  UPDATE_MOSTOFCONTFIELDS: 'mostOfContFields/UPDATE_MOSTOFCONTFIELDS',
  PARTIAL_UPDATE_MOSTOFCONTFIELDS: 'mostOfContFields/PARTIAL_UPDATE_MOSTOFCONTFIELDS',
  DELETE_MOSTOFCONTFIELDS: 'mostOfContFields/DELETE_MOSTOFCONTFIELDS',
  RESET: 'mostOfContFields/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMostOfContFields>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type MostOfContFieldsState = Readonly<typeof initialState>;

// Reducer

export default (state: MostOfContFieldsState = initialState, action): MostOfContFieldsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MOSTOFCONTFIELDS):
    case REQUEST(ACTION_TYPES.FETCH_MOSTOFCONTFIELDS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MOSTOFCONTFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MOSTOFCONTFIELDS):
    case REQUEST(ACTION_TYPES.UPDATE_MOSTOFCONTFIELDS):
    case REQUEST(ACTION_TYPES.DELETE_MOSTOFCONTFIELDS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTFIELDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_MOSTOFCONTFIELDS):
    case FAILURE(ACTION_TYPES.FETCH_MOSTOFCONTFIELDS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MOSTOFCONTFIELDS):
    case FAILURE(ACTION_TYPES.CREATE_MOSTOFCONTFIELDS):
    case FAILURE(ACTION_TYPES.UPDATE_MOSTOFCONTFIELDS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTFIELDS):
    case FAILURE(ACTION_TYPES.DELETE_MOSTOFCONTFIELDS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MOSTOFCONTFIELDS):
    case SUCCESS(ACTION_TYPES.FETCH_MOSTOFCONTFIELDS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_MOSTOFCONTFIELDS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MOSTOFCONTFIELDS):
    case SUCCESS(ACTION_TYPES.UPDATE_MOSTOFCONTFIELDS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTFIELDS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MOSTOFCONTFIELDS):
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

const apiUrl = 'api/most-of-cont-fields';
const apiSearchUrl = 'api/_search/most-of-cont-fields';

// Actions

export const getSearchEntities: ICrudSearchAction<IMostOfContFields> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MOSTOFCONTFIELDS,
  payload: axios.get<IMostOfContFields>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IMostOfContFields> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MOSTOFCONTFIELDS_LIST,
    payload: axios.get<IMostOfContFields>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IMostOfContFields> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MOSTOFCONTFIELDS,
    payload: axios.get<IMostOfContFields>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMostOfContFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MOSTOFCONTFIELDS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMostOfContFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MOSTOFCONTFIELDS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IMostOfContFields> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTFIELDS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMostOfContFields> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MOSTOFCONTFIELDS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
