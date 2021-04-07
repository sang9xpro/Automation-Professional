import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './most-of-cont-fields.reducer';
import { IMostOfContFields } from 'app/shared/model/most-of-cont-fields.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMostOfContFieldsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MostOfContFieldsUpdate = (props: IMostOfContFieldsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { mostOfContFieldsEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/most-of-cont-fields' + props.location.search);
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
        ...mostOfContFieldsEntity,
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
          <h2 id="automationProfessionalApp.mostOfContFields.home.createOrEditLabel" data-cy="MostOfContFieldsCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.mostOfContFields.home.createOrEditLabel">
              Create or edit a MostOfContFields
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : mostOfContFieldsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="most-of-cont-fields-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="most-of-cont-fields-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fieldNameLabel" for="most-of-cont-fields-fieldName">
                  <Translate contentKey="automationProfessionalApp.mostOfContFields.fieldName">Field Name</Translate>
                </Label>
                <AvField id="most-of-cont-fields-fieldName" data-cy="fieldName" type="text" name="fieldName" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/most-of-cont-fields" replace color="info">
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
  mostOfContFieldsEntity: storeState.mostOfContFields.entity,
  loading: storeState.mostOfContFields.loading,
  updating: storeState.mostOfContFields.updating,
  updateSuccess: storeState.mostOfContFields.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MostOfContFieldsUpdate);
