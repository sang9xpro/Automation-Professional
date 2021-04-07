import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Accounts from './accounts';
import AccountsDetail from './accounts-detail';
import AccountsUpdate from './accounts-update';
import AccountsDeleteDialog from './accounts-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AccountsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AccountsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AccountsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Accounts} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AccountsDeleteDialog} />
  </>
);

export default Routes;
