import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LoggersValues from './loggers-values';
import LoggersValuesDetail from './loggers-values-detail';
import LoggersValuesUpdate from './loggers-values-update';
import LoggersValuesDeleteDialog from './loggers-values-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LoggersValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LoggersValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LoggersValuesDetail} />
      <ErrorBoundaryRoute path={match.url} component={LoggersValues} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LoggersValuesDeleteDialog} />
  </>
);

export default Routes;
