import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TaskValues from './task-values';
import TaskValuesDetail from './task-values-detail';
import TaskValuesUpdate from './task-values-update';
import TaskValuesDeleteDialog from './task-values-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TaskValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TaskValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TaskValuesDetail} />
      <ErrorBoundaryRoute path={match.url} component={TaskValues} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TaskValuesDeleteDialog} />
  </>
);

export default Routes;
