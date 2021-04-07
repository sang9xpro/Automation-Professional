import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './history.reducer';
import { IHistory } from 'app/shared/model/history.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHistoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HistoryUpdate = (props: IHistoryUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { historyEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/history' + props.location.search);
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
    values.lastUpdate = convertDateTimeToServer(values.lastUpdate);

    if (errors.length === 0) {
      const entity = {
        ...historyEntity,
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
          <h2 id="automationProfessionalApp.history.home.createOrEditLabel" data-cy="HistoryCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.history.home.createOrEditLabel">Create or edit a History</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : historyEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="history-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="history-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="urlLabel" for="history-url">
                  <Translate contentKey="automationProfessionalApp.history.url">Url</Translate>
                </Label>
                <AvField id="history-url" data-cy="url" type="text" name="url" />
              </AvGroup>
              <AvGroup>
                <Label id="taskIdLabel" for="history-taskId">
                  <Translate contentKey="automationProfessionalApp.history.taskId">Task Id</Translate>
                </Label>
                <AvField id="history-taskId" data-cy="taskId" type="string" className="form-control" name="taskId" />
              </AvGroup>
              <AvGroup>
                <Label id="deviceIdLabel" for="history-deviceId">
                  <Translate contentKey="automationProfessionalApp.history.deviceId">Device Id</Translate>
                </Label>
                <AvField id="history-deviceId" data-cy="deviceId" type="string" className="form-control" name="deviceId" />
              </AvGroup>
              <AvGroup>
                <Label id="accountIdLabel" for="history-accountId">
                  <Translate contentKey="automationProfessionalApp.history.accountId">Account Id</Translate>
                </Label>
                <AvField id="history-accountId" data-cy="accountId" type="string" className="form-control" name="accountId" />
              </AvGroup>
              <AvGroup>
                <Label id="lastUpdateLabel" for="history-lastUpdate">
                  <Translate contentKey="automationProfessionalApp.history.lastUpdate">Last Update</Translate>
                </Label>
                <AvInput
                  id="history-lastUpdate"
                  data-cy="lastUpdate"
                  type="datetime-local"
                  className="form-control"
                  name="lastUpdate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.historyEntity.lastUpdate)}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/history" replace color="info">
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
  historyEntity: storeState.history.entity,
  loading: storeState.history.loading,
  updating: storeState.history.updating,
  updateSuccess: storeState.history.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HistoryUpdate);
