import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SchedulerTaskDevice from './scheduler-task-device';
import SchedulerTaskDeviceDetail from './scheduler-task-device-detail';
import SchedulerTaskDeviceUpdate from './scheduler-task-device-update';
import SchedulerTaskDeviceDeleteDialog from './scheduler-task-device-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SchedulerTaskDeviceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SchedulerTaskDeviceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SchedulerTaskDeviceDetail} />
      <ErrorBoundaryRoute path={match.url} component={SchedulerTaskDevice} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SchedulerTaskDeviceDeleteDialog} />
  </>
);

export default Routes;
