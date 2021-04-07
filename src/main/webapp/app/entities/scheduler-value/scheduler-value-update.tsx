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
import { ISchedulerFields } from 'app/shared/model/scheduler-fields.model';
import { getEntities as getSchedulerFields } from 'app/entities/scheduler-fields/scheduler-fields.reducer';
import { getEntity, updateEntity, createEntity, reset } from './scheduler-value.reducer';
import { ISchedulerValue } from 'app/shared/model/scheduler-value.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISchedulerValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulerValueUpdate = (props: ISchedulerValueUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { schedulerValueEntity, schedulers, schedulerFields, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/scheduler-value' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getSchedulers();
    props.getSchedulerFields();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...schedulerValueEntity,
        ...values,
        schedulers: schedulers.find(it => it.id.toString() === values.schedulersId.toString()),
        schedulerFields: schedulerFields.find(it => it.id.toString() === values.schedulerFieldsId.toString()),
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
          <h2 id="automationProfessionalApp.schedulerValue.home.createOrEditLabel" data-cy="SchedulerValueCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.schedulerValue.home.createOrEditLabel">
              Create or edit a SchedulerValue
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : schedulerValueEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="scheduler-value-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="scheduler-value-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="scheduler-value-value">
                  <Translate contentKey="automationProfessionalApp.schedulerValue.value">Value</Translate>
                </Label>
                <AvField id="scheduler-value-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="scheduler-value-schedulers">
                  <Translate contentKey="automationProfessionalApp.schedulerValue.schedulers">Schedulers</Translate>
                </Label>
                <AvInput id="scheduler-value-schedulers" data-cy="schedulers" type="select" className="form-control" name="schedulersId">
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
                <Label for="scheduler-value-schedulerFields">
                  <Translate contentKey="automationProfessionalApp.schedulerValue.schedulerFields">Scheduler Fields</Translate>
                </Label>
                <AvInput
                  id="scheduler-value-schedulerFields"
                  data-cy="schedulerFields"
                  type="select"
                  className="form-control"
                  name="schedulerFieldsId"
                >
                  <option value="" key="0" />
                  {schedulerFields
                    ? schedulerFields.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/scheduler-value" replace color="info">
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
  schedulerFields: storeState.schedulerFields.entities,
  schedulerValueEntity: storeState.schedulerValue.entity,
  loading: storeState.schedulerValue.loading,
  updating: storeState.schedulerValue.updating,
  updateSuccess: storeState.schedulerValue.updateSuccess,
});

const mapDispatchToProps = {
  getSchedulers,
  getSchedulerFields,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerValueUpdate);
