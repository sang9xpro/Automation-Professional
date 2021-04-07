package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.TaskValuesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaskValues} and its DTO {@link TaskValuesDTO}.
 */
@Mapper(componentModel = "spring", uses = { TasksMapper.class, TaskFieldsMapper.class })
public interface TaskValuesMapper extends EntityMapper<TaskValuesDTO, TaskValues> {
    @Mapping(target = "tasks", source = "tasks", qualifiedByName = "id")
    @Mapping(target = "taskFields", source = "taskFields", qualifiedByName = "id")
    TaskValuesDTO toDto(TaskValues s);
}
