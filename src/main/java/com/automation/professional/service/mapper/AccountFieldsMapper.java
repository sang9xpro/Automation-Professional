package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.AccountFieldsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountFields} and its DTO {@link AccountFieldsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccountFieldsMapper extends EntityMapper<AccountFieldsDTO, AccountFields> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AccountFieldsDTO toDtoId(AccountFields accountFields);
}
