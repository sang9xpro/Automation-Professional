import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import loggers, {
  LoggersState
} from 'app/entities/loggers/loggers.reducer';
// prettier-ignore
import loggersFields, {
  LoggersFieldsState
} from 'app/entities/loggers-fields/loggers-fields.reducer';
// prettier-ignore
import loggersValues, {
  LoggersValuesState
} from 'app/entities/loggers-values/loggers-values.reducer';
// prettier-ignore
import history, {
  HistoryState
} from 'app/entities/history/history.reducer';
// prettier-ignore
import historyFields, {
  HistoryFieldsState
} from 'app/entities/history-fields/history-fields.reducer';
// prettier-ignore
import historyValues, {
  HistoryValuesState
} from 'app/entities/history-values/history-values.reducer';
// prettier-ignore
import comment, {
  CommentState
} from 'app/entities/comment/comment.reducer';
// prettier-ignore
import commentFields, {
  CommentFieldsState
} from 'app/entities/comment-fields/comment-fields.reducer';
// prettier-ignore
import commentValues, {
  CommentValuesState
} from 'app/entities/comment-values/comment-values.reducer';
// prettier-ignore
import accounts, {
  AccountsState
} from 'app/entities/accounts/accounts.reducer';
// prettier-ignore
import accountFields, {
  AccountFieldsState
} from 'app/entities/account-fields/account-fields.reducer';
// prettier-ignore
import accountValues, {
  AccountValuesState
} from 'app/entities/account-values/account-values.reducer';
// prettier-ignore
import schedulers, {
  SchedulersState
} from 'app/entities/schedulers/schedulers.reducer';
// prettier-ignore
import schedulerFields, {
  SchedulerFieldsState
} from 'app/entities/scheduler-fields/scheduler-fields.reducer';
// prettier-ignore
import schedulerValue, {
  SchedulerValueState
} from 'app/entities/scheduler-value/scheduler-value.reducer';
// prettier-ignore
import tasks, {
  TasksState
} from 'app/entities/tasks/tasks.reducer';
// prettier-ignore
import taskFields, {
  TaskFieldsState
} from 'app/entities/task-fields/task-fields.reducer';
// prettier-ignore
import taskValues, {
  TaskValuesState
} from 'app/entities/task-values/task-values.reducer';
// prettier-ignore
import devices, {
  DevicesState
} from 'app/entities/devices/devices.reducer';
// prettier-ignore
import devicesFields, {
  DevicesFieldsState
} from 'app/entities/devices-fields/devices-fields.reducer';
// prettier-ignore
import deviceValues, {
  DeviceValuesState
} from 'app/entities/device-values/device-values.reducer';
// prettier-ignore
import schedulerTaskDevice, {
  SchedulerTaskDeviceState
} from 'app/entities/scheduler-task-device/scheduler-task-device.reducer';
// prettier-ignore
import schedulerTaskDeviceFields, {
  SchedulerTaskDeviceFieldsState
} from 'app/entities/scheduler-task-device-fields/scheduler-task-device-fields.reducer';
// prettier-ignore
import schedulerTaskDeviceValues, {
  SchedulerTaskDeviceValuesState
} from 'app/entities/scheduler-task-device-values/scheduler-task-device-values.reducer';
// prettier-ignore
import facebook, {
  FacebookState
} from 'app/entities/facebook/facebook.reducer';
// prettier-ignore
import facebookFields, {
  FacebookFieldsState
} from 'app/entities/facebook-fields/facebook-fields.reducer';
// prettier-ignore
import facebookValues, {
  FacebookValuesState
} from 'app/entities/facebook-values/facebook-values.reducer';
// prettier-ignore
import mostOfContent, {
  MostOfContentState
} from 'app/entities/most-of-content/most-of-content.reducer';
// prettier-ignore
import mostOfContFields, {
  MostOfContFieldsState
} from 'app/entities/most-of-cont-fields/most-of-cont-fields.reducer';
// prettier-ignore
import mostOfContValues, {
  MostOfContValuesState
} from 'app/entities/most-of-cont-values/most-of-cont-values.reducer';
// prettier-ignore
import country, {
  CountryState
} from 'app/entities/country/country.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly loggers: LoggersState;
  readonly loggersFields: LoggersFieldsState;
  readonly loggersValues: LoggersValuesState;
  readonly history: HistoryState;
  readonly historyFields: HistoryFieldsState;
  readonly historyValues: HistoryValuesState;
  readonly comment: CommentState;
  readonly commentFields: CommentFieldsState;
  readonly commentValues: CommentValuesState;
  readonly accounts: AccountsState;
  readonly accountFields: AccountFieldsState;
  readonly accountValues: AccountValuesState;
  readonly schedulers: SchedulersState;
  readonly schedulerFields: SchedulerFieldsState;
  readonly schedulerValue: SchedulerValueState;
  readonly tasks: TasksState;
  readonly taskFields: TaskFieldsState;
  readonly taskValues: TaskValuesState;
  readonly devices: DevicesState;
  readonly devicesFields: DevicesFieldsState;
  readonly deviceValues: DeviceValuesState;
  readonly schedulerTaskDevice: SchedulerTaskDeviceState;
  readonly schedulerTaskDeviceFields: SchedulerTaskDeviceFieldsState;
  readonly schedulerTaskDeviceValues: SchedulerTaskDeviceValuesState;
  readonly facebook: FacebookState;
  readonly facebookFields: FacebookFieldsState;
  readonly facebookValues: FacebookValuesState;
  readonly mostOfContent: MostOfContentState;
  readonly mostOfContFields: MostOfContFieldsState;
  readonly mostOfContValues: MostOfContValuesState;
  readonly country: CountryState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  loggers,
  loggersFields,
  loggersValues,
  history,
  historyFields,
  historyValues,
  comment,
  commentFields,
  commentValues,
  accounts,
  accountFields,
  accountValues,
  schedulers,
  schedulerFields,
  schedulerValue,
  tasks,
  taskFields,
  taskValues,
  devices,
  devicesFields,
  deviceValues,
  schedulerTaskDevice,
  schedulerTaskDeviceFields,
  schedulerTaskDeviceValues,
  facebook,
  facebookFields,
  facebookValues,
  mostOfContent,
  mostOfContFields,
  mostOfContValues,
  country,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
