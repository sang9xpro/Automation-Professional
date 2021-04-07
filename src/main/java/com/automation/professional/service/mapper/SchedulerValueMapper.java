package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.SchedulerValueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchedulerValue} and its DTO {@link SchedulerValueDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchedulersMapper.class, SchedulerFieldsMapper.class })
public interface SchedulerValueMapper extends EntityMapper<SchedulerValueDTO, SchedulerValue> {
    @Mapping(target = "schedulers", source = "schedulers", qualifiedByName = "id")
    @Mapping(target = "schedulerFields", source = "schedulerFields", qualifiedByName = "id")
    SchedulerValueDTO toDto(SchedulerValue s);
}
