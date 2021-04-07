package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.MostOfContValuesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MostOfContValues} and its DTO {@link MostOfContValuesDTO}.
 */
@Mapper(componentModel = "spring", uses = { MostOfContentMapper.class, MostOfContFieldsMapper.class })
public interface MostOfContValuesMapper extends EntityMapper<MostOfContValuesDTO, MostOfContValues> {
    @Mapping(target = "mostOfContent", source = "mostOfContent", qualifiedByName = "id")
    @Mapping(target = "mostOfContFields", source = "mostOfContFields", qualifiedByName = "id")
    MostOfContValuesDTO toDto(MostOfContValues s);
}
