import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './task-values.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITaskValuesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TaskValuesDetail = (props: ITaskValuesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { taskValuesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taskValuesDetailsHeading">
          <Translate contentKey="automationProfessionalApp.taskValues.detail.title">TaskValues</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taskValuesEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="automationProfessionalApp.taskValues.value">Value</Translate>
            </span>
          </dt>
          <dd>{taskValuesEntity.value}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.taskValues.tasks">Tasks</Translate>
          </dt>
          <dd>{taskValuesEntity.tasks ? taskValuesEntity.tasks.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.taskValues.taskFields">Task Fields</Translate>
          </dt>
          <dd>{taskValuesEntity.taskFields ? taskValuesEntity.taskFields.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/task-values" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/task-values/${taskValuesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ taskValues }: IRootState) => ({
  taskValuesEntity: taskValues.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TaskValuesDetail);
