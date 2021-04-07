import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IHistory } from 'app/shared/model/history.model';
import { getEntities as getHistories } from 'app/entities/history/history.reducer';
import { IHistoryFields } from 'app/shared/model/history-fields.model';
import { getEntities as getHistoryFields } from 'app/entities/history-fields/history-fields.reducer';
import { getEntity, updateEntity, createEntity, reset } from './history-values.reducer';
import { IHistoryValues } from 'app/shared/model/history-values.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHistoryValuesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HistoryValuesUpdate = (props: IHistoryValuesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { historyValuesEntity, histories, historyFields, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/history-values' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getHistories();
    props.getHistoryFields();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...historyValuesEntity,
        ...values,
        history: histories.find(it => it.id.toString() === values.historyId.toString()),
        historyFields: historyFields.find(it => it.id.toString() === values.historyFieldsId.toString()),
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
          <h2 id="automationProfessionalApp.historyValues.home.createOrEditLabel" data-cy="HistoryValuesCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.historyValues.home.createOrEditLabel">
              Create or edit a HistoryValues
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : historyValuesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="history-values-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="history-values-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="history-values-value">
                  <Translate contentKey="automationProfessionalApp.historyValues.value">Value</Translate>
                </Label>
                <AvField id="history-values-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="history-values-history">
                  <Translate contentKey="automationProfessionalApp.historyValues.history">History</Translate>
                </Label>
                <AvInput id="history-values-history" data-cy="history" type="select" className="form-control" name="historyId">
                  <option value="" key="0" />
                  {histories
                    ? histories.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="history-values-historyFields">
                  <Translate contentKey="automationProfessionalApp.historyValues.historyFields">History Fields</Translate>
                </Label>
                <AvInput
                  id="history-values-historyFields"
                  data-cy="historyFields"
                  type="select"
                  className="form-control"
                  name="historyFieldsId"
                >
                  <option value="" key="0" />
                  {historyFields
                    ? historyFields.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/history-values" replace color="info">
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
  histories: storeState.history.entities,
  historyFields: storeState.historyFields.entities,
  historyValuesEntity: storeState.historyValues.entity,
  loading: storeState.historyValues.loading,
  updating: storeState.historyValues.updating,
  updateSuccess: storeState.historyValues.updateSuccess,
});

const mapDispatchToProps = {
  getHistories,
  getHistoryFields,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HistoryValuesUpdate);
