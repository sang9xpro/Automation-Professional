import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AccountValues from './account-values';
import AccountValuesDetail from './account-values-detail';
import AccountValuesUpdate from './account-values-update';
import AccountValuesDeleteDialog from './account-values-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AccountValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AccountValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AccountValuesDetail} />
      <ErrorBoundaryRoute path={match.url} component={AccountValues} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AccountValuesDeleteDialog} />
  </>
);

export default Routes;
