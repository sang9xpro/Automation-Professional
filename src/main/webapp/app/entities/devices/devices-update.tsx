import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { IAccounts } from 'app/shared/model/accounts.model';
import { getEntities as getAccounts } from 'app/entities/accounts/accounts.reducer';
import { ISchedulerTaskDevice } from 'app/shared/model/scheduler-task-device.model';
import { getEntities as getSchedulerTaskDevices } from 'app/entities/scheduler-task-device/scheduler-task-device.reducer';
import { getEntity, updateEntity, createEntity, reset } from './devices.reducer';
import { IDevices } from 'app/shared/model/devices.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDevicesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DevicesUpdate = (props: IDevicesUpdateProps) => {
  const [idsaccounts, setIdsaccounts] = useState([]);
  const [idsschedulerTaskDevice, setIdsschedulerTaskDevice] = useState([]);
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { devicesEntity, countries, accounts, schedulerTaskDevices, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/devices');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getCountries();
    props.getAccounts();
    props.getSchedulerTaskDevices();
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
        ...devicesEntity,
        ...values,
        accounts: mapIdList(values.accounts),
        schedulerTaskDevices: mapIdList(values.schedulerTaskDevices),
        country: countries.find(it => it.id.toString() === values.countryId.toString()),
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
          <h2 id="automationProfessionalApp.devices.home.createOrEditLabel" data-cy="DevicesCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.devices.home.createOrEditLabel">Create or edit a Devices</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : devicesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="devices-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="devices-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="ipAddressLabel" for="devices-ipAddress">
                  <Translate contentKey="automationProfessionalApp.devices.ipAddress">Ip Address</Translate>
                </Label>
                <AvField id="devices-ipAddress" data-cy="ipAddress" type="text" name="ipAddress" />
              </AvGroup>
              <AvGroup>
                <Label id="macAddressLabel" for="devices-macAddress">
                  <Translate contentKey="automationProfessionalApp.devices.macAddress">Mac Address</Translate>
                </Label>
                <AvField id="devices-macAddress" data-cy="macAddress" type="text" name="macAddress" />
              </AvGroup>
              <AvGroup>
                <Label id="osLabel" for="devices-os">
                  <Translate contentKey="automationProfessionalApp.devices.os">Os</Translate>
                </Label>
                <AvField id="devices-os" data-cy="os" type="text" name="os" />
              </AvGroup>
              <AvGroup>
                <Label id="deviceTypeLabel" for="devices-deviceType">
                  <Translate contentKey="automationProfessionalApp.devices.deviceType">Device Type</Translate>
                </Label>
                <AvInput
                  id="devices-deviceType"
                  data-cy="deviceType"
                  type="select"
                  className="form-control"
                  name="deviceType"
                  value={(!isNew && devicesEntity.deviceType) || 'MOBILE'}
                >
                  <option value="MOBILE">{translate('automationProfessionalApp.DeviceType.MOBILE')}</option>
                  <option value="COMPUTER">{translate('automationProfessionalApp.DeviceType.COMPUTER')}</option>
                  <option value="TABLET">{translate('automationProfessionalApp.DeviceType.TABLET')}</option>
                  <option value="VPS">{translate('automationProfessionalApp.DeviceType.VPS')}</option>
                  <option value="VMW">{translate('automationProfessionalApp.DeviceType.VMW')}</option>
                  <option value="ELUMATOR">{translate('automationProfessionalApp.DeviceType.ELUMATOR')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="devices-status">
                  <Translate contentKey="automationProfessionalApp.devices.status">Status</Translate>
                </Label>
                <AvInput
                  id="devices-status"
                  data-cy="status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && devicesEntity.status) || 'Online'}
                >
                  <option value="Online">{translate('automationProfessionalApp.DeviceStatus.Online')}</option>
                  <option value="Offline">{translate('automationProfessionalApp.DeviceStatus.Offline')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="lastUpdateLabel" for="devices-lastUpdate">
                  <Translate contentKey="automationProfessionalApp.devices.lastUpdate">Last Update</Translate>
                </Label>
                <AvInput
                  id="devices-lastUpdate"
                  data-cy="lastUpdate"
                  type="datetime-local"
                  className="form-control"
                  name="lastUpdate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.devicesEntity.lastUpdate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="ownerLabel" for="devices-owner">
                  <Translate contentKey="automationProfessionalApp.devices.owner">Owner</Translate>
                </Label>
                <AvField id="devices-owner" data-cy="owner" type="text" name="owner" />
              </AvGroup>
              <AvGroup>
                <Label for="devices-country">
                  <Translate contentKey="automationProfessionalApp.devices.country">Country</Translate>
                </Label>
                <AvInput id="devices-country" data-cy="country" type="select" className="form-control" name="countryId">
                  <option value="" key="0" />
                  {countries
                    ? countries.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="devices-accounts">
                  <Translate contentKey="automationProfessionalApp.devices.accounts">Accounts</Translate>
                </Label>
                <AvInput
                  id="devices-accounts"
                  data-cy="accounts"
                  type="select"
                  multiple
                  className="form-control"
                  name="accounts"
                  value={!isNew && devicesEntity.accounts && devicesEntity.accounts.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {accounts
                    ? accounts.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="devices-schedulerTaskDevice">
                  <Translate contentKey="automationProfessionalApp.devices.schedulerTaskDevice">Scheduler Task Device</Translate>
                </Label>
                <AvInput
                  id="devices-schedulerTaskDevice"
                  data-cy="schedulerTaskDevice"
                  type="select"
                  multiple
                  className="form-control"
                  name="schedulerTaskDevices"
                  value={!isNew && devicesEntity.schedulerTaskDevices && devicesEntity.schedulerTaskDevices.map(e => e.id)}
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
              <Button tag={Link} id="cancel-save" to="/devices" replace color="info">
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
  countries: storeState.country.entities,
  accounts: storeState.accounts.entities,
  schedulerTaskDevices: storeState.schedulerTaskDevice.entities,
  devicesEntity: storeState.devices.entity,
  loading: storeState.devices.loading,
  updating: storeState.devices.updating,
  updateSuccess: storeState.devices.updateSuccess,
});

const mapDispatchToProps = {
  getCountries,
  getAccounts,
  getSchedulerTaskDevices,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DevicesUpdate);
