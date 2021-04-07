import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISchedulers } from 'app/shared/model/schedulers.model';
import { getEntities as getSchedulers } from 'app/entities/schedulers/schedulers.reducer';
import { ITasks } from 'app/shared/model/tasks.model';
import { getEntities as getTasks } from 'app/entities/tasks/tasks.reducer';
import { IDevices } from 'app/shared/model/devices.model';
import { getEntities as getDevices } from 'app/entities/devices/devices.reducer';
import { getEntity, updateEntity, createEntity, reset } from './scheduler-task-device.reducer';
import { ISchedulerTaskDevice } from 'app/shared/model/scheduler-task-device.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISchedulerTaskDeviceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulerTaskDeviceUpdate = (props: ISchedulerTaskDeviceUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { schedulerTaskDeviceEntity, schedulers, tasks, devices, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/scheduler-task-device' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getSchedulers();
    props.getTasks();
    props.getDevices();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.startTime = convertDateTimeToServer(values.startTime);
    values.endTime = convertDateTimeToServer(values.endTime);
    values.lastUpdate = convertDateTimeToServer(values.lastUpdate);

    if (errors.length === 0) {
      const entity = {
        ...schedulerTaskDeviceEntity,
        ...values,
        schedulers: schedulers.find(it => it.id.toString() === values.schedulersId.toString()),
        tasks: tasks.find(it => it.id.toString() === values.tasksId.toString()),
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
          <h2 id="automationProfessionalApp.schedulerTaskDevice.home.createOrEditLabel" data-cy="SchedulerTaskDeviceCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.home.createOrEditLabel">
              Create or edit a SchedulerTaskDevice
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : schedulerTaskDeviceEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="scheduler-task-device-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="scheduler-task-device-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="startTimeLabel" for="scheduler-task-device-startTime">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.startTime">Start Time</Translate>
                </Label>
                <AvInput
                  id="scheduler-task-device-startTime"
                  data-cy="startTime"
                  type="datetime-local"
                  className="form-control"
                  name="startTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.schedulerTaskDeviceEntity.startTime)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endTimeLabel" for="scheduler-task-device-endTime">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.endTime">End Time</Translate>
                </Label>
                <AvInput
                  id="scheduler-task-device-endTime"
                  data-cy="endTime"
                  type="datetime-local"
                  className="form-control"
                  name="endTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.schedulerTaskDeviceEntity.endTime)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="countFromLabel" for="scheduler-task-device-countFrom">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.countFrom">Count From</Translate>
                </Label>
                <AvField id="scheduler-task-device-countFrom" data-cy="countFrom" type="string" className="form-control" name="countFrom" />
              </AvGroup>
              <AvGroup>
                <Label id="countToLabel" for="scheduler-task-device-countTo">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.countTo">Count To</Translate>
                </Label>
                <AvField id="scheduler-task-device-countTo" data-cy="countTo" type="string" className="form-control" name="countTo" />
              </AvGroup>
              <AvGroup>
                <Label id="pointLabel" for="scheduler-task-device-point">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.point">Point</Translate>
                </Label>
                <AvField id="scheduler-task-device-point" data-cy="point" type="string" className="form-control" name="point" />
              </AvGroup>
              <AvGroup>
                <Label id="lastUpdateLabel" for="scheduler-task-device-lastUpdate">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.lastUpdate">Last Update</Translate>
                </Label>
                <AvInput
                  id="scheduler-task-device-lastUpdate"
                  data-cy="lastUpdate"
                  type="datetime-local"
                  className="form-control"
                  name="lastUpdate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.schedulerTaskDeviceEntity.lastUpdate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="ownerLabel" for="scheduler-task-device-owner">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.owner">Owner</Translate>
                </Label>
                <AvField id="scheduler-task-device-owner" data-cy="owner" type="text" name="owner" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="scheduler-task-device-status">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.status">Status</Translate>
                </Label>
                <AvInput
                  id="scheduler-task-device-status"
                  data-cy="status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && schedulerTaskDeviceEntity.status) || 'Open'}
                >
                  <option value="Open">{translate('automationProfessionalApp.SchedulerStatus.Open')}</option>
                  <option value="Processing">{translate('automationProfessionalApp.SchedulerStatus.Processing')}</option>
                  <option value="Waiting">{translate('automationProfessionalApp.SchedulerStatus.Waiting')}</option>
                  <option value="Completed">{translate('automationProfessionalApp.SchedulerStatus.Completed')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="scheduler-task-device-schedulers">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.schedulers">Schedulers</Translate>
                </Label>
                <AvInput
                  id="scheduler-task-device-schedulers"
                  data-cy="schedulers"
                  type="select"
                  className="form-control"
                  name="schedulersId"
                >
                  <option value="" key="0" />
                  {schedulers
                    ? schedulers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="scheduler-task-device-tasks">
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.tasks">Tasks</Translate>
                </Label>
                <AvInput id="scheduler-task-device-tasks" data-cy="tasks" type="select" className="form-control" name="tasksId">
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
              <Button tag={Link} id="cancel-save" to="/scheduler-task-device" replace color="info">
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
  schedulers: storeState.schedulers.entities,
  tasks: storeState.tasks.entities,
  devices: storeState.devices.entities,
  schedulerTaskDeviceEntity: storeState.schedulerTaskDevice.entity,
  loading: storeState.schedulerTaskDevice.loading,
  updating: storeState.schedulerTaskDevice.updating,
  updateSuccess: storeState.schedulerTaskDevice.updateSuccess,
});

const mapDispatchToProps = {
  getSchedulers,
  getTasks,
  getDevices,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerTaskDeviceUpdate);
