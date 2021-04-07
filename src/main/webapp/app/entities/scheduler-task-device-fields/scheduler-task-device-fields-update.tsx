import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './scheduler-task-device-fields.reducer';
import { ISchedulerTaskDeviceFields } from 'app/shared/model/scheduler-task-device-fields.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISchedulerTaskDeviceFieldsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulerTaskDeviceFieldsUpdate = (props: ISchedulerTaskDeviceFieldsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { schedulerTaskDeviceFieldsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/scheduler-task-device-fields' + props.location.search);
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
    if (errors.length === 0) {
      const entity = {
        ...schedulerTaskDeviceFieldsEntity,
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
          <h2
            id="automationProfessionalApp.schedulerTaskDeviceFields.home.createOrEditLabel"
            data-cy="SchedulerTaskDeviceFieldsCreateUpdateHeading"
          >
            <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceFields.home.createOrEditLabel">
              Create or edit a SchedulerTaskDeviceFields
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : schedulerTaskDeviceFieldsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="scheduler-task-device-fields-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="scheduler-task-device-fields-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fieldNameLabel" for="scheduler-task-device-fields-fieldName">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceFields.fieldName">Field Name</Translate>
                </Label>
                <AvField id="scheduler-task-device-fields-fieldName" data-cy="fieldName" type="text" name="fieldName" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/scheduler-task-device-fields" replace color="info">
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
  schedulerTaskDeviceFieldsEntity: storeState.schedulerTaskDeviceFields.entity,
  loading: storeState.schedulerTaskDeviceFields.loading,
  updating: storeState.schedulerTaskDeviceFields.updating,
  updateSuccess: storeState.schedulerTaskDeviceFields.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerTaskDeviceFieldsUpdate);
