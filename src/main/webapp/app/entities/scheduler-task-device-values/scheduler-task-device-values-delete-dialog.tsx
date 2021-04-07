import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity, deleteEntity } from './scheduler-task-device-values.reducer';

export interface ISchedulerTaskDeviceValuesDeleteDialogProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SchedulerTaskDeviceValuesDeleteDialog = (props: ISchedulerTaskDeviceValuesDeleteDialogProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const handleClose = () => {
    props.history.push('/scheduler-task-device-values' + props.location.search);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const confirmDelete = () => {
    props.deleteEntity(props.schedulerTaskDeviceValuesEntity.id);
  };

  const { schedulerTaskDeviceValuesEntity } = props;
  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="schedulerTaskDeviceValuesDeleteDialogHeading">
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody id="automationProfessionalApp.schedulerTaskDeviceValues.delete.question">
        <Translate
          contentKey="automationProfessionalApp.schedulerTaskDeviceValues.delete.question"
          interpolate={{ id: schedulerTaskDeviceValuesEntity.id }}
        >
          Are you sure you want to delete this SchedulerTaskDeviceValues?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button
          id="jhi-confirm-delete-schedulerTaskDeviceValues"
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

const mapStateToProps = ({ schedulerTaskDeviceValues }: IRootState) => ({
  schedulerTaskDeviceValuesEntity: schedulerTaskDeviceValues.entity,
  updateSuccess: schedulerTaskDeviceValues.updateSuccess,
});

const mapDispatchToProps = { getEntity, deleteEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerTaskDeviceValuesDeleteDialog);
