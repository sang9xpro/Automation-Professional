import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DevicesFields from './devices-fields';
import DevicesFieldsDetail from './devices-fields-detail';
import DevicesFieldsUpdate from './devices-fields-update';
import DevicesFieldsDeleteDialog from './devices-fields-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DevicesFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DevicesFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DevicesFieldsDetail} />
      <ErrorBoundaryRoute path={match.url} component={DevicesFields} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DevicesFieldsDeleteDialog} />
  </>
);

export default Routes;
