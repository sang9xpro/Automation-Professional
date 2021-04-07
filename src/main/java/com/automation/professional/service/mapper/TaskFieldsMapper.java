package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.TaskFieldsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaskFields} and its DTO {@link TaskFieldsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaskFieldsMapper extends EntityMapper<TaskFieldsDTO, TaskFields> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskFieldsDTO toDtoId(TaskFields taskFields);
}
