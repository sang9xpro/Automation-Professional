import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TaskFields from './task-fields';
import TaskFieldsDetail from './task-fields-detail';
import TaskFieldsUpdate from './task-fields-update';
import TaskFieldsDeleteDialog from './task-fields-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TaskFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TaskFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TaskFieldsDetail} />
      <ErrorBoundaryRoute path={match.url} component={TaskFields} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TaskFieldsDeleteDialog} />
  </>
);

export default Routes;
