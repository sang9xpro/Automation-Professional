import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './most-of-cont-values.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMostOfContValuesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MostOfContValuesDetail = (props: IMostOfContValuesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { mostOfContValuesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mostOfContValuesDetailsHeading">
          <Translate contentKey="automationProfessionalApp.mostOfContValues.detail.title">MostOfContValues</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mostOfContValuesEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="automationProfessionalApp.mostOfContValues.value">Value</Translate>
            </span>
          </dt>
          <dd>{mostOfContValuesEntity.value}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.mostOfContValues.mostOfContent">Most Of Content</Translate>
          </dt>
          <dd>{mostOfContValuesEntity.mostOfContent ? mostOfContValuesEntity.mostOfContent.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.mostOfContValues.mostOfContFields">Most Of Cont Fields</Translate>
          </dt>
          <dd>{mostOfContValuesEntity.mostOfContFields ? mostOfContValuesEntity.mostOfContFields.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/most-of-cont-values" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/most-of-cont-values/${mostOfContValuesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ mostOfContValues }: IRootState) => ({
  mostOfContValuesEntity: mostOfContValues.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MostOfContValuesDetail);
