import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './history.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHistoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HistoryDetail = (props: IHistoryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { historyEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="historyDetailsHeading">
          <Translate contentKey="automationProfessionalApp.history.detail.title">History</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{historyEntity.id}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="automationProfessionalApp.history.url">Url</Translate>
            </span>
          </dt>
          <dd>{historyEntity.url}</dd>
          <dt>
            <span id="taskId">
              <Translate contentKey="automationProfessionalApp.history.taskId">Task Id</Translate>
            </span>
          </dt>
          <dd>{historyEntity.taskId}</dd>
          <dt>
            <span id="deviceId">
              <Translate contentKey="automationProfessionalApp.history.deviceId">Device Id</Translate>
            </span>
          </dt>
          <dd>{historyEntity.deviceId}</dd>
          <dt>
            <span id="accountId">
              <Translate contentKey="automationProfessionalApp.history.accountId">Account Id</Translate>
            </span>
          </dt>
          <dd>{historyEntity.accountId}</dd>
          <dt>
            <span id="lastUpdate">
              <Translate contentKey="automationProfessionalApp.history.lastUpdate">Last Update</Translate>
            </span>
          </dt>
          <dd>{historyEntity.lastUpdate ? <TextFormat value={historyEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/history" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/history/${historyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ history }: IRootState) => ({
  historyEntity: history.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HistoryDetail);
