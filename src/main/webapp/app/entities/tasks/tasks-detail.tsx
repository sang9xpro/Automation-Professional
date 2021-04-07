import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './tasks.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITasksDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TasksDetail = (props: ITasksDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { tasksEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tasksDetailsHeading">
          <Translate contentKey="automationProfessionalApp.tasks.detail.title">Tasks</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tasksEntity.id}</dd>
          <dt>
            <span id="taskName">
              <Translate contentKey="automationProfessionalApp.tasks.taskName">Task Name</Translate>
            </span>
          </dt>
          <dd>{tasksEntity.taskName}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="automationProfessionalApp.tasks.description">Description</Translate>
            </span>
          </dt>
          <dd>{tasksEntity.description}</dd>
          <dt>
            <span id="source">
              <Translate contentKey="automationProfessionalApp.tasks.source">Source</Translate>
            </span>
          </dt>
          <dd>{tasksEntity.source}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="automationProfessionalApp.tasks.price">Price</Translate>
            </span>
          </dt>
          <dd>{tasksEntity.price}</dd>
          <dt>
            <span id="social">
              <Translate contentKey="automationProfessionalApp.tasks.social">Social</Translate>
            </span>
          </dt>
          <dd>{tasksEntity.social}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="automationProfessionalApp.tasks.type">Type</Translate>
            </span>
          </dt>
          <dd>{tasksEntity.type}</dd>
        </dl>
        <Button tag={Link} to="/tasks" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tasks/${tasksEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ tasks }: IRootState) => ({
  tasksEntity: tasks.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TasksDetail);
