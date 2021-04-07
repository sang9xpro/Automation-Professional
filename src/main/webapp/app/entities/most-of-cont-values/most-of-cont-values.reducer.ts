import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMostOfContValues, defaultValue } from 'app/shared/model/most-of-cont-values.model';

export const ACTION_TYPES = {
  SEARCH_MOSTOFCONTVALUES: 'mostOfContValues/SEARCH_MOSTOFCONTVALUES',
  FETCH_MOSTOFCONTVALUES_LIST: 'mostOfContValues/FETCH_MOSTOFCONTVALUES_LIST',
  FETCH_MOSTOFCONTVALUES: 'mostOfContValues/FETCH_MOSTOFCONTVALUES',
  CREATE_MOSTOFCONTVALUES: 'mostOfContValues/CREATE_MOSTOFCONTVALUES',
  UPDATE_MOSTOFCONTVALUES: 'mostOfContValues/UPDATE_MOSTOFCONTVALUES',
  PARTIAL_UPDATE_MOSTOFCONTVALUES: 'mostOfContValues/PARTIAL_UPDATE_MOSTOFCONTVALUES',
  DELETE_MOSTOFCONTVALUES: 'mostOfContValues/DELETE_MOSTOFCONTVALUES',
  RESET: 'mostOfContValues/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMostOfContValues>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type MostOfContValuesState = Readonly<typeof initialState>;

// Reducer

export default (state: MostOfContValuesState = initialState, action): MostOfContValuesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MOSTOFCONTVALUES):
    case REQUEST(ACTION_TYPES.FETCH_MOSTOFCONTVALUES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MOSTOFCONTVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MOSTOFCONTVALUES):
    case REQUEST(ACTION_TYPES.UPDATE_MOSTOFCONTVALUES):
    case REQUEST(ACTION_TYPES.DELETE_MOSTOFCONTVALUES):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTVALUES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_MOSTOFCONTVALUES):
    case FAILURE(ACTION_TYPES.FETCH_MOSTOFCONTVALUES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MOSTOFCONTVALUES):
    case FAILURE(ACTION_TYPES.CREATE_MOSTOFCONTVALUES):
    case FAILURE(ACTION_TYPES.UPDATE_MOSTOFCONTVALUES):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTVALUES):
    case FAILURE(ACTION_TYPES.DELETE_MOSTOFCONTVALUES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MOSTOFCONTVALUES):
    case SUCCESS(ACTION_TYPES.FETCH_MOSTOFCONTVALUES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_MOSTOFCONTVALUES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MOSTOFCONTVALUES):
    case SUCCESS(ACTION_TYPES.UPDATE_MOSTOFCONTVALUES):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTVALUES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MOSTOFCONTVALUES):
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

const apiUrl = 'api/most-of-cont-values';
const apiSearchUrl = 'api/_search/most-of-cont-values';

// Actions

export const getSearchEntities: ICrudSearchAction<IMostOfContValues> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MOSTOFCONTVALUES,
  payload: axios.get<IMostOfContValues>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IMostOfContValues> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MOSTOFCONTVALUES_LIST,
    payload: axios.get<IMostOfContValues>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IMostOfContValues> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MOSTOFCONTVALUES,
    payload: axios.get<IMostOfContValues>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMostOfContValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MOSTOFCONTVALUES,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMostOfContValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MOSTOFCONTVALUES,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IMostOfContValues> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTVALUES,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMostOfContValues> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MOSTOFCONTVALUES,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
