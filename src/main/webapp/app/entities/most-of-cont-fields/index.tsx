import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MostOfContFields from './most-of-cont-fields';
import MostOfContFieldsDetail from './most-of-cont-fields-detail';
import MostOfContFieldsUpdate from './most-of-cont-fields-update';
import MostOfContFieldsDeleteDialog from './most-of-cont-fields-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MostOfContFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MostOfContFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MostOfContFieldsDetail} />
      <ErrorBoundaryRoute path={match.url} component={MostOfContFields} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MostOfContFieldsDeleteDialog} />
  </>
);

export default Routes;
