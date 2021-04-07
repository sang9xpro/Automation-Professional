import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MostOfContValues from './most-of-cont-values';
import MostOfContValuesDetail from './most-of-cont-values-detail';
import MostOfContValuesUpdate from './most-of-cont-values-update';
import MostOfContValuesDeleteDialog from './most-of-cont-values-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MostOfContValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MostOfContValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MostOfContValuesDetail} />
      <ErrorBoundaryRoute path={match.url} component={MostOfContValues} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MostOfContValuesDeleteDialog} />
  </>
);

export default Routes;
