import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SchedulerTaskDeviceFields from './scheduler-task-device-fields';
import SchedulerTaskDeviceFieldsDetail from './scheduler-task-device-fields-detail';
import SchedulerTaskDeviceFieldsUpdate from './scheduler-task-device-fields-update';
import SchedulerTaskDeviceFieldsDeleteDialog from './scheduler-task-device-fields-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SchedulerTaskDeviceFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SchedulerTaskDeviceFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SchedulerTaskDeviceFieldsDetail} />
      <ErrorBoundaryRoute path={match.url} component={SchedulerTaskDeviceFields} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SchedulerTaskDeviceFieldsDeleteDialog} />
  </>
);

export default Routes;
