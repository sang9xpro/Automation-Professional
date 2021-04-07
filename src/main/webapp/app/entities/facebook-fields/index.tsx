import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FacebookFields from './facebook-fields';
import FacebookFieldsDetail from './facebook-fields-detail';
import FacebookFieldsUpdate from './facebook-fields-update';
import FacebookFieldsDeleteDialog from './facebook-fields-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FacebookFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FacebookFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FacebookFieldsDetail} />
      <ErrorBoundaryRoute path={match.url} component={FacebookFields} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FacebookFieldsDeleteDialog} />
  </>
);

export default Routes;
