import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CommentValues from './comment-values';
import CommentValuesDetail from './comment-values-detail';
import CommentValuesUpdate from './comment-values-update';
import CommentValuesDeleteDialog from './comment-values-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CommentValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CommentValuesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CommentValuesDetail} />
      <ErrorBoundaryRoute path={match.url} component={CommentValues} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CommentValuesDeleteDialog} />
  </>
);

export default Routes;
