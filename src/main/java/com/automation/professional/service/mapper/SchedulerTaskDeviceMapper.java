package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.SchedulerTaskDeviceDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchedulerTaskDevice} and its DTO {@link SchedulerTaskDeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchedulersMapper.class, TasksMapper.class })
public interface SchedulerTaskDeviceMapper extends EntityMapper<SchedulerTaskDeviceDTO, SchedulerTaskDevice> {
    @Mapping(target = "schedulers", source = "schedulers", qualifiedByName = "id")
    @Mapping(target = "tasks", source = "tasks", qualifiedByName = "id")
    SchedulerTaskDeviceDTO toDto(SchedulerTaskDevice s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SchedulerTaskDeviceDTO toDtoId(SchedulerTaskDevice schedulerTaskDevice);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<SchedulerTaskDeviceDTO> toDtoIdSet(Set<SchedulerTaskDevice> schedulerTaskDevice);
}
