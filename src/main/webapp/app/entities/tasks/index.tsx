import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Tasks from './tasks';
import TasksDetail from './tasks-detail';
import TasksUpdate from './tasks-update';
import TasksDeleteDialog from './tasks-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TasksUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TasksUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TasksDetail} />
      <ErrorBoundaryRoute path={match.url} component={Tasks} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TasksDeleteDialog} />
  </>
);

export default Routes;
