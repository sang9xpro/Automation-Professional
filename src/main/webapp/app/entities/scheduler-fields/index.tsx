import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SchedulerFields from './scheduler-fields';
import SchedulerFieldsDetail from './scheduler-fields-detail';
import SchedulerFieldsUpdate from './scheduler-fields-update';
import SchedulerFieldsDeleteDialog from './scheduler-fields-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SchedulerFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SchedulerFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SchedulerFieldsDetail} />
      <ErrorBoundaryRoute path={match.url} component={SchedulerFields} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SchedulerFieldsDeleteDialog} />
  </>
);

export default Routes;
