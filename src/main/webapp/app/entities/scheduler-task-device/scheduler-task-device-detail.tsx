import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './scheduler-task-device.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISchedulerTaskDeviceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulerTaskDeviceDetail = (props: ISchedulerTaskDeviceDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { schedulerTaskDeviceEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="schedulerTaskDeviceDetailsHeading">
          <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.detail.title">SchedulerTaskDevice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{schedulerTaskDeviceEntity.id}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>
            {schedulerTaskDeviceEntity.startTime ? (
              <TextFormat value={schedulerTaskDeviceEntity.startTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>
            {schedulerTaskDeviceEntity.endTime ? (
              <TextFormat value={schedulerTaskDeviceEntity.endTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="countFrom">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.countFrom">Count From</Translate>
            </span>
          </dt>
          <dd>{schedulerTaskDeviceEntity.countFrom}</dd>
          <dt>
            <span id="countTo">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.countTo">Count To</Translate>
            </span>
          </dt>
          <dd>{schedulerTaskDeviceEntity.countTo}</dd>
          <dt>
            <span id="point">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.point">Point</Translate>
            </span>
          </dt>
          <dd>{schedulerTaskDeviceEntity.point}</dd>
          <dt>
            <span id="lastUpdate">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.lastUpdate">Last Update</Translate>
            </span>
          </dt>
          <dd>
            {schedulerTaskDeviceEntity.lastUpdate ? (
              <TextFormat value={schedulerTaskDeviceEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="owner">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.owner">Owner</Translate>
            </span>
          </dt>
          <dd>{schedulerTaskDeviceEntity.owner}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.status">Status</Translate>
            </span>
          </dt>
          <dd>{schedulerTaskDeviceEntity.status}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.schedulers">Schedulers</Translate>
          </dt>
          <dd>{schedulerTaskDeviceEntity.schedulers ? schedulerTaskDeviceEntity.schedulers.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.tasks">Tasks</Translate>
          </dt>
          <dd>{schedulerTaskDeviceEntity.tasks ? schedulerTaskDeviceEntity.tasks.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/scheduler-task-device" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/scheduler-task-device/${schedulerTaskDeviceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ schedulerTaskDevice }: IRootState) => ({
  schedulerTaskDeviceEntity: schedulerTaskDevice.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerTaskDeviceDetail);
