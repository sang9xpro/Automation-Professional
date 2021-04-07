package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.SchedulersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Schedulers} and its DTO {@link SchedulersDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SchedulersMapper extends EntityMapper<SchedulersDTO, Schedulers> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SchedulersDTO toDtoId(Schedulers schedulers);
}
