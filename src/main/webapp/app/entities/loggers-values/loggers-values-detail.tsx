import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './loggers-values.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILoggersValuesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LoggersValuesDetail = (props: ILoggersValuesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { loggersValuesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="loggersValuesDetailsHeading">
          <Translate contentKey="automationProfessionalApp.loggersValues.detail.title">LoggersValues</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{loggersValuesEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="automationProfessionalApp.loggersValues.value">Value</Translate>
            </span>
          </dt>
          <dd>{loggersValuesEntity.value}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.loggersValues.loggers">Loggers</Translate>
          </dt>
          <dd>{loggersValuesEntity.loggers ? loggersValuesEntity.loggers.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.loggersValues.loggersFields">Loggers Fields</Translate>
          </dt>
          <dd>{loggersValuesEntity.loggersFields ? loggersValuesEntity.loggersFields.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/loggers-values" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/loggers-values/${loggersValuesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ loggersValues }: IRootState) => ({
  loggersValuesEntity: loggersValues.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LoggersValuesDetail);
