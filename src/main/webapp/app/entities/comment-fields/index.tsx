import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CommentFields from './comment-fields';
import CommentFieldsDetail from './comment-fields-detail';
import CommentFieldsUpdate from './comment-fields-update';
import CommentFieldsDeleteDialog from './comment-fields-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CommentFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CommentFieldsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CommentFieldsDetail} />
      <ErrorBoundaryRoute path={match.url} component={CommentFields} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CommentFieldsDeleteDialog} />
  </>
);

export default Routes;
