import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './comment-values.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICommentValuesDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CommentValuesDetail = (props: ICommentValuesDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { commentValuesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commentValuesDetailsHeading">
          <Translate contentKey="automationProfessionalApp.commentValues.detail.title">CommentValues</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commentValuesEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="automationProfessionalApp.commentValues.value">Value</Translate>
            </span>
          </dt>
          <dd>{commentValuesEntity.value}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.commentValues.comment">Comment</Translate>
          </dt>
          <dd>{commentValuesEntity.comment ? commentValuesEntity.comment.id : ''}</dd>
          <dt>
            <Translate contentKey="automationProfessionalApp.commentValues.commentFields">Comment Fields</Translate>
          </dt>
          <dd>{commentValuesEntity.commentFields ? commentValuesEntity.commentFields.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/comment-values" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/comment-values/${commentValuesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ commentValues }: IRootState) => ({
  commentValuesEntity: commentValues.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CommentValuesDetail);
