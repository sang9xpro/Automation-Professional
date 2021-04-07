import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './account-fields.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAccountFieldsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AccountFieldsDetail = (props: IAccountFieldsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { accountFieldsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="accountFieldsDetailsHeading">
          <Translate contentKey="automationProfessionalApp.accountFields.detail.title">AccountFields</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{accountFieldsEntity.id}</dd>
          <dt>
            <span id="fieldName">
              <Translate contentKey="automationProfessionalApp.accountFields.fieldName">Field Name</Translate>
            </span>
          </dt>
          <dd>{accountFieldsEntity.fieldName}</dd>
        </dl>
        <Button tag={Link} to="/account-fields" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/account-fields/${accountFieldsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ accountFields }: IRootState) => ({
  accountFieldsEntity: accountFields.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AccountFieldsDetail);
