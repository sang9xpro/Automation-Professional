package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.SchedulerTaskDeviceFieldsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchedulerTaskDeviceFields} and its DTO {@link SchedulerTaskDeviceFieldsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SchedulerTaskDeviceFieldsMapper extends EntityMapper<SchedulerTaskDeviceFieldsDTO, SchedulerTaskDeviceFields> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SchedulerTaskDeviceFieldsDTO toDtoId(SchedulerTaskDeviceFields schedulerTaskDeviceFields);
}
