import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SchedulerTaskDeviceValues from './scheduler-task-device-values';
import SchedulerTaskDeviceValuesDetail from './scheduler-task-device-values-detail';
import SchedulerTaskDeviceValuesUpdate from './scheduler-task-device-values-update';
import SchedulerTaskDeviceValuesDeleteDialog from './scheduler-task-device-values-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SchedulerTaskDeviceValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SchedulerTaskDeviceValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SchedulerTaskDeviceValuesDetail} />
      <ErrorBoundaryRoute path={match.url} component={SchedulerTaskDeviceValues} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SchedulerTaskDeviceValuesDeleteDialog} />
  </>
);

export default Routes;
