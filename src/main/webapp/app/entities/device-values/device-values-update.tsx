import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDevices } from 'app/shared/model/devices.model';
import { getEntities as getDevices } from 'app/entities/devices/devices.reducer';
import { IDevicesFields } from 'app/shared/model/devices-fields.model';
import { getEntities as getDevicesFields } from 'app/entities/devices-fields/devices-fields.reducer';
import { getEntity, updateEntity, createEntity, reset } from './device-values.reducer';
import { IDeviceValues } from 'app/shared/model/device-values.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDeviceValuesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DeviceValuesUpdate = (props: IDeviceValuesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { deviceValuesEntity, devices, devicesFields, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/device-values' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getDevices();
    props.getDevicesFields();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...deviceValuesEntity,
        ...values,
        devices: devices.find(it => it.id.toString() === values.devicesId.toString()),
        devicesFields: devicesFields.find(it => it.id.toString() === values.devicesFieldsId.toString()),
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
          <h2 id="automationProfessionalApp.deviceValues.home.createOrEditLabel" data-cy="DeviceValuesCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.deviceValues.home.createOrEditLabel">Create or edit a DeviceValues</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : deviceValuesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="device-values-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="device-values-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="device-values-value">
                  <Translate contentKey="automationProfessionalApp.deviceValues.value">Value</Translate>
                </Label>
                <AvField id="device-values-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="device-values-devices">
                  <Translate contentKey="automationProfessionalApp.deviceValues.devices">Devices</Translate>
                </Label>
                <AvInput id="device-values-devices" data-cy="devices" type="select" className="form-control" name="devicesId">
                  <option value="" key="0" />
                  {devices
                    ? devices.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="device-values-devicesFields">
                  <Translate contentKey="automationProfessionalApp.deviceValues.devicesFields">Devices Fields</Translate>
                </Label>
                <AvInput
                  id="device-values-devicesFields"
                  data-cy="devicesFields"
                  type="select"
                  className="form-control"
                  name="devicesFieldsId"
                >
                  <option value="" key="0" />
                  {devicesFields
                    ? devicesFields.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/device-values" replace color="info">
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
  devices: storeState.devices.entities,
  devicesFields: storeState.devicesFields.entities,
  deviceValuesEntity: storeState.deviceValues.entity,
  loading: storeState.deviceValues.loading,
  updating: storeState.deviceValues.updating,
  updateSuccess: storeState.deviceValues.updateSuccess,
});

const mapDispatchToProps = {
  getDevices,
  getDevicesFields,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DeviceValuesUpdate);
