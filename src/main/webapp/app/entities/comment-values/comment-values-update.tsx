import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IComment } from 'app/shared/model/comment.model';
import { getEntities as getComments } from 'app/entities/comment/comment.reducer';
import { ICommentFields } from 'app/shared/model/comment-fields.model';
import { getEntities as getCommentFields } from 'app/entities/comment-fields/comment-fields.reducer';
import { getEntity, updateEntity, createEntity, reset } from './comment-values.reducer';
import { ICommentValues } from 'app/shared/model/comment-values.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICommentValuesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CommentValuesUpdate = (props: ICommentValuesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { commentValuesEntity, comments, commentFields, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/comment-values' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getComments();
    props.getCommentFields();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...commentValuesEntity,
        ...values,
        comment: comments.find(it => it.id.toString() === values.commentId.toString()),
        commentFields: commentFields.find(it => it.id.toString() === values.commentFieldsId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="automationProfessionalApp.commentValues.home.createOrEditLabel" data-cy="CommentValuesCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.commentValues.home.createOrEditLabel">
              Create or edit a CommentValues
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : commentValuesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="comment-values-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="comment-values-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="comment-values-value">
                  <Translate contentKey="automationProfessionalApp.commentValues.value">Value</Translate>
                </Label>
                <AvField id="comment-values-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="comment-values-comment">
                  <Translate contentKey="automationProfessionalApp.commentValues.comment">Comment</Translate>
                </Label>
                <AvInput id="comment-values-comment" data-cy="comment" type="select" className="form-control" name="commentId">
                  <option value="" key="0" />
                  {comments
                    ? comments.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="comment-values-commentFields">
                  <Translate contentKey="automationProfessionalApp.commentValues.commentFields">Comment Fields</Translate>
                </Label>
                <AvInput
                  id="comment-values-commentFields"
                  data-cy="commentFields"
                  type="select"
                  className="form-control"
                  name="commentFieldsId"
                >
                  <option value="" key="0" />
                  {commentFields
                    ? commentFields.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/comment-values" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  comments: storeState.comment.entities,
  commentFields: storeState.commentFields.entities,
  commentValuesEntity: storeState.commentValues.entity,
  loading: storeState.commentValues.loading,
  updating: storeState.commentValues.updating,
  updateSuccess: storeState.commentValues.updateSuccess,
});

const mapDispatchToProps = {
  getComments,
  getCommentFields,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CommentValuesUpdate);
