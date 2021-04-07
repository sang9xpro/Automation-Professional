import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './scheduler-task-device-fields.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISchedulerTaskDeviceFieldsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulerTaskDeviceFieldsDetail = (props: ISchedulerTaskDeviceFieldsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { schedulerTaskDeviceFieldsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="schedulerTaskDeviceFieldsDetailsHeading">
          <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceFields.detail.title">SchedulerTaskDeviceFields</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{schedulerTaskDeviceFieldsEntity.id}</dd>
          <dt>
            <span id="fieldName">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDeviceFields.fieldName">Field Name</Translate>
            </span>
          </dt>
          <dd>{schedulerTaskDeviceFieldsEntity.fieldName}</dd>
        </dl>
        <Button tag={Link} to="/scheduler-task-device-fields" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/scheduler-task-device-fields/${schedulerTaskDeviceFieldsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ schedulerTaskDeviceFields }: IRootState) => ({
  schedulerTaskDeviceFieldsEntity: schedulerTaskDeviceFields.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerTaskDeviceFieldsDetail);
