package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.MostOfContFieldsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MostOfContFields} and its DTO {@link MostOfContFieldsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MostOfContFieldsMapper extends EntityMapper<MostOfContFieldsDTO, MostOfContFields> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MostOfContFieldsDTO toDtoId(MostOfContFields mostOfContFields);
}
