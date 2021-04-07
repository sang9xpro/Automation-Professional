import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './account-values.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAccountValuesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AccountValuesDetail = (props: IAccountValuesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { accountValuesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="accountValuesDetailsHeading">
          <Translate contentKey="automationProfessionalApp.accountValues.detail.title">AccountValues</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{accountValuesEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="automationProfessionalApp.accountValues.value">Value</Translate>
            </span>
          </dt>
          <dd>{accountValuesEntity.value}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.accountValues.accounts">Accounts</Translate>
          </dt>
          <dd>{accountValuesEntity.accounts ? accountValuesEntity.accounts.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.accountValues.accountFields">Account Fields</Translate>
          </dt>
          <dd>{accountValuesEntity.accountFields ? accountValuesEntity.accountFields.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/account-values" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/account-values/${accountValuesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ accountValues }: IRootState) => ({
  accountValuesEntity: accountValues.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AccountValuesDetail);
