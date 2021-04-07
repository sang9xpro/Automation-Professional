import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAccounts } from 'app/shared/model/accounts.model';
import { getEntities as getAccounts } from 'app/entities/accounts/accounts.reducer';
import { IAccountFields } from 'app/shared/model/account-fields.model';
import { getEntities as getAccountFields } from 'app/entities/account-fields/account-fields.reducer';
import { getEntity, updateEntity, createEntity, reset } from './account-values.reducer';
import { IAccountValues } from 'app/shared/model/account-values.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAccountValuesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AccountValuesUpdate = (props: IAccountValuesUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { accountValuesEntity, accounts, accountFields, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/account-values' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAccounts();
    props.getAccountFields();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...accountValuesEntity,
        ...values,
        accounts: accounts.find(it => it.id.toString() === values.accountsId.toString()),
        accountFields: accountFields.find(it => it.id.toString() === values.accountFieldsId.toString()),
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
          <h2 id="automationProfessionalApp.accountValues.home.createOrEditLabel" data-cy="AccountValuesCreateUpdateHeading">
            <Translate contentKey="automationProfessionalApp.accountValues.home.createOrEditLabel">
              Create or edit a AccountValues
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : accountValuesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="account-values-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="account-values-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="account-values-value">
                  <Translate contentKey="automationProfessionalApp.accountValues.value">Value</Translate>
                </Label>
                <AvField id="account-values-value" data-cy="value" type="text" name="value" />
              </AvGroup>
              <AvGroup>
                <Label for="account-values-accounts">
                  <Translate contentKey="automationProfessionalApp.accountValues.accounts">Accounts</Translate>
                </Label>
                <AvInput id="account-values-accounts" data-cy="accounts" type="select" className="form-control" name="accountsId">
                  <option value="" key="0" />
                  {accounts
                    ? accounts.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="account-values-accountFields">
                  <Translate contentKey="automationProfessionalApp.accountValues.accountFields">Account Fields</Translate>
                </Label>
                <AvInput
                  id="account-values-accountFields"
                  data-cy="accountFields"
                  type="select"
                  className="form-control"
                  name="accountFieldsId"
                >
                  <option value="" key="0" />
                  {accountFields
                    ? accountFields.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/account-values" replace color="info">
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
  accounts: storeState.accounts.entities,
  accountFields: storeState.accountFields.entities,
  accountValuesEntity: storeState.accountValues.entity,
  loading: storeState.accountValues.loading,
  updating: storeState.accountValues.updating,
  updateSuccess: storeState.accountValues.updateSuccess,
});

const mapDispatchToProps = {
  getAccounts,
  getAccountFields,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AccountValuesUpdate);
