import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MostOfContent from './most-of-content';
import MostOfContentDetail from './most-of-content-detail';
import MostOfContentUpdate from './most-of-content-update';
import MostOfContentDeleteDialog from './most-of-content-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MostOfContentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MostOfContentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MostOfContentDetail} />
      <ErrorBoundaryRoute path={match.url} component={MostOfContent} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MostOfContentDeleteDialog} />
  </>
);

export default Routes;
