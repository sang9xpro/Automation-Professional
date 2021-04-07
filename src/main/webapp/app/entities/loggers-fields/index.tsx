import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LoggersFields from './loggers-fields';
import LoggersFieldsDetail from './loggers-fields-detail';
import LoggersFieldsUpdate from './loggers-fields-update';
import LoggersFieldsDeleteDialog from './loggers-fields-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LoggersFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LoggersFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LoggersFieldsDetail} />
      <ErrorBoundaryRoute path={match.url} component={LoggersFields} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LoggersFieldsDeleteDialog} />
  </>
);

export default Routes;
