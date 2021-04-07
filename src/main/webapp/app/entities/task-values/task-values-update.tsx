import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ITasks } from 'app/shared/model/tasks.model';
import { getEntities as getTasks } from 'app/entities/tasks/tasks.reducer';
import { ITaskFields } from 'app/shared/model/task-fields.model';
import { getEntities as getTaskFields } from 'app/entities/task-fields/task-fields.reducer';
import { getEntity, updateEntity, createEntity, reset } from './task-values.reducer';
import { ITaskValues } from 'app/shared/model/task-values.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITaskValuesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TaskValuesUpdate = (props: ITaskValuesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { taskValuesEntity, tasks, taskFields, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/task-values' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getTasks();
    props.getTaskFields();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...taskValuesEntity,
        ...values,
        tasks: tasks.find(it => it.id.toString() === values.tasksId.toString()),
        taskFields: taskFields.find(it => it.id.toString() === values.taskFieldsId.toString()),
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
          <h2 id="automationProfessionalApp.taskValues.home.createOrEditLabel" data-cy="TaskValuesCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.taskValues.home.createOrEditLabel">Create or edit a TaskValues</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : taskValuesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="task-values-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="task-values-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="task-values-value">
                  <Translate contentKey="automationProfessionalApp.taskValues.value">Value</Translate>
                </Label>
                <AvField id="task-values-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="task-values-tasks">
                  <Translate contentKey="automationProfessionalApp.taskValues.tasks">Tasks</Translate>
                </Label>
                <AvInput id="task-values-tasks" data-cy="tasks" type="select" className="form-control" name="tasksId">
                  <option value="" key="0" />
                  {tasks
                    ? tasks.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="task-values-taskFields">
                  <Translate contentKey="automationProfessionalApp.taskValues.taskFields">Task Fields</Translate>
                </Label>
                <AvInput id="task-values-taskFields" data-cy="taskFields" type="select" className="form-control" name="taskFieldsId">
                  <option value="" key="0" />
                  {taskFields
                    ? taskFields.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/task-values" replace color="info">
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
  tasks: storeState.tasks.entities,
  taskFields: storeState.taskFields.entities,
  taskValuesEntity: storeState.taskValues.entity,
  loading: storeState.taskValues.loading,
  updating: storeState.taskValues.updating,
  updateSuccess: storeState.taskValues.updateSuccess,
});

const mapDispatchToProps = {
  getTasks,
  getTaskFields,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TaskValuesUpdate);
