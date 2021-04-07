import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AccountFields from './account-fields';
import AccountFieldsDetail from './account-fields-detail';
import AccountFieldsUpdate from './account-fields-update';
import AccountFieldsDeleteDialog from './account-fields-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AccountFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AccountFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AccountFieldsDetail} />
      <ErrorBoundaryRoute path={match.url} component={AccountFields} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AccountFieldsDeleteDialog} />
  </>
);

export default Routes;
