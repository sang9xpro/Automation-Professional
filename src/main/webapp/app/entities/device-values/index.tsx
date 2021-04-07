import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DeviceValues from './device-values';
import DeviceValuesDetail from './device-values-detail';
import DeviceValuesUpdate from './device-values-update';
import DeviceValuesDeleteDialog from './device-values-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DeviceValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DeviceValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DeviceValuesDetail} />
      <ErrorBoundaryRoute path={match.url} component={DeviceValues} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DeviceValuesDeleteDialog} />
  </>
);

export default Routes;
