import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './schedulers.reducer';
import { ISchedulers } from 'app/shared/model/schedulers.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISchedulersUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulersUpdate = (props: ISchedulersUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { schedulersEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/schedulers' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.lastUpdate = convertDateTimeToServer(values.lastUpdate);

    if (errors.length === 0) {
      const entity = {
        ...schedulersEntity,
        ...values,
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
          <h2 id="automationProfessionalApp.schedulers.home.createOrEditLabel" data-cy="SchedulersCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.schedulers.home.createOrEditLabel">Create or edit a Schedulers</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : schedulersEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="schedulers-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="schedulers-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="urlLabel" for="schedulers-url">
                  <Translate contentKey="automationProfessionalApp.schedulers.url">Url</Translate>
                </Label>
                <AvField id="schedulers-url" data-cy="url" type="text" name="url" />
              </AvGroup>
              <AvGroup>
                <Label id="titleLabel" for="schedulers-title">
                  <Translate contentKey="automationProfessionalApp.schedulers.title">Title</Translate>
                </Label>
                <AvField id="schedulers-title" data-cy="title" type="text" name="title" />
              </AvGroup>
              <AvGroup>
                <Label id="otherSourceLabel" for="schedulers-otherSource">
                  <Translate contentKey="automationProfessionalApp.schedulers.otherSource">Other Source</Translate>
                </Label>
                <AvField id="schedulers-otherSource" data-cy="otherSource" type="text" name="otherSource" />
              </AvGroup>
              <AvGroup>
                <Label id="countLabel" for="schedulers-count">
                  <Translate contentKey="automationProfessionalApp.schedulers.count">Count</Translate>
                </Label>
                <AvField id="schedulers-count" data-cy="count" type="string" className="form-control" name="count" />
              </AvGroup>
              <AvGroup>
                <Label id="lastUpdateLabel" for="schedulers-lastUpdate">
                  <Translate contentKey="automationProfessionalApp.schedulers.lastUpdate">Last Update</Translate>
                </Label>
                <AvInput
                  id="schedulers-lastUpdate"
                  data-cy="lastUpdate"
                  type="datetime-local"
                  className="form-control"
                  name="lastUpdate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.schedulersEntity.lastUpdate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="ownerLabel" for="schedulers-owner">
                  <Translate contentKey="automationProfessionalApp.schedulers.owner">Owner</Translate>
                </Label>
                <AvField id="schedulers-owner" data-cy="owner" type="text" name="owner" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="schedulers-status">
                  <Translate contentKey="automationProfessionalApp.schedulers.status">Status</Translate>
                </Label>
                <AvInput
                  id="schedulers-status"
                  data-cy="status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && schedulersEntity.status) || 'Open'}
                >
                  <option value="Open">{translate('automationProfessionalApp.SchedulerStatus.Open')}</option>
                  <option value="Processing">{translate('automationProfessionalApp.SchedulerStatus.Processing')}</option>
                  <option value="Waiting">{translate('automationProfessionalApp.SchedulerStatus.Waiting')}</option>
                  <option value="Completed">{translate('automationProfessionalApp.SchedulerStatus.Completed')}</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/schedulers" replace color="info">
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
  schedulersEntity: storeState.schedulers.entity,
  loading: storeState.schedulers.loading,
  updating: storeState.schedulers.updating,
  updateSuccess: storeState.schedulers.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulersUpdate);
