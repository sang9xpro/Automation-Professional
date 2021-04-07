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
import { IDevices } from 'app/shared/model/devices.model';
import { getEntities as getDevices } from 'app/entities/devices/devices.reducer';
import { getEntity, updateEntity, createEntity, reset } from './accounts.reducer';
import { IAccounts } from 'app/shared/model/accounts.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAccountsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AccountsUpdate = (props: IAccountsUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { accountsEntity, countries, devices, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/accounts' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getCountries();
    props.getDevices();
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
        ...accountsEntity,
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
          <h2 id="automationProfessionalApp.accounts.home.createOrEditLabel" data-cy="AccountsCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.accounts.home.createOrEditLabel">Create or edit a Accounts</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : accountsEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="accounts-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="accounts-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="userNameLabel" for="accounts-userName">
                  <Translate contentKey="automationProfessionalApp.accounts.userName">User Name</Translate>
                </Label>
                <AvField id="accounts-userName" data-cy="userName" type="text" name="userName" />
              </AvGroup>
              <AvGroup>
                <Label id="passwordLabel" for="accounts-password">
                  <Translate contentKey="automationProfessionalApp.accounts.password">Password</Translate>
                </Label>
                <AvField id="accounts-password" data-cy="password" type="text" name="password" />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="accounts-type">
                  <Translate contentKey="automationProfessionalApp.accounts.type">Type</Translate>
                </Label>
                <AvInput
                  id="accounts-type"
                  data-cy="type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && accountsEntity.type) || 'Facebook'}
                >
                  <option value="Facebook">{translate('automationProfessionalApp.Social.Facebook')}</option>
                  <option value="Youtube">{translate('automationProfessionalApp.Social.Youtube')}</option>
                  <option value="Instagram">{translate('automationProfessionalApp.Social.Instagram')}</option>
                  <option value="Tiktok">{translate('automationProfessionalApp.Social.Tiktok')}</option>
                  <option value="Other">{translate('automationProfessionalApp.Social.Other')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="urlLoginLabel" for="accounts-urlLogin">
                  <Translate contentKey="automationProfessionalApp.accounts.urlLogin">Url Login</Translate>
                </Label>
                <AvField id="accounts-urlLogin" data-cy="urlLogin" type="text" name="urlLogin" />
              </AvGroup>
              <AvGroup>
                <Label id="profileFirefoxLabel" for="accounts-profileFirefox">
                  <Translate contentKey="automationProfessionalApp.accounts.profileFirefox">Profile Firefox</Translate>
                </Label>
                <AvField id="accounts-profileFirefox" data-cy="profileFirefox" type="text" name="profileFirefox" />
              </AvGroup>
              <AvGroup>
                <Label id="profileChromeLabel" for="accounts-profileChrome">
                  <Translate contentKey="automationProfessionalApp.accounts.profileChrome">Profile Chrome</Translate>
                </Label>
                <AvField id="accounts-profileChrome" data-cy="profileChrome" type="text" name="profileChrome" />
              </AvGroup>
              <AvGroup>
                <Label id="lastUpdateLabel" for="accounts-lastUpdate">
                  <Translate contentKey="automationProfessionalApp.accounts.lastUpdate">Last Update</Translate>
                </Label>
                <AvInput
                  id="accounts-lastUpdate"
                  data-cy="lastUpdate"
                  type="datetime-local"
                  className="form-control"
                  name="lastUpdate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.accountsEntity.lastUpdate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="ownerLabel" for="accounts-owner">
                  <Translate contentKey="automationProfessionalApp.accounts.owner">Owner</Translate>
                </Label>
                <AvField id="accounts-owner" data-cy="owner" type="text" name="owner" />
              </AvGroup>
              <AvGroup>
                <Label id="activedLabel" for="accounts-actived">
                  <Translate contentKey="automationProfessionalApp.accounts.actived">Actived</Translate>
                </Label>
                <AvField
                  id="accounts-actived"
                  data-cy="actived"
                  type="string"
                  className="form-control"
                  name="actived"
                  validate={{
                    min: { value: 1, errorMessage: translate('entity.validation.min', { min: 1 }) },
                    max: { value: 1, errorMessage: translate('entity.validation.max', { max: 1 }) },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="accounts-country">
                  <Translate contentKey="automationProfessionalApp.accounts.country">Country</Translate>
                </Label>
                <AvInput id="accounts-country" data-cy="country" type="select" className="form-control" name="countryId">
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
              <Button tag={Link} id="cancel-save" to="/accounts" replace color="info">
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
  devices: storeState.devices.entities,
  accountsEntity: storeState.accounts.entity,
  loading: storeState.accounts.loading,
  updating: storeState.accounts.updating,
  updateSuccess: storeState.accounts.updateSuccess,
});

const mapDispatchToProps = {
  getCountries,
  getDevices,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AccountsUpdate);
