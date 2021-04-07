import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMostOfContent, defaultValue } from 'app/shared/model/most-of-content.model';

export const ACTION_TYPES = {
  SEARCH_MOSTOFCONTENTS: 'mostOfContent/SEARCH_MOSTOFCONTENTS',
  FETCH_MOSTOFCONTENT_LIST: 'mostOfContent/FETCH_MOSTOFCONTENT_LIST',
  FETCH_MOSTOFCONTENT: 'mostOfContent/FETCH_MOSTOFCONTENT',
  CREATE_MOSTOFCONTENT: 'mostOfContent/CREATE_MOSTOFCONTENT',
  UPDATE_MOSTOFCONTENT: 'mostOfContent/UPDATE_MOSTOFCONTENT',
  PARTIAL_UPDATE_MOSTOFCONTENT: 'mostOfContent/PARTIAL_UPDATE_MOSTOFCONTENT',
  DELETE_MOSTOFCONTENT: 'mostOfContent/DELETE_MOSTOFCONTENT',
  RESET: 'mostOfContent/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMostOfContent>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type MostOfContentState = Readonly<typeof initialState>;

// Reducer

export default (state: MostOfContentState = initialState, action): MostOfContentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MOSTOFCONTENTS):
    case REQUEST(ACTION_TYPES.FETCH_MOSTOFCONTENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MOSTOFCONTENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MOSTOFCONTENT):
    case REQUEST(ACTION_TYPES.UPDATE_MOSTOFCONTENT):
    case REQUEST(ACTION_TYPES.DELETE_MOSTOFCONTENT):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_MOSTOFCONTENTS):
    case FAILURE(ACTION_TYPES.FETCH_MOSTOFCONTENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MOSTOFCONTENT):
    case FAILURE(ACTION_TYPES.CREATE_MOSTOFCONTENT):
    case FAILURE(ACTION_TYPES.UPDATE_MOSTOFCONTENT):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTENT):
    case FAILURE(ACTION_TYPES.DELETE_MOSTOFCONTENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MOSTOFCONTENTS):
    case SUCCESS(ACTION_TYPES.FETCH_MOSTOFCONTENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_MOSTOFCONTENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MOSTOFCONTENT):
    case SUCCESS(ACTION_TYPES.UPDATE_MOSTOFCONTENT):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MOSTOFCONTENT):
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

const apiUrl = 'api/most-of-contents';
const apiSearchUrl = 'api/_search/most-of-contents';

// Actions

export const getSearchEntities: ICrudSearchAction<IMostOfContent> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MOSTOFCONTENTS,
  payload: axios.get<IMostOfContent>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IMostOfContent> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MOSTOFCONTENT_LIST,
    payload: axios.get<IMostOfContent>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IMostOfContent> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MOSTOFCONTENT,
    payload: axios.get<IMostOfContent>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMostOfContent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MOSTOFCONTENT,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMostOfContent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MOSTOFCONTENT,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IMostOfContent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_MOSTOFCONTENT,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMostOfContent> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MOSTOFCONTENT,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
