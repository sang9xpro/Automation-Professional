import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HistoryValues from './history-values';
import HistoryValuesDetail from './history-values-detail';
import HistoryValuesUpdate from './history-values-update';
import HistoryValuesDeleteDialog from './history-values-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HistoryValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HistoryValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HistoryValuesDetail} />
      <ErrorBoundaryRoute path={match.url} component={HistoryValues} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HistoryValuesDeleteDialog} />
  </>
);

export default Routes;
