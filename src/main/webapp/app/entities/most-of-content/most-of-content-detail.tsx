import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './most-of-content.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMostOfContentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MostOfContentDetail = (props: IMostOfContentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { mostOfContentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mostOfContentDetailsHeading">
          <Translate contentKey="automationProfessionalApp.mostOfContent.detail.title">MostOfContent</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mostOfContentEntity.id}</dd>
          <dt>
            <span id="urlOriginal">
              <Translate contentKey="automationProfessionalApp.mostOfContent.urlOriginal">Url Original</Translate>
            </span>
          </dt>
          <dd>{mostOfContentEntity.urlOriginal}</dd>
          <dt>
            <span id="contentText">
              <Translate contentKey="automationProfessionalApp.mostOfContent.contentText">Content Text</Translate>
            </span>
          </dt>
          <dd>{mostOfContentEntity.contentText}</dd>
          <dt>
            <span id="postTime">
              <Translate contentKey="automationProfessionalApp.mostOfContent.postTime">Post Time</Translate>
            </span>
          </dt>
          <dd>
            {mostOfContentEntity.postTime ? <TextFormat value={mostOfContentEntity.postTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="numberLike">
              <Translate contentKey="automationProfessionalApp.mostOfContent.numberLike">Number Like</Translate>
            </span>
          </dt>
          <dd>{mostOfContentEntity.numberLike}</dd>
          <dt>
            <span id="numberComment">
              <Translate contentKey="automationProfessionalApp.mostOfContent.numberComment">Number Comment</Translate>
            </span>
          </dt>
          <dd>{mostOfContentEntity.numberComment}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.mostOfContent.facebook">Facebook</Translate>
          </dt>
          <dd>{mostOfContentEntity.facebook ? mostOfContentEntity.facebook.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/most-of-content" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/most-of-content/${mostOfContentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ mostOfContent }: IRootState) => ({
  mostOfContentEntity: mostOfContent.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MostOfContentDetail);
