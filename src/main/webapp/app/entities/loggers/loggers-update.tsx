import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { setFileData, openFile, byteSize, Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDevices } from 'app/shared/model/devices.model';
import { getEntities as getDevices } from 'app/entities/devices/devices.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './loggers.reducer';
import { ILoggers } from 'app/shared/model/loggers.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILoggersUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LoggersUpdate = (props: ILoggersUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { loggersEntity, devices, loading, updating } = props;

  const { logDetail, logDetailContentType } = loggersEntity;

  const handleClose = () => {
    props.history.push('/loggers' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getDevices();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.lastUpdate = convertDateTimeToServer(values.lastUpdate);

    if (errors.length === 0) {
      const entity = {
        ...loggersEntity,
        ...values,
        devices: devices.find(it => it.id.toString() === values.devicesId.toString()),
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
          <h2 id="automationProfessionalApp.loggers.home.createOrEditLabel" data-cy="LoggersCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.loggers.home.createOrEditLabel">Create or edit a Loggers</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : loggersEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="loggers-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="loggers-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="statusLabel" for="loggers-status">
                  <Translate contentKey="automationProfessionalApp.loggers.status">Status</Translate>
                </Label>
                <AvField id="loggers-status" data-cy="status" type="text" name="status" />
              </AvGroup>
              <AvGroup>
                <AvGroup>
                  <Label id="logDetailLabel" for="logDetail">
                    <Translate contentKey="automationProfessionalApp.loggers.logDetail">Log Detail</Translate>
                  </Label>
                  <br />
                  {logDetail ? (
                    <div>
                      {logDetailContentType ? (
                        <a onClick={openFile(logDetailContentType, logDetail)}>
                          <Translate contentKey="entity.action.open">Open</Translate>
                        </a>
                      ) : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {logDetailContentType}, {byteSize(logDetail)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('logDetail')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_logDetail" data-cy="logDetail" type="file" onChange={onBlobChange(false, 'logDetail')} />
                  <AvInput type="hidden" name="logDetail" value={logDetail} />
                </AvGroup>
              </AvGroup>
              <AvGroup>
                <Label id="lastUpdateLabel" for="loggers-lastUpdate">
                  <Translate contentKey="automationProfessionalApp.loggers.lastUpdate">Last Update</Translate>
                </Label>
                <AvInput
                  id="loggers-lastUpdate"
                  data-cy="lastUpdate"
                  type="datetime-local"
                  className="form-control"
                  name="lastUpdate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.loggersEntity.lastUpdate)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="loggers-devices">
                  <Translate contentKey="automationProfessionalApp.loggers.devices">Devices</Translate>
                </Label>
                <AvInput id="loggers-devices" data-cy="devices" type="select" className="form-control" name="devicesId">
                  <option value="" key="0" />
                  {devices
                    ? devices.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/loggers" replace color="info">
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
  devices: storeState.devices.entities,
  loggersEntity: storeState.loggers.entity,
  loading: storeState.loggers.loading,
  updating: storeState.loggers.updating,
  updateSuccess: storeState.loggers.updateSuccess,
});

const mapDispatchToProps = {
  getDevices,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LoggersUpdate);
