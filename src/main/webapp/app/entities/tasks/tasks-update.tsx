import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './tasks.reducer';
import { ITasks } from 'app/shared/model/tasks.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ITasksUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const TasksUpdate = (props: ITasksUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { tasksEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/tasks' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...tasksEntity,
        ...values,
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
          <h2 id="automationProfessionalApp.tasks.home.createOrEditLabel" data-cy="TasksCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.tasks.home.createOrEditLabel">Create or edit a Tasks</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : tasksEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="tasks-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="tasks-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="taskNameLabel" for="tasks-taskName">
                  <Translate contentKey="automationProfessionalApp.tasks.taskName">Task Name</Translate>
                </Label>
                <AvField id="tasks-taskName" data-cy="taskName" type="text" name="taskName" />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="tasks-description">
                  <Translate contentKey="automationProfessionalApp.tasks.description">Description</Translate>
                </Label>
                <AvField id="tasks-description" data-cy="description" type="text" name="description" />
              </AvGroup>
              <AvGroup>
                <Label id="sourceLabel" for="tasks-source">
                  <Translate contentKey="automationProfessionalApp.tasks.source">Source</Translate>
                </Label>
                <AvField id="tasks-source" data-cy="source" type="text" name="source" />
              </AvGroup>
              <AvGroup>
                <Label id="priceLabel" for="tasks-price">
                  <Translate contentKey="automationProfessionalApp.tasks.price">Price</Translate>
                </Label>
                <AvField id="tasks-price" data-cy="price" type="string" className="form-control" name="price" />
              </AvGroup>
              <AvGroup>
                <Label id="socialLabel" for="tasks-social">
                  <Translate contentKey="automationProfessionalApp.tasks.social">Social</Translate>
                </Label>
                <AvInput
                  id="tasks-social"
                  data-cy="social"
                  type="select"
                  className="form-control"
                  name="social"
                  value={(!isNew && tasksEntity.social) || 'Facebook'}
                >
                  <option value="Facebook">{translate('automationProfessionalApp.Social.Facebook')}</option>
                  <option value="Youtube">{translate('automationProfessionalApp.Social.Youtube')}</option>
                  <option value="Instagram">{translate('automationProfessionalApp.Social.Instagram')}</option>
                  <option value="Tiktok">{translate('automationProfessionalApp.Social.Tiktok')}</option>
                  <option value="Other">{translate('automationProfessionalApp.Social.Other')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="tasks-type">
                  <Translate contentKey="automationProfessionalApp.tasks.type">Type</Translate>
                </Label>
                <AvInput
                  id="tasks-type"
                  data-cy="type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && tasksEntity.type) || 'Stream'}
                >
                  <option value="Stream">{translate('automationProfessionalApp.TaskType.Stream')}</option>
                  <option value="NonStream">{translate('automationProfessionalApp.TaskType.NonStream')}</option>
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/tasks" replace color="info">
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
  tasksEntity: storeState.tasks.entity,
  loading: storeState.tasks.loading,
  updating: storeState.tasks.updating,
  updateSuccess: storeState.tasks.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TasksUpdate);
