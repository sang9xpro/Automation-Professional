import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './history-values.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHistoryValuesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HistoryValuesDetail = (props: IHistoryValuesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { historyValuesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="historyValuesDetailsHeading">
          <Translate contentKey="automationProfessionalApp.historyValues.detail.title">HistoryValues</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{historyValuesEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="automationProfessionalApp.historyValues.value">Value</Translate>
            </span>
          </dt>
          <dd>{historyValuesEntity.value}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.historyValues.history">History</Translate>
          </dt>
          <dd>{historyValuesEntity.history ? historyValuesEntity.history.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.historyValues.historyFields">History Fields</Translate>
          </dt>
          <dd>{historyValuesEntity.historyFields ? historyValuesEntity.historyFields.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/history-values" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/history-values/${historyValuesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ historyValues }: IRootState) => ({
  historyValuesEntity: historyValues.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HistoryValuesDetail);
