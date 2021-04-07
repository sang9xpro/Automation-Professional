import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './accounts.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAccountsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AccountsDetail = (props: IAccountsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { accountsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="accountsDetailsHeading">
          <Translate contentKey="automationProfessionalApp.accounts.detail.title">Accounts</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{accountsEntity.id}</dd>
          <dt>
            <span id="userName">
              <Translate contentKey="automationProfessionalApp.accounts.userName">User Name</Translate>
            </span>
          </dt>
          <dd>{accountsEntity.userName}</dd>
          <dt>
            <span id="password">
              <Translate contentKey="automationProfessionalApp.accounts.password">Password</Translate>
            </span>
          </dt>
          <dd>{accountsEntity.password}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="automationProfessionalApp.accounts.type">Type</Translate>
            </span>
          </dt>
          <dd>{accountsEntity.type}</dd>
          <dt>
            <span id="urlLogin">
              <Translate contentKey="automationProfessionalApp.accounts.urlLogin">Url Login</Translate>
            </span>
          </dt>
          <dd>{accountsEntity.urlLogin}</dd>
          <dt>
            <span id="profileFirefox">
              <Translate contentKey="automationProfessionalApp.accounts.profileFirefox">Profile Firefox</Translate>
            </span>
          </dt>
          <dd>{accountsEntity.profileFirefox}</dd>
          <dt>
            <span id="profileChrome">
              <Translate contentKey="automationProfessionalApp.accounts.profileChrome">Profile Chrome</Translate>
            </span>
          </dt>
          <dd>{accountsEntity.profileChrome}</dd>
          <dt>
            <span id="lastUpdate">
              <Translate contentKey="automationProfessionalApp.accounts.lastUpdate">Last Update</Translate>
            </span>
          </dt>
          <dd>
            {accountsEntity.lastUpdate ? <TextFormat value={accountsEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="owner">
              <Translate contentKey="automationProfessionalApp.accounts.owner">Owner</Translate>
            </span>
          </dt>
          <dd>{accountsEntity.owner}</dd>
          <dt>
            <span id="actived">
              <Translate contentKey="automationProfessionalApp.accounts.actived">Actived</Translate>
            </span>
          </dt>
          <dd>{accountsEntity.actived}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.accounts.country">Country</Translate>
          </dt>
          <dd>{accountsEntity.country ? accountsEntity.country.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/accounts" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/accounts/${accountsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ accounts }: IRootState) => ({
  accountsEntity: accounts.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AccountsDetail);
