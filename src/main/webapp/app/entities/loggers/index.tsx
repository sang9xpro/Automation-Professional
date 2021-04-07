import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Loggers from './loggers';
import LoggersDetail from './loggers-detail';
import LoggersUpdate from './loggers-update';
import LoggersDeleteDialog from './loggers-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LoggersUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LoggersUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LoggersDetail} />
      <ErrorBoundaryRoute path={match.url} component={Loggers} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LoggersDeleteDialog} />
  </>
);

export default Routes;
