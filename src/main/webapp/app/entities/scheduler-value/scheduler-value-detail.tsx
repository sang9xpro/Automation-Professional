import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './scheduler-value.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISchedulerValueDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulerValueDetail = (props: ISchedulerValueDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { schedulerValueEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="schedulerValueDetailsHeading">
          <Translate contentKey="automationProfessionalApp.schedulerValue.detail.title">SchedulerValue</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{schedulerValueEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="automationProfessionalApp.schedulerValue.value">Value</Translate>
            </span>
          </dt>
          <dd>{schedulerValueEntity.value}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.schedulerValue.schedulers">Schedulers</Translate>
          </dt>
          <dd>{schedulerValueEntity.schedulers ? schedulerValueEntity.schedulers.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.schedulerValue.schedulerFields">Scheduler Fields</Translate>
          </dt>
          <dd>{schedulerValueEntity.schedulerFields ? schedulerValueEntity.schedulerFields.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/scheduler-value" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/scheduler-value/${schedulerValueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ schedulerValue }: IRootState) => ({
  schedulerValueEntity: schedulerValue.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerValueDetail);
