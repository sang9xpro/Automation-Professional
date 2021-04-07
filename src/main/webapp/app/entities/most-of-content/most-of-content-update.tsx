import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFacebook } from 'app/shared/model/facebook.model';
import { getEntities as getFacebooks } from 'app/entities/facebook/facebook.reducer';
import { getEntity, updateEntity, createEntity, reset } from './most-of-content.reducer';
import { IMostOfContent } from 'app/shared/model/most-of-content.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMostOfContentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MostOfContentUpdate = (props: IMostOfContentUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { mostOfContentEntity, facebooks, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/most-of-content' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getFacebooks();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.postTime = convertDateTimeToServer(values.postTime);

    if (errors.length === 0) {
      const entity = {
        ...mostOfContentEntity,
        ...values,
        facebook: facebooks.find(it => it.id.toString() === values.facebookId.toString()),
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
          <h2 id="automationProfessionalApp.mostOfContent.home.createOrEditLabel" data-cy="MostOfContentCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.mostOfContent.home.createOrEditLabel">
              Create or edit a MostOfContent
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : mostOfContentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="most-of-content-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="most-of-content-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="urlOriginalLabel" for="most-of-content-urlOriginal">
                  <Translate contentKey="automationProfessionalApp.mostOfContent.urlOriginal">Url Original</Translate>
                </Label>
                <AvField id="most-of-content-urlOriginal" data-cy="urlOriginal" type="text" name="urlOriginal" />
              </AvGroup>
              <AvGroup>
                <Label id="contentTextLabel" for="most-of-content-contentText">
                  <Translate contentKey="automationProfessionalApp.mostOfContent.contentText">Content Text</Translate>
                </Label>
                <AvField id="most-of-content-contentText" data-cy="contentText" type="text" name="contentText" />
              </AvGroup>
              <AvGroup>
                <Label id="postTimeLabel" for="most-of-content-postTime">
                  <Translate contentKey="automationProfessionalApp.mostOfContent.postTime">Post Time</Translate>
                </Label>
                <AvInput
                  id="most-of-content-postTime"
                  data-cy="postTime"
                  type="datetime-local"
                  className="form-control"
                  name="postTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.mostOfContentEntity.postTime)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="numberLikeLabel" for="most-of-content-numberLike">
                  <Translate contentKey="automationProfessionalApp.mostOfContent.numberLike">Number Like</Translate>
                </Label>
                <AvField id="most-of-content-numberLike" data-cy="numberLike" type="string" className="form-control" name="numberLike" />
              </AvGroup>
              <AvGroup>
                <Label id="numberCommentLabel" for="most-of-content-numberComment">
                  <Translate contentKey="automationProfessionalApp.mostOfContent.numberComment">Number Comment</Translate>
                </Label>
                <AvField
                  id="most-of-content-numberComment"
                  data-cy="numberComment"
                  type="string"
                  className="form-control"
                  name="numberComment"
                />
              </AvGroup>
              <AvGroup>
                <Label for="most-of-content-facebook">
                  <Translate contentKey="automationProfessionalApp.mostOfContent.facebook">Facebook</Translate>
                </Label>
                <AvInput id="most-of-content-facebook" data-cy="facebook" type="select" className="form-control" name="facebookId">
                  <option value="" key="0" />
                  {facebooks
                    ? facebooks.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/most-of-content" replace color="info">
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
  facebooks: storeState.facebook.entities,
  mostOfContentEntity: storeState.mostOfContent.entity,
  loading: storeState.mostOfContent.loading,
  updating: storeState.mostOfContent.updating,
  updateSuccess: storeState.mostOfContent.updateSuccess,
});

const mapDispatchToProps = {
  getFacebooks,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MostOfContentUpdate);
