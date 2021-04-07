import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './scheduler-task-device-fields.reducer';

export interface ISchedulerTaskDeviceFieldsDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulerTaskDeviceFieldsDeleteDialog = (props: ISchedulerTaskDeviceFieldsDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/scheduler-task-device-fields' + props.location.search);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.schedulerTaskDeviceFieldsEntity.id);
  };

  const { schedulerTaskDeviceFieldsEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="schedulerTaskDeviceFieldsDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="automationProfessionalApp.schedulerTaskDeviceFields.delete.question">
        <Translate
          contentKey="automationProfessionalApp.schedulerTaskDeviceFields.delete.question"
          interpolate={{ id: schedulerTaskDeviceFieldsEntity.id }}
        >
          Are you sure you want to delete this SchedulerTaskDeviceFields?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button
          id="jhi-confirm-delete-schedulerTaskDeviceFields"
          data-cy="entityConfirmDeleteButton"
          color="danger"
          onClick={confirmDelete}
        >
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

const mapStateToProps = ({ schedulerTaskDeviceFields }: IRootState) => ({
  schedulerTaskDeviceFieldsEntity: schedulerTaskDeviceFields.entity,
  updateSuccess: schedulerTaskDeviceFields.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerTaskDeviceFieldsDeleteDialog);
