package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.FacebookFieldsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacebookFields} and its DTO {@link FacebookFieldsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FacebookFieldsMapper extends EntityMapper<FacebookFieldsDTO, FacebookFields> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacebookFieldsDTO toDtoId(FacebookFields facebookFields);
}
