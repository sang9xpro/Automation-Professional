import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './devices-fields.reducer';
import { IDevicesFields } from 'app/shared/model/devices-fields.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDevicesFieldsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DevicesFieldsUpdate = (props: IDevicesFieldsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { devicesFieldsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/devices-fields' + props.location.search);
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
        ...devicesFieldsEntity,
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
          <h2 id="automationProfessionalApp.devicesFields.home.createOrEditLabel" data-cy="DevicesFieldsCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.devicesFields.home.createOrEditLabel">
              Create or edit a DevicesFields
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : devicesFieldsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="devices-fields-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="devices-fields-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fieldsNameLabel" for="devices-fields-fieldsName">
                  <Translate contentKey="automationProfessionalApp.devicesFields.fieldsName">Fields Name</Translate>
                </Label>
                <AvField id="devices-fields-fieldsName" data-cy="fieldsName" type="text" name="fieldsName" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/devices-fields" replace color="info">
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
  devicesFieldsEntity: storeState.devicesFields.entity,
  loading: storeState.devicesFields.loading,
  updating: storeState.devicesFields.updating,
  updateSuccess: storeState.devicesFields.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DevicesFieldsUpdate);
