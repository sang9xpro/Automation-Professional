import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './history-fields.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHistoryFieldsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HistoryFieldsDetail = (props: IHistoryFieldsDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { historyFieldsEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="historyFieldsDetailsHeading">
          <Translate contentKey="automationProfessionalApp.historyFields.detail.title">HistoryFields</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{historyFieldsEntity.id}</dd>
          <dt>
            <span id="fieldName">
              <Translate contentKey="automationProfessionalApp.historyFields.fieldName">Field Name</Translate>
            </span>
          </dt>
          <dd>{historyFieldsEntity.fieldName}</dd>
        </dl>
        <Button tag={Link} to="/history-fields" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/history-fields/${historyFieldsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ historyFields }: IRootState) => ({
  historyFieldsEntity: historyFields.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HistoryFieldsDetail);
