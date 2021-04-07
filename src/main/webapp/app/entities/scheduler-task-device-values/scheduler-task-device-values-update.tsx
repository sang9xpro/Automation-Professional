import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISchedulerTaskDevice } from 'app/shared/model/scheduler-task-device.model';
import { getEntities as getSchedulerTaskDevices } from 'app/entities/scheduler-task-device/scheduler-task-device.reducer';
import { ISchedulerTaskDeviceFields } from 'app/shared/model/scheduler-task-device-fields.model';
import { getEntities as getSchedulerTaskDeviceFields } from 'app/entities/scheduler-task-device-fields/scheduler-task-device-fields.reducer';
import { getEntity, updateEntity, createEntity, reset } from './scheduler-task-device-values.reducer';
import { ISchedulerTaskDeviceValues } from 'app/shared/model/scheduler-task-device-values.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISchedulerTaskDeviceValuesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulerTaskDeviceValuesUpdate = (props: ISchedulerTaskDeviceValuesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { schedulerTaskDeviceValuesEntity, schedulerTaskDevices, schedulerTaskDeviceFields, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/scheduler-task-device-values' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getSchedulerTaskDevices();
    props.getSchedulerTaskDeviceFields();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...schedulerTaskDeviceValuesEntity,
        ...values,
        schedulerTaskDevice: schedulerTaskDevices.find(it => it.id.toString() === values.schedulerTaskDeviceId.toString()),
        schedulerTaskDeviceFields: schedulerTaskDeviceFields.find(it => it.id.toString() === values.schedulerTaskDeviceFieldsId.toString()),
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
            id="automationProfessionalApp.schedulerTaskDeviceValues.home.createOrEditLabel"
            data-cy="SchedulerTaskDeviceValuesCreateUpdateHeading"
          >
            <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceValues.home.createOrEditLabel">
              Create or edit a SchedulerTaskDeviceValues
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : schedulerTaskDeviceValuesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="scheduler-task-device-values-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="scheduler-task-device-values-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="scheduler-task-device-values-value">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceValues.value">Value</Translate>
                </Label>
                <AvField id="scheduler-task-device-values-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="scheduler-task-device-values-schedulerTaskDevice">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceValues.schedulerTaskDevice">
                    Scheduler Task Device
                  </Translate>
                </Label>
                <AvInput
                  id="scheduler-task-device-values-schedulerTaskDevice"
                  data-cy="schedulerTaskDevice"
                  type="select"
                  className="form-control"
                  name="schedulerTaskDeviceId"
                >
                  <option value="" key="0" />
                  {schedulerTaskDevices
                    ? schedulerTaskDevices.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="scheduler-task-device-values-schedulerTaskDeviceFields">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceValues.schedulerTaskDeviceFields">
                    Scheduler Task Device Fields
                  </Translate>
                </Label>
                <AvInput
                  id="scheduler-task-device-values-schedulerTaskDeviceFields"
                  data-cy="schedulerTaskDeviceFields"
                  type="select"
                  className="form-control"
                  name="schedulerTaskDeviceFieldsId"
                >
                  <option value="" key="0" />
                  {schedulerTaskDeviceFields
                    ? schedulerTaskDeviceFields.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/scheduler-task-device-values" replace color="info">
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
  schedulerTaskDevices: storeState.schedulerTaskDevice.entities,
  schedulerTaskDeviceFields: storeState.schedulerTaskDeviceFields.entities,
  schedulerTaskDeviceValuesEntity: storeState.schedulerTaskDeviceValues.entity,
  loading: storeState.schedulerTaskDeviceValues.loading,
  updating: storeState.schedulerTaskDeviceValues.updating,
  updateSuccess: storeState.schedulerTaskDeviceValues.updateSuccess,
});

const mapDispatchToProps = {
  getSchedulerTaskDevices,
  getSchedulerTaskDeviceFields,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerTaskDeviceValuesUpdate);
