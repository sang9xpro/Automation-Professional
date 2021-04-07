package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.CommentFieldsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommentFields} and its DTO {@link CommentFieldsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CommentFieldsMapper extends EntityMapper<CommentFieldsDTO, CommentFields> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommentFieldsDTO toDtoId(CommentFields commentFields);
}
