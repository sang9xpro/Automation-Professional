import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { getEntity, updateEntity, createEntity, reset } from './facebook.reducer';
import { IFacebook } from 'app/shared/model/facebook.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFacebookUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FacebookUpdate = (props: IFacebookUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { facebookEntity, countries, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/facebook' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getCountries();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...facebookEntity,
        ...values,
        country: countries.find(it => it.id.toString() === values.countryId.toString()),
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
          <h2 id="automationProfessionalApp.facebook.home.createOrEditLabel" data-cy="FacebookCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.facebook.home.createOrEditLabel">Create or edit a Facebook</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : facebookEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="facebook-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="facebook-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="facebook-name">
                  <Translate contentKey="automationProfessionalApp.facebook.name">Name</Translate>
                </Label>
                <AvField id="facebook-name" data-cy="name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="urlLabel" for="facebook-url">
                  <Translate contentKey="automationProfessionalApp.facebook.url">Url</Translate>
                </Label>
                <AvField id="facebook-url" data-cy="url" type="text" name="url" />
              </AvGroup>
              <AvGroup>
                <Label id="idOnFbLabel" for="facebook-idOnFb">
                  <Translate contentKey="automationProfessionalApp.facebook.idOnFb">Id On Fb</Translate>
                </Label>
                <AvField id="facebook-idOnFb" data-cy="idOnFb" type="text" name="idOnFb" />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="facebook-type">
                  <Translate contentKey="automationProfessionalApp.facebook.type">Type</Translate>
                </Label>
                <AvInput
                  id="facebook-type"
                  data-cy="type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && facebookEntity.type) || 'Post'}
                >
                  <option value="Post">{translate('automationProfessionalApp.FbType.Post')}</option>
                  <option value="People">{translate('automationProfessionalApp.FbType.People')}</option>
                  <option value="Image">{translate('automationProfessionalApp.FbType.Image')}</option>
                  <option value="Video">{translate('automationProfessionalApp.FbType.Video')}</option>
                  <option value="Page">{translate('automationProfessionalApp.FbType.Page')}</option>
                  <option value="Location">{translate('automationProfessionalApp.FbType.Location')}</option>
                  <option value="Group">{translate('automationProfessionalApp.FbType.Group')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="facebook-country">
                  <Translate contentKey="automationProfessionalApp.facebook.country">Country</Translate>
                </Label>
                <AvInput id="facebook-country" data-cy="country" type="select" className="form-control" name="countryId">
                  <option value="" key="0" />
                  {countries
                    ? countries.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/facebook" replace color="info">
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
  countries: storeState.country.entities,
  facebookEntity: storeState.facebook.entity,
  loading: storeState.facebook.loading,
  updating: storeState.facebook.updating,
  updateSuccess: storeState.facebook.updateSuccess,
});

const mapDispatchToProps = {
  getCountries,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FacebookUpdate);
