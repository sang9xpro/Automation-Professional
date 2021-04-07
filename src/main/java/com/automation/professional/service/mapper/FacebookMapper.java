package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.FacebookDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Facebook} and its DTO {@link FacebookDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class })
public interface FacebookMapper extends EntityMapper<FacebookDTO, Facebook> {
    @Mapping(target = "country", source = "country", qualifiedByName = "id")
    FacebookDTO toDto(Facebook s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacebookDTO toDtoId(Facebook facebook);
}
