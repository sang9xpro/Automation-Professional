import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Schedulers from './schedulers';
import SchedulersDetail from './schedulers-detail';
import SchedulersUpdate from './schedulers-update';
import SchedulersDeleteDialog from './schedulers-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SchedulersUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SchedulersUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SchedulersDetail} />
      <ErrorBoundaryRoute path={match.url} component={Schedulers} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SchedulersDeleteDialog} />
  </>
);

export default Routes;
