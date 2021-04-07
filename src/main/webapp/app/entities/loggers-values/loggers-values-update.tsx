import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ILoggers } from 'app/shared/model/loggers.model';
import { getEntities as getLoggers } from 'app/entities/loggers/loggers.reducer';
import { ILoggersFields } from 'app/shared/model/loggers-fields.model';
import { getEntities as getLoggersFields } from 'app/entities/loggers-fields/loggers-fields.reducer';
import { getEntity, updateEntity, createEntity, reset } from './loggers-values.reducer';
import { ILoggersValues } from 'app/shared/model/loggers-values.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILoggersValuesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LoggersValuesUpdate = (props: ILoggersValuesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { loggersValuesEntity, loggers, loggersFields, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/loggers-values' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getLoggers();
    props.getLoggersFields();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...loggersValuesEntity,
        ...values,
        loggers: loggers.find(it => it.id.toString() === values.loggersId.toString()),
        loggersFields: loggersFields.find(it => it.id.toString() === values.loggersFieldsId.toString()),
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
          <h2 id="automationProfessionalApp.loggersValues.home.createOrEditLabel" data-cy="LoggersValuesCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.loggersValues.home.createOrEditLabel">
              Create or edit a LoggersValues
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : loggersValuesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="loggers-values-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="loggers-values-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="loggers-values-value">
                  <Translate contentKey="automationProfessionalApp.loggersValues.value">Value</Translate>
                </Label>
                <AvField id="loggers-values-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="loggers-values-loggers">
                  <Translate contentKey="automationProfessionalApp.loggersValues.loggers">Loggers</Translate>
                </Label>
                <AvInput id="loggers-values-loggers" data-cy="loggers" type="select" className="form-control" name="loggersId">
                  <option value="" key="0" />
                  {loggers
                    ? loggers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="loggers-values-loggersFields">
                  <Translate contentKey="automationProfessionalApp.loggersValues.loggersFields">Loggers Fields</Translate>
                </Label>
                <AvInput
                  id="loggers-values-loggersFields"
                  data-cy="loggersFields"
                  type="select"
                  className="form-control"
                  name="loggersFieldsId"
                >
                  <option value="" key="0" />
                  {loggersFields
                    ? loggersFields.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/loggers-values" replace color="info">
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
  loggers: storeState.loggers.entities,
  loggersFields: storeState.loggersFields.entities,
  loggersValuesEntity: storeState.loggersValues.entity,
  loading: storeState.loggersValues.loading,
  updating: storeState.loggersValues.updating,
  updateSuccess: storeState.loggersValues.updateSuccess,
});

const mapDispatchToProps = {
  getLoggers,
  getLoggersFields,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LoggersValuesUpdate);
