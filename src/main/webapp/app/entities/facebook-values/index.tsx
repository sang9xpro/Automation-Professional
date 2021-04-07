import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FacebookValues from './facebook-values';
import FacebookValuesDetail from './facebook-values-detail';
import FacebookValuesUpdate from './facebook-values-update';
import FacebookValuesDeleteDialog from './facebook-values-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FacebookValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FacebookValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FacebookValuesDetail} />
      <ErrorBoundaryRoute path={match.url} component={FacebookValues} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FacebookValuesDeleteDialog} />
  </>
);

export default Routes;
