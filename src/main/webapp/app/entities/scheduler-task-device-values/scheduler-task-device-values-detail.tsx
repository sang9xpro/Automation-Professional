import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './scheduler-task-device-values.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISchedulerTaskDeviceValuesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulerTaskDeviceValuesDetail = (props: ISchedulerTaskDeviceValuesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { schedulerTaskDeviceValuesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="schedulerTaskDeviceValuesDetailsHeading">
          <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceValues.detail.title">SchedulerTaskDeviceValues</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{schedulerTaskDeviceValuesEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceValues.value">Value</Translate>
            </span>
          </dt>
          <dd>{schedulerTaskDeviceValuesEntity.value}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceValues.schedulerTaskDevice">
              Scheduler Task Device
            </Translate>
          </dt>
          <dd>{schedulerTaskDeviceValuesEntity.schedulerTaskDevice ? schedulerTaskDeviceValuesEntity.schedulerTaskDevice.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceValues.schedulerTaskDeviceFields">
              Scheduler Task Device Fields
            </Translate>
          </dt>
          <dd>
            {schedulerTaskDeviceValuesEntity.schedulerTaskDeviceFields ? schedulerTaskDeviceValuesEntity.schedulerTaskDeviceFields.id : ''}
          </dd>
        </dl>
        <Button tag={Link} to="/scheduler-task-device-values" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/scheduler-task-device-values/${schedulerTaskDeviceValuesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ schedulerTaskDeviceValues }: IRootState) => ({
  schedulerTaskDeviceValuesEntity: schedulerTaskDeviceValues.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerTaskDeviceValuesDetail);
