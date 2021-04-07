import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './facebook-values.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFacebookValuesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FacebookValuesDetail = (props: IFacebookValuesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { facebookValuesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="facebookValuesDetailsHeading">
          <Translate contentKey="automationProfessionalApp.facebookValues.detail.title">FacebookValues</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{facebookValuesEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="automationProfessionalApp.facebookValues.value">Value</Translate>
            </span>
          </dt>
          <dd>{facebookValuesEntity.value}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.facebookValues.facebook">Facebook</Translate>
          </dt>
          <dd>{facebookValuesEntity.facebook ? facebookValuesEntity.facebook.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.facebookValues.facebookFields">Facebook Fields</Translate>
          </dt>
          <dd>{facebookValuesEntity.facebookFields ? facebookValuesEntity.facebookFields.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/facebook-values" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/facebook-values/${facebookValuesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ facebookValues }: IRootState) => ({
  facebookValuesEntity: facebookValues.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FacebookValuesDetail);
