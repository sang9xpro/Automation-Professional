import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './task-fields.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITaskFieldsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TaskFieldsDetail = (props: ITaskFieldsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { taskFieldsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taskFieldsDetailsHeading">
          <Translate contentKey="automationProfessionalApp.taskFields.detail.title">TaskFields</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taskFieldsEntity.id}</dd>
          <dt>
            <span id="fieldName">
              <Translate contentKey="automationProfessionalApp.taskFields.fieldName">Field Name</Translate>
            </span>
          </dt>
          <dd>{taskFieldsEntity.fieldName}</dd>
        </dl>
        <Button tag={Link} to="/task-fields" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/task-fields/${taskFieldsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ taskFields }: IRootState) => ({
  taskFieldsEntity: taskFields.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TaskFieldsDetail);
