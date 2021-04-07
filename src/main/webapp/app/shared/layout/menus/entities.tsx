import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/loggers">
      <Translate contentKey="global.menu.entities.loggers" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/loggers-fields">
      <Translate contentKey="global.menu.entities.loggersFields" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/loggers-values">
      <Translate contentKey="global.menu.entities.loggersValues" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/history">
      <Translate contentKey="global.menu.entities.history" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/history-fields">
      <Translate contentKey="global.menu.entities.historyFields" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/history-values">
      <Translate contentKey="global.menu.entities.historyValues" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/comment">
      <Translate contentKey="global.menu.entities.comment" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/comment-fields">
      <Translate contentKey="global.menu.entities.commentFields" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/comment-values">
      <Translate contentKey="global.menu.entities.commentValues" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/accounts">
      <Translate contentKey="global.menu.entities.accounts" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/account-fields">
      <Translate contentKey="global.menu.entities.accountFields" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/account-values">
      <Translate contentKey="global.menu.entities.accountValues" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/schedulers">
      <Translate contentKey="global.menu.entities.schedulers" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/scheduler-fields">
      <Translate contentKey="global.menu.entities.schedulerFields" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/scheduler-value">
      <Translate contentKey="global.menu.entities.schedulerValue" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/tasks">
      <Translate contentKey="global.menu.entities.tasks" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/task-fields">
      <Translate contentKey="global.menu.entities.taskFields" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/task-values">
      <Translate contentKey="global.menu.entities.taskValues" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/devices">
      <Translate contentKey="global.menu.entities.devices" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/devices-fields">
      <Translate contentKey="global.menu.entities.devicesFields" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/device-values">
      <Translate contentKey="global.menu.entities.deviceValues" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/scheduler-task-device">
      <Translate contentKey="global.menu.entities.schedulerTaskDevice" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/scheduler-task-device-fields">
      <Translate contentKey="global.menu.entities.schedulerTaskDeviceFields" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/scheduler-task-device-values">
      <Translate contentKey="global.menu.entities.schedulerTaskDeviceValues" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/facebook">
      <Translate contentKey="global.menu.entities.facebook" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/facebook-fields">
      <Translate contentKey="global.menu.entities.facebookFields" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/facebook-values">
      <Translate contentKey="global.menu.entities.facebookValues" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/most-of-content">
      <Translate contentKey="global.menu.entities.mostOfContent" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/most-of-cont-fields">
      <Translate contentKey="global.menu.entities.mostOfContFields" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/most-of-cont-values">
      <Translate contentKey="global.menu.entities.mostOfContValues" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/country">
      <Translate contentKey="global.menu.entities.country" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
