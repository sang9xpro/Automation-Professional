import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HistoryFields from './history-fields';
import HistoryFieldsDetail from './history-fields-detail';
import HistoryFieldsUpdate from './history-fields-update';
import HistoryFieldsDeleteDialog from './history-fields-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HistoryFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HistoryFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HistoryFieldsDetail} />
      <ErrorBoundaryRoute path={match.url} component={HistoryFields} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HistoryFieldsDeleteDialog} />
  </>
);

export default Routes;
