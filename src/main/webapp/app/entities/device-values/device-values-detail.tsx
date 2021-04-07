import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './device-values.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDeviceValuesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DeviceValuesDetail = (props: IDeviceValuesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { deviceValuesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="deviceValuesDetailsHeading">
          <Translate contentKey="automationProfessionalApp.deviceValues.detail.title">DeviceValues</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{deviceValuesEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="automationProfessionalApp.deviceValues.value">Value</Translate>
            </span>
          </dt>
          <dd>{deviceValuesEntity.value}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.deviceValues.devices">Devices</Translate>
          </dt>
          <dd>{deviceValuesEntity.devices ? deviceValuesEntity.devices.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.deviceValues.devicesFields">Devices Fields</Translate>
          </dt>
          <dd>{deviceValuesEntity.devicesFields ? deviceValuesEntity.devicesFields.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/device-values" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/device-values/${deviceValuesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ deviceValues }: IRootState) => ({
  deviceValuesEntity: deviceValues.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DeviceValuesDetail);
