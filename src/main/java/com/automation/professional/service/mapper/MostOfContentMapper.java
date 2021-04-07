package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.MostOfContentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MostOfContent} and its DTO {@link MostOfContentDTO}.
 */
@Mapper(componentModel = "spring", uses = { FacebookMapper.class })
public interface MostOfContentMapper extends EntityMapper<MostOfContentDTO, MostOfContent> {
    @Mapping(target = "facebook", source = "facebook", qualifiedByName = "id")
    MostOfContentDTO toDto(MostOfContent s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MostOfContentDTO toDtoId(MostOfContent mostOfContent);
}
