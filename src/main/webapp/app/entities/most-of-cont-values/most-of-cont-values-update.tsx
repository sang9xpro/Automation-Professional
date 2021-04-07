import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMostOfContent } from 'app/shared/model/most-of-content.model';
import { getEntities as getMostOfContents } from 'app/entities/most-of-content/most-of-content.reducer';
import { IMostOfContFields } from 'app/shared/model/most-of-cont-fields.model';
import { getEntities as getMostOfContFields } from 'app/entities/most-of-cont-fields/most-of-cont-fields.reducer';
import { getEntity, updateEntity, createEntity, reset } from './most-of-cont-values.reducer';
import { IMostOfContValues } from 'app/shared/model/most-of-cont-values.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMostOfContValuesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MostOfContValuesUpdate = (props: IMostOfContValuesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { mostOfContValuesEntity, mostOfContents, mostOfContFields, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/most-of-cont-values' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getMostOfContents();
    props.getMostOfContFields();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...mostOfContValuesEntity,
        ...values,
        mostOfContent: mostOfContents.find(it => it.id.toString() === values.mostOfContentId.toString()),
        mostOfContFields: mostOfContFields.find(it => it.id.toString() === values.mostOfContFieldsId.toString()),
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
          <h2 id="automationProfessionalApp.mostOfContValues.home.createOrEditLabel" data-cy="MostOfContValuesCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.mostOfContValues.home.createOrEditLabel">
              Create or edit a MostOfContValues
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : mostOfContValuesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="most-of-cont-values-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="most-of-cont-values-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="most-of-cont-values-value">
                  <Translate contentKey="automationProfessionalApp.mostOfContValues.value">Value</Translate>
                </Label>
                <AvField id="most-of-cont-values-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="most-of-cont-values-mostOfContent">
                  <Translate contentKey="automationProfessionalApp.mostOfContValues.mostOfContent">Most Of Content</Translate>
                </Label>
                <AvInput
                  id="most-of-cont-values-mostOfContent"
                  data-cy="mostOfContent"
                  type="select"
                  className="form-control"
                  name="mostOfContentId"
                >
                  <option value="" key="0" />
                  {mostOfContents
                    ? mostOfContents.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="most-of-cont-values-mostOfContFields">
                  <Translate contentKey="automationProfessionalApp.mostOfContValues.mostOfContFields">Most Of Cont Fields</Translate>
                </Label>
                <AvInput
                  id="most-of-cont-values-mostOfContFields"
                  data-cy="mostOfContFields"
                  type="select"
                  className="form-control"
                  name="mostOfContFieldsId"
                >
                  <option value="" key="0" />
                  {mostOfContFields
                    ? mostOfContFields.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/most-of-cont-values" replace color="info">
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
  mostOfContents: storeState.mostOfContent.entities,
  mostOfContFields: storeState.mostOfContFields.entities,
  mostOfContValuesEntity: storeState.mostOfContValues.entity,
  loading: storeState.mostOfContValues.loading,
  updating: storeState.mostOfContValues.updating,
  updateSuccess: storeState.mostOfContValues.updateSuccess,
});

const mapDispatchToProps = {
  getMostOfContents,
  getMostOfContFields,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MostOfContValuesUpdate);
