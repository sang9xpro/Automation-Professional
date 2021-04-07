import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './devices.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDevicesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DevicesDetail = (props: IDevicesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { devicesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="devicesDetailsHeading">
          <Translate contentKey="automationProfessionalApp.devices.detail.title">Devices</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{devicesEntity.id}</dd>
          <dt>
            <span id="ipAddress">
              <Translate contentKey="automationProfessionalApp.devices.ipAddress">Ip Address</Translate>
            </span>
          </dt>
          <dd>{devicesEntity.ipAddress}</dd>
          <dt>
            <span id="macAddress">
              <Translate contentKey="automationProfessionalApp.devices.macAddress">Mac Address</Translate>
            </span>
          </dt>
          <dd>{devicesEntity.macAddress}</dd>
          <dt>
            <span id="os">
              <Translate contentKey="automationProfessionalApp.devices.os">Os</Translate>
            </span>
          </dt>
          <dd>{devicesEntity.os}</dd>
          <dt>
            <span id="deviceType">
              <Translate contentKey="automationProfessionalApp.devices.deviceType">Device Type</Translate>
            </span>
          </dt>
          <dd>{devicesEntity.deviceType}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="automationProfessionalApp.devices.status">Status</Translate>
            </span>
          </dt>
          <dd>{devicesEntity.status}</dd>
          <dt>
            <span id="lastUpdate">
              <Translate contentKey="automationProfessionalApp.devices.lastUpdate">Last Update</Translate>
            </span>
          </dt>
          <dd>{devicesEntity.lastUpdate ? <TextFormat value={devicesEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="owner">
              <Translate contentKey="automationProfessionalApp.devices.owner">Owner</Translate>
            </span>
          </dt>
          <dd>{devicesEntity.owner}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.devices.country">Country</Translate>
          </dt>
          <dd>{devicesEntity.country ? devicesEntity.country.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.devices.accounts">Accounts</Translate>
          </dt>
          <dd>
            {devicesEntity.accounts
              ? devicesEntity.accounts.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {devicesEntity.accounts && i === devicesEntity.accounts.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.devices.schedulerTaskDevice">Scheduler Task Device</Translate>
          </dt>
          <dd>
            {devicesEntity.schedulerTaskDevices
              ? devicesEntity.schedulerTaskDevices.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {devicesEntity.schedulerTaskDevices && i === devicesEntity.schedulerTaskDevices.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/devices" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/devices/${devicesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ devices }: IRootState) => ({
  devicesEntity: devices.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DevicesDetail);
