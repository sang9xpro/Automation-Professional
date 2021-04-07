import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFacebook } from 'app/shared/model/facebook.model';
import { getEntities as getFacebooks } from 'app/entities/facebook/facebook.reducer';
import { IFacebookFields } from 'app/shared/model/facebook-fields.model';
import { getEntities as getFacebookFields } from 'app/entities/facebook-fields/facebook-fields.reducer';
import { getEntity, updateEntity, createEntity, reset } from './facebook-values.reducer';
import { IFacebookValues } from 'app/shared/model/facebook-values.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFacebookValuesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FacebookValuesUpdate = (props: IFacebookValuesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { facebookValuesEntity, facebooks, facebookFields, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/facebook-values' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getFacebooks();
    props.getFacebookFields();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...facebookValuesEntity,
        ...values,
        facebook: facebooks.find(it => it.id.toString() === values.facebookId.toString()),
        facebookFields: facebookFields.find(it => it.id.toString() === values.facebookFieldsId.toString()),
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
          <h2 id="automationProfessionalApp.facebookValues.home.createOrEditLabel" data-cy="FacebookValuesCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.facebookValues.home.createOrEditLabel">
              Create or edit a FacebookValues
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : facebookValuesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="facebook-values-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="facebook-values-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="facebook-values-value">
                  <Translate contentKey="automationProfessionalApp.facebookValues.value">Value</Translate>
                </Label>
                <AvField id="facebook-values-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="facebook-values-facebook">
                  <Translate contentKey="automationProfessionalApp.facebookValues.facebook">Facebook</Translate>
                </Label>
                <AvInput id="facebook-values-facebook" data-cy="facebook" type="select" className="form-control" name="facebookId">
                  <option value="" key="0" />
                  {facebooks
                    ? facebooks.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="facebook-values-facebookFields">
                  <Translate contentKey="automationProfessionalApp.facebookValues.facebookFields">Facebook Fields</Translate>
                </Label>
                <AvInput
                  id="facebook-values-facebookFields"
                  data-cy="facebookFields"
                  type="select"
                  className="form-control"
                  name="facebookFieldsId"
                >
                  <option value="" key="0" />
                  {facebookFields
                    ? facebookFields.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/facebook-values" replace color="info">
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
  facebooks: storeState.facebook.entities,
  facebookFields: storeState.facebookFields.entities,
  facebookValuesEntity: storeState.facebookValues.entity,
  loading: storeState.facebookValues.loading,
  updating: storeState.facebookValues.updating,
  updateSuccess: storeState.facebookValues.updateSuccess,
});

const mapDispatchToProps = {
  getFacebooks,
  getFacebookFields,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FacebookValuesUpdate);
