package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.TasksDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tasks} and its DTO {@link TasksDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TasksMapper extends EntityMapper<TasksDTO, Tasks> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TasksDTO toDtoId(Tasks tasks);
}
