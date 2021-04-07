import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { setFileData, openFile, byteSize, Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMostOfContent } from 'app/shared/model/most-of-content.model';
import { getEntities as getMostOfContents } from 'app/entities/most-of-content/most-of-content.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './comment.reducer';
import { IComment } from 'app/shared/model/comment.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICommentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CommentUpdate = (props: ICommentUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { commentEntity, mostOfContents, loading, updating } = props;

  const { image, imageContentType } = commentEntity;

  const handleClose = () => {
    props.history.push('/comment' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getMostOfContents();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...commentEntity,
        ...values,
        mostOfContent: mostOfContents.find(it => it.id.toString() === values.mostOfContentId.toString()),
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
          <h2 id="automationProfessionalApp.comment.home.createOrEditLabel" data-cy="CommentCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.comment.home.createOrEditLabel">Create or edit a Comment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : commentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="comment-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="comment-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="contentLabel" for="comment-content">
                  <Translate contentKey="automationProfessionalApp.comment.content">Content</Translate>
                </Label>
                <AvField id="comment-content" data-cy="content" type="text" name="content" />
              </AvGroup>
              <AvGroup>
                <AvGroup>
                  <Label id="imageLabel" for="image">
                    <Translate contentKey="automationProfessionalApp.comment.image">Image</Translate>
                  </Label>
                  <br />
                  {image ? (
                    <div>
                      {imageContentType ? (
                        <a onClick={openFile(imageContentType, image)}>
                          <Translate contentKey="entity.action.open">Open</Translate>
                        </a>
                      ) : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {imageContentType}, {byteSize(image)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('image')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_image" data-cy="image" type="file" onChange={onBlobChange(false, 'image')} />
                  <AvInput type="hidden" name="image" value={image} />
                </AvGroup>
              </AvGroup>
              <AvGroup>
                <Label id="socialLabel" for="comment-social">
                  <Translate contentKey="automationProfessionalApp.comment.social">Social</Translate>
                </Label>
                <AvInput
                  id="comment-social"
                  data-cy="social"
                  type="select"
                  className="form-control"
                  name="social"
                  value={(!isNew && commentEntity.social) || 'Facebook'}
                >
                  <option value="Facebook">{translate('automationProfessionalApp.Social.Facebook')}</option>
                  <option value="Youtube">{translate('automationProfessionalApp.Social.Youtube')}</option>
                  <option value="Instagram">{translate('automationProfessionalApp.Social.Instagram')}</option>
                  <option value="Tiktok">{translate('automationProfessionalApp.Social.Tiktok')}</option>
                  <option value="Other">{translate('automationProfessionalApp.Social.Other')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="ownerLabel" for="comment-owner">
                  <Translate contentKey="automationProfessionalApp.comment.owner">Owner</Translate>
                </Label>
                <AvField id="comment-owner" data-cy="owner" type="text" name="owner" />
              </AvGroup>
              <AvGroup>
                <Label for="comment-mostOfContent">
                  <Translate contentKey="automationProfessionalApp.comment.mostOfContent">Most Of Content</Translate>
                </Label>
                <AvInput id="comment-mostOfContent" data-cy="mostOfContent" type="select" className="form-control" name="mostOfContentId">
                  <option value="" key="0" />
                  {mostOfContents
                    ? mostOfContents.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/comment" replace color="info">
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
  mostOfContents: storeState.mostOfContent.entities,
  commentEntity: storeState.comment.entity,
  loading: storeState.comment.loading,
  updating: storeState.comment.updating,
  updateSuccess: storeState.comment.updateSuccess,
});

const mapDispatchToProps = {
  getMostOfContents,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CommentUpdate);
