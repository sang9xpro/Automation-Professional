import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './schedulers.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISchedulersDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulersDetail = (props: ISchedulersDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { schedulersEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="schedulersDetailsHeading">
          <Translate contentKey="automationProfessionalApp.schedulers.detail.title">Schedulers</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{schedulersEntity.id}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="automationProfessionalApp.schedulers.url">Url</Translate>
            </span>
          </dt>
          <dd>{schedulersEntity.url}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="automationProfessionalApp.schedulers.title">Title</Translate>
            </span>
          </dt>
          <dd>{schedulersEntity.title}</dd>
          <dt>
            <span id="otherSource">
              <Translate contentKey="automationProfessionalApp.schedulers.otherSource">Other Source</Translate>
            </span>
          </dt>
          <dd>{schedulersEntity.otherSource}</dd>
          <dt>
            <span id="count">
              <Translate contentKey="automationProfessionalApp.schedulers.count">Count</Translate>
            </span>
          </dt>
          <dd>{schedulersEntity.count}</dd>
          <dt>
            <span id="lastUpdate">
              <Translate contentKey="automationProfessionalApp.schedulers.lastUpdate">Last Update</Translate>
            </span>
          </dt>
          <dd>
            {schedulersEntity.lastUpdate ? <TextFormat value={schedulersEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="owner">
              <Translate contentKey="automationProfessionalApp.schedulers.owner">Owner</Translate>
            </span>
          </dt>
          <dd>{schedulersEntity.owner}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="automationProfessionalApp.schedulers.status">Status</Translate>
            </span>
          </dt>
          <dd>{schedulersEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/schedulers" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/schedulers/${schedulersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ schedulers }: IRootState) => ({
  schedulersEntity: schedulers.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulersDetail);
