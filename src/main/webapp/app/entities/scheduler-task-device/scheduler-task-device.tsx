import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import {
  Translate,
  translate,
  ICrudSearchAction,
  TextFormat,
  getSortState,
  IPaginationBaseState,
  JhiPagination,
  JhiItemCount,
} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './scheduler-task-device.reducer';
import { ISchedulerTaskDevice } from 'app/shared/model/scheduler-task-device.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';

export interface ISchedulerTaskDeviceProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SchedulerTaskDevice = (props: ISchedulerTaskDeviceProps) => {
  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const getAllEntities = () => {
    if (search) {
      props.getSearchEntities(
        search,
        paginationState.activePage - 1,
        paginationState.itemsPerPage,
        `${paginationState.sort},${paginationState.order}`
      );
    } else {
      props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
    }
  };

  const startSearching = () => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      props.getSearchEntities(
        search,
        paginationState.activePage - 1,
        paginationState.itemsPerPage,
        `${paginationState.sort},${paginationState.order}`
      );
    }
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    props.getEntities();
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get('sort');
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { schedulerTaskDeviceList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="scheduler-task-device-heading" data-cy="SchedulerTaskDeviceHeading">
        <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.home.title">Scheduler Task Devices</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.home.createLabel">
              Create new Scheduler Task Device
            </Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <AvForm onSubmit={startSearching}>
            <AvGroup>
              <InputGroup>
                <AvInput
                  type="text"
                  name="search"
                  value={search}
                  onChange={handleSearch}
                  placeholder={translate('automationProfessionalApp.schedulerTaskDevice.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </AvGroup>
          </AvForm>
        </Col>
      </Row>
      <div className="table-responsive">
        {schedulerTaskDeviceList && schedulerTaskDeviceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('startTime')}>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.startTime">Start Time</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('endTime')}>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.endTime">End Time</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('countFrom')}>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.countFrom">Count From</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('countTo')}>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.countTo">Count To</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('point')}>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.point">Point</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastUpdate')}>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.lastUpdate">Last Update</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('owner')}>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.owner">Owner</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.schedulers">Schedulers</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.tasks">Tasks</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {schedulerTaskDeviceList.map((schedulerTaskDevice, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${schedulerTaskDevice.id}`} color="link" size="sm">
                      {schedulerTaskDevice.id}
                    </Button>
                  </td>
                  <td>
                    {schedulerTaskDevice.startTime ? (
                      <TextFormat type="date" value={schedulerTaskDevice.startTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {schedulerTaskDevice.endTime ? (
                      <TextFormat type="date" value={schedulerTaskDevice.endTime} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{schedulerTaskDevice.countFrom}</td>
                  <td>{schedulerTaskDevice.countTo}</td>
                  <td>{schedulerTaskDevice.point}</td>
                  <td>
                    {schedulerTaskDevice.lastUpdate ? (
                      <TextFormat type="date" value={schedulerTaskDevice.lastUpdate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{schedulerTaskDevice.owner}</td>
                  <td>
                    <Translate contentKey={`automationProfessionalApp.SchedulerStatus.${schedulerTaskDevice.status}`} />
                  </td>
                  <td>
                    {schedulerTaskDevice.schedulers ? (
                      <Link to={`schedulers/${schedulerTaskDevice.schedulers.id}`}>{schedulerTaskDevice.schedulers.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {schedulerTaskDevice.tasks ? (
                      <Link to={`tasks/${schedulerTaskDevice.tasks.id}`}>{schedulerTaskDevice.tasks.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${schedulerTaskDevice.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${schedulerTaskDevice.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${schedulerTaskDevice.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="automationProfessionalApp.schedulerTaskDevice.home.notFound">
                No Scheduler Task Devices found
              </Translate>
            </div>
          )
        )}
      </div>
      {props.totalItems ? (
        <div className={schedulerTaskDeviceList && schedulerTaskDeviceList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={props.totalItems}
            />
          </Row>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

const mapStateToProps = ({ schedulerTaskDevice }: IRootState) => ({
  schedulerTaskDeviceList: schedulerTaskDevice.entities,
  loading: schedulerTaskDevice.loading,
  totalItems: schedulerTaskDevice.totalItems,
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchedulerTaskDevice);
