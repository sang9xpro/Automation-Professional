import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SchedulerValue from './scheduler-value';
import SchedulerValueDetail from './scheduler-value-detail';
import SchedulerValueUpdate from './scheduler-value-update';
import SchedulerValueDeleteDialog from './scheduler-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SchedulerValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SchedulerValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SchedulerValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={SchedulerValue} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SchedulerValueDeleteDialog} />
  </>
);

export default Routes;
